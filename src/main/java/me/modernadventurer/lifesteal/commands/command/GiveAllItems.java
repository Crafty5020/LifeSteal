package me.modernadventurer.lifesteal.commands.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import me.modernadventurer.lifesteal.Loader;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class GiveAllItems{
    public static int giveItems(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource source = ctx.getSource();
        ServerPlayerEntity player = source.getPlayer();

        // Give items in Lifesteal group
        assert player != null;
        player.giveItemStack(new ItemStack(Loader.ECHO));
        player.giveItemStack(new ItemStack(Loader.WARDENHORNS));
        player.giveItemStack(new ItemStack(Loader.ELDERGUARDIANSKIN));

        // Optional: notify you
        source.sendFeedback(() -> Text.literal("All items given!"), false);

        return Command.SINGLE_SUCCESS;
    }
}
