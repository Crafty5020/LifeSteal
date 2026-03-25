package me.modernadventurer.lifesteal.commands.command.pvpMode;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import me.modernadventurer.lifesteal.Loader;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.rule.GameRules;
import org.apache.logging.log4j.core.jmx.Server;

public class pvp {
    public static int pvpOn(CommandContext<ServerCommandSource> ctx) {
        Loader.pvpEnabled = true;
        ctx.getSource().getWorld().getGameRules().setValue(GameRules.PVP, true, ctx.getSource().getServer());
        ctx.getSource().getServer().getPlayerManager().broadcast(
                Text.literal("§aPvP Enabled"), false
        );

        return Command.SINGLE_SUCCESS;
    }

    public static int pvpOff(CommandContext<ServerCommandSource> ctx) {
        Loader.pvpEnabled = false;

        ctx.getSource().getServer().getPlayerManager().broadcast(
                Text.literal("§cPvP Disabled"), false
        );

        return Command.SINGLE_SUCCESS;
    }
    public static int pvpTimerOn(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource source = ctx.getSource();
        ServerWorld world = source.getWorld();
        Loader.timer = world.getGameRules().getValue(Loader.PVPTIMERTIME); // change later to argument
        Loader.running = true;
        Loader.paused = false;

        long days = Loader.timer / 86400;            // 86,400 seconds in a day
        long hours = (Loader.timer % 86400) / 3600;  // Leftover from days, divided by 3,600
        long minutes = (Loader.timer % 3600) / 60;   // Leftover from hours, divided by 60
        long seconds = Loader.timer % 60;            // Leftover from minutes (what's left!)
        ctx.getSource().getServer().getPlayerManager().broadcast(
                Text.literal("§ePvP Timer Started: " + days  + "d, " + hours + "h, " + minutes + "m, " + seconds + "s."), false
        );

        return Command.SINGLE_SUCCESS;
    }
    public static int pvpTimerOff(CommandContext<ServerCommandSource> ctx) {
        Loader.running = false;
        Loader.timer = 0;
        ctx.getSource().getWorld().getGameRules().setValue(GameRules.PVP, false, ctx.getSource().getServer());

        ctx.getSource().getServer().getPlayerManager().broadcast(
                Text.literal("§cPvP Timer Stopped"), false
        );

        return Command.SINGLE_SUCCESS;
    }
    public static int pvpTimerPause(CommandContext<ServerCommandSource> ctx) {
        Loader.paused = !Loader.paused;

        ctx.getSource().getServer().getPlayerManager().broadcast(
                Text.literal(Loader.paused ? "§6Timer Paused" : "§aTimer Resumed"), false
        );

        return Command.SINGLE_SUCCESS;
    }

}
