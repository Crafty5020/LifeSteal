package me.modernadventurer.lifesteal.commands.command.GIveHearts;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.modernadventurer.lifesteal.commands.command.maxHealth.UpdateMaxHealth;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;


public class GiveHearts {
    public static int give(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerCommandSource source = ctx.getSource();
        Collection<ServerPlayerEntity> playersToSend = EntityArgumentType.getPlayers(ctx, "give_to_player");
        int heartAmount = IntegerArgumentType.getInteger(ctx, "heart_amount");
        UUID playerUUID = Objects.requireNonNull(source.getPlayer()).getUuid();

        StringBuilder names = new StringBuilder();
        for (ServerPlayerEntity player : playersToSend) {
            if (!names.isEmpty()) names.append(", "); // comma separation
            names.append(player.getName().getString()); // get player name as string
        }

        PendingHearts.addPending(playerUUID,new PendingHearts.PendingData(playersToSend, heartAmount, source.getPlayer()), source.getPlayer());

        source.sendFeedback(() -> Text.of(Text.literal("Are you sure you want to give hearts to " + names) + ". If yes type \"/lifesteal give_hearts accept\""), false);

        return Command.SINGLE_SUCCESS;
    }
    public static int accept(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource source = ctx.getSource();
        UUID playerUUID = Objects.requireNonNull(source.getPlayer()).getUuid();

        PendingHearts.PendingData pending = PendingHearts.PENDING.get(playerUUID);

        if (pending == null) {
            source.sendFeedback(() -> Text.literal("You have no pending heart requests."), false);
            return 0;
        }

// if "yes" command
        UpdateMaxHealth.update(pending.targets,pending.amount);
        source.sendFeedback(() -> Text.literal("Hearts given successfully!"), false);

// remove from pending
        PendingHearts.PENDING.remove(playerUUID);
        return Command.SINGLE_SUCCESS;
    }
    public static int cancel(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource source = ctx.getSource();
        UUID playerUUID = Objects.requireNonNull(source.getPlayer()).getUuid();

        PendingHearts.PendingData pending = PendingHearts.PENDING.get(playerUUID);

        if (pending == null) {
            source.sendFeedback(() -> Text.literal("You have no pending heart requests."), false);
            return 0;
        }

        source.sendFeedback(() -> Text.literal("Cancelled pending heart request!"), false);
        PendingHearts.PENDING.remove(playerUUID);

        return Command.SINGLE_SUCCESS;
    }
}
