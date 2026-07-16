package me.modernadventurer.lifesteal.events;

import me.modernadventurer.lifesteal.Loader;
import me.modernadventurer.lifesteal.data.GetMyWorldData;
import net.minecraft.block.Blocks;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.function.Predicate;

public class LifestealEvents {
    public static void onEyesDetected(ServerPlayerEntity player) {
        if (GetMyWorldData.get(player.getEntityWorld().toServerWorld()).getEndPortalSpanwed()) return;
        boolean hasEye = player.getInventory().getMainStacks().stream().anyMatch(stack -> stack.isOf(Items.ENDER_EYE));
        // Main inventory
        // Offhand
        net.minecraft.item.ItemStack stack = player.getOffHandStack();
        if (stack.getItem().equals(Items.ENDER_EYE)) {
            hasEye = true;
        }
        if (!hasEye) return;
        Loader.LOGGER.info("player has item");
        GetMyWorldData.get(player.getEntityWorld().toServerWorld()).setEndPortalSpanwed(true);

        ServerWorld world = player.getEntityWorld().toServerWorld();

        BlockPos center = GetMyWorldData.get(world).getPortalPos();
        int levels = world.getGameRules().getValue(Loader.BEACONLEVEL);
        // North side
        for (int x = -1; x <= 1; x++) {
            world.setBlockState(center.add(x, 0, -2),
                    Blocks.END_PORTAL_FRAME.getDefaultState()
                            .with(EndPortalFrameBlock.FACING, Direction.SOUTH));
        }

        // South side
        for (int x = -1; x <= 1; x++) {
            world.setBlockState(center.add(x, 0, 2),
                    Blocks.END_PORTAL_FRAME.getDefaultState()
                            .with(EndPortalFrameBlock.FACING, Direction.NORTH));
        }

        // West side
        for (int z = -1; z <= 1; z++) {
            world.setBlockState(center.add(-2, 0, z),
                    Blocks.END_PORTAL_FRAME.getDefaultState()
                            .with(EndPortalFrameBlock.FACING, Direction.EAST));
        }

        // East side
        for (int z = -1; z <= 1; z++) {
            world.setBlockState(center.add(2, 0, z),
                    Blocks.END_PORTAL_FRAME.getDefaultState()
                            .with(EndPortalFrameBlock.FACING, Direction.WEST));
        }

        world.setBlockState(center.add(0, world.getGameRules().getValue(Loader.BEACONYOFFSET), 0),
                Blocks.BEACON.getDefaultState()
                );

        // Place the beacon on top
        world.setBlockState(center, Blocks.BEACON.getDefaultState());
        for (ServerPlayerEntity worldPlayer : world.getPlayers()) {
            worldPlayer.sendMessage(
                    Text.literal("End Portal Spawned at: " + center),
                    false // false = chat, true = action bar
            );
            worldPlayer.sendMessage(
                    Text.literal("End Portal Spawned at: " + center),
                    true // false = chat, true = action bar
            );
        }
    }

}
