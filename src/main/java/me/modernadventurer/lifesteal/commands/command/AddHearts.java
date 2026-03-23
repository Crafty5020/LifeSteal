package me.modernadventurer.lifesteal.commands.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.modernadventurer.lifesteal.Loader;
import me.modernadventurer.lifesteal.commands.command.maxHealth.UpdateMaxHealth;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Collection;

public class AddHearts {
    public static int Add(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(ctx, "give_to_player");
        int heartsAmount = IntegerArgumentType.getInteger(ctx, "amount_hearts");

        UpdateMaxHealth.update(players,heartsAmount);

        return Command.SINGLE_SUCCESS;
    }
}
