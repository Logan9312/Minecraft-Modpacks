package ca.loganlab.playercartphysics;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.entity.vehicle.minecart.NewMinecartBehavior;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CollisionContext.class)
interface CollisionMixin {
    @ModifyExpressionValue(method = "of(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/phys/shapes/CollisionContext;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/minecart/AbstractMinecart;useExperimentalMovement(Lnet/minecraft/world/level/Level;)Z"))
    private static boolean useSelectedPhysics(boolean original, Entity entity) {
        return original || entity instanceof AbstractMinecart cart && cart.getBehavior() instanceof NewMinecartBehavior;
    }
}
