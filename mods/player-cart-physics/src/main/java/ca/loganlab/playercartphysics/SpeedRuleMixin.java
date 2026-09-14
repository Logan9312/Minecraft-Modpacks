package ca.loganlab.playercartphysics;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRule.class)
abstract class SpeedRuleMixin {
    @ModifyReturnValue(method = "requiredFeatures", at = @At("RETURN"))
    private FeatureFlagSet exposeExistingSpeedRule(FeatureFlagSet original) {
        return (Object) this == GameRules.MAX_MINECART_SPEED ? FeatureFlagSet.of() : original;
    }
}
