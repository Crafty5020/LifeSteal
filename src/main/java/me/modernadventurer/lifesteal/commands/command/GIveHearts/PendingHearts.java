package me.modernadventurer.lifesteal.commands.command.GIveHearts;

import com.mojang.brigadier.context.CommandContext;
import me.modernadventurer.lifesteal.Loader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PendingHearts {
    public static final ConcurrentHashMap<UUID, PendingData> PENDING = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();


    public static class PendingData {
        public Collection<ServerPlayerEntity> targets;
        public int amount;
        public ServerPlayerEntity player;

        public PendingData(Collection<ServerPlayerEntity> targets, int amount, ServerPlayerEntity player) {
            this.targets = targets;
            this.amount = amount;
            this.player = player;
        }
    }

    public static void addPending(UUID playerUUID, PendingData data, ServerPlayerEntity player) {
        PENDING.put(playerUUID, data);
        ServerWorld world = player.getEntityWorld();

        int timeoutSeconds = world.getGameRules().getValue(Loader.PENDINGHEARTSTIMEOUT);
        // Schedule automatic expiry
        scheduler.schedule(() -> {
            PendingData pending = PENDING.remove(playerUUID);
            if (pending != null) {
                // Notify the player that time expired
                pending.player.sendMessage(Text.literal("Pending heart request expired!"), false);
            }
        }, timeoutSeconds, TimeUnit.SECONDS);
    }
}
