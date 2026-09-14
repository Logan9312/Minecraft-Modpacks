package ca.loganlab.playercartphysics;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerEntity.class)
abstract class TrackingMixin {
    @Definition(id = "AbstractMinecart", type = AbstractMinecart.class)
    @Expression("? instanceof AbstractMinecart")
    @WrapOperation(method = "sendChanges", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean useVanillaClientPackets(Object entity, Operation<Boolean> original) {
        // Clients choose their controller from the world flag, not the passenger.
        return original.call(entity) && AbstractMinecart.useExperimentalMovement(((AbstractMinecart) entity).level());
    }
}
