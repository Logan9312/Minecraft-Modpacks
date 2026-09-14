package ca.loganlab.playercartphysics;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.entity.vehicle.minecart.Minecart;
import net.minecraft.world.entity.vehicle.minecart.MinecartBehavior;
import net.minecraft.world.entity.vehicle.minecart.NewMinecartBehavior;
import net.minecraft.world.entity.vehicle.minecart.OldMinecartBehavior;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecart.class)
abstract class CartMixin {
    @Shadow @Final @Mutable private MinecartBehavior behavior;

    @Inject(method = "tick", at = @At("HEAD"))
    private void selectPhysics(CallbackInfo ci) {
        AbstractMinecart cart = (AbstractMinecart) (Object) this;
        // Don't replace other mods' cart subclasses, or override a world-wide experiment.
        if (cart.getClass() != Minecart.class || AbstractMinecart.useExperimentalMovement(cart.level())) return;
        boolean player = cart.getFirstPassenger() instanceof Player;
        if (player && behavior instanceof OldMinecartBehavior) {
            NewMinecartBehavior next = new NewMinecartBehavior(cart);
            behavior = next;
            var pos = cart.getCurrentBlockPosOrRailBelow();
            next.adjustToRails(pos, cart.level().getBlockState(pos), true);
        } else if (!player && behavior instanceof NewMinecartBehavior) {
            behavior = new OldMinecartBehavior(cart);
        }
    }

    // Keep rail lookup, movement and collisions consistent with this tick's controller.
    @ModifyExpressionValue(method = "*", at = @At(value = "INVOKE", target =
            "Lnet/minecraft/world/entity/vehicle/minecart/AbstractMinecart;useExperimentalMovement(Lnet/minecraft/world/level/Level;)Z"))
    private boolean useSelectedPhysics(boolean original) {
        return original || behavior instanceof NewMinecartBehavior;
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void discardUnusedInterpolation(CallbackInfo ci) {
        AbstractMinecart cart = (AbstractMinecart) (Object) this;
        if (!AbstractMinecart.useExperimentalMovement(cart.level()) && behavior instanceof NewMinecartBehavior next) {
            // Vanilla clients in a non-experimental world consume regular position packets.
            next.lerpSteps.clear();
        }
    }
}
