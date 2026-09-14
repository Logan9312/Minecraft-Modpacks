package ca.loganlab.carttests;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.entity.vehicle.minecart.NewMinecartBehavior;
import net.minecraft.world.entity.vehicle.minecart.OldMinecartBehavior;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DetectorRailBlock;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.phys.Vec3;

public class CartTests {
    @GameTest
    public void boardingAndDismounting(GameTestHelper h) {
        var cart = track(h);
        Player player = h.makeMockPlayer(GameType.SURVIVAL);
        h.startSequence()
            .thenExecute(() -> h.assertTrue(cart.getBehavior() instanceof OldMinecartBehavior, "starts vanilla"))
            .thenExecute(() -> player.startRiding(cart, true, false))
            .thenIdle(2)
            .thenExecute(() -> h.assertTrue(cart.getBehavior() instanceof NewMinecartBehavior, "player selects new physics"))
            .thenExecute(player::stopRiding)
            .thenIdle(2)
            .thenExecute(() -> h.assertTrue(cart.getBehavior() instanceof OldMinecartBehavior, "empty returns to old physics"))
            .thenExecute(() -> player.startRiding(cart, true, false))
            .thenIdle(2)
            .thenExecute(() -> h.assertTrue(cart.getBehavior() instanceof NewMinecartBehavior, "can board again"))
            .thenSucceed();
    }

    @GameTest
    public void mobAndStorageCartsStayVanilla(GameTestHelper h) {
        var cart = track(h);
        var pig = h.spawn(EntityTypes.PIG, new BlockPos(1, 2, 1));
        pig.startRiding(cart, true, false);
        var hopper = h.spawn(EntityTypes.HOPPER_MINECART, new BlockPos(4, 1, 1));
        var chest = h.spawn(EntityTypes.CHEST_MINECART, new BlockPos(6, 1, 1));
        h.startSequence().thenIdle(3).thenExecute(() -> {
            for (var c : new AbstractMinecart[]{cart, hopper, chest}) {
                h.assertTrue(c.getBehavior() instanceof OldMinecartBehavior, "non-player cart changed physics");
            }
        }).thenSucceed();
    }

    @GameTest
    public void defaultRuleAndNoMultiplier(GameTestHelper h) {
        h.assertTrue(!AbstractMinecart.useExperimentalMovement(h.getLevel()), "test must run with experiment off");
        h.assertTrue(h.getLevel().getGameRules().get(GameRules.MAX_MINECART_SPEED) == 8, "vanilla default is 8");
        var cart = track(h);
        h.makeMockPlayer(GameType.SURVIVAL).startRiding(cart, true, false);
        double[] x = new double[1];
        h.startSequence().thenIdle(2).thenExecute(() -> {
            cart.setDeltaMovement(new Vec3(2, 0, 0));
            x[0] = cart.getX();
        }).thenIdle(2).thenExecute(() -> {
            double distance = cart.getX() - x[0];
            h.assertTrue(distance > 0.3 && distance <= 0.81, "default cap should move <= 0.8 blocks in two ticks: " + distance);
            h.assertTrue(((NewMinecartBehavior) cart.getBehavior()).lerpSteps.isEmpty(), "unused packets must not accumulate");
        }).thenSucceed();
    }

    @GameTest(environment = "player-cart-physics-test:fast")
    public void gameruleControlsOnlyPlayerCart(GameTestHelper h) {
        var cart = track(h);
        h.getLevel().getGameRules().set(GameRules.MAX_MINECART_SPEED, 24, h.getLevel().getServer());
        Player player = h.makeMockPlayer(GameType.SURVIVAL);
        player.startRiding(cart, true, false);
        double[] x = new double[1];
        h.startSequence().thenIdle(2).thenExecute(() -> {
            cart.setDeltaMovement(new Vec3(2, 0, 0)); x[0] = cart.getX();
        }).thenIdle(1).thenExecute(() -> {
            double distance = cart.getX() - x[0];
            h.assertTrue(distance > 0.8 && distance <= 1.21, "24 bps travel: " + distance);
            player.stopRiding();
        }).thenIdle(1).thenExecute(() -> {
            cart.setDeltaMovement(new Vec3(2, 0, 0)); x[0] = cart.getX();
        }).thenIdle(1).thenExecute(() -> {
            double distance = cart.getX() - x[0];
            h.assertTrue(distance >= 0 && distance <= 0.41, "empty cart retains vanilla cap: " + distance);
            h.getLevel().getGameRules().set(GameRules.MAX_MINECART_SPEED, 8, h.getLevel().getServer());
        }).thenSucceed();
    }

    @GameTest
    public void detectorWorks(GameTestHelper h) {
        var cart = track(h);
        var detector = new BlockPos(3, 1, 1);
        h.setBlock(detector, Blocks.DETECTOR_RAIL.defaultBlockState().setValue(DetectorRailBlock.SHAPE, RailShape.EAST_WEST));
        h.makeMockPlayer(GameType.SURVIVAL).startRiding(cart, true, false);
        cart.setDeltaMovement(new Vec3(0.4, 0, 0));
        h.startSequence().thenWaitUntil(() -> h.assertBlockProperty(detector, DetectorRailBlock.POWERED, true)).thenSucceed();
    }

    private AbstractMinecart track(GameTestHelper h) {
        for (int x = 0; x < 8; x++) {
            h.setBlock(new BlockPos(x, 0, 1), Blocks.STONE);
            h.setBlock(new BlockPos(x, 1, 1), Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, RailShape.EAST_WEST));
        }
        return h.spawn(EntityTypes.MINECART, new BlockPos(1, 1, 1));
    }
}
