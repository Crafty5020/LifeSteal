package me.modernadventurer.lifesteal.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import me.modernadventurer.lifesteal.commands.command.AddHearts;
import me.modernadventurer.lifesteal.commands.command.GIveHearts.GiveHearts;
import me.modernadventurer.lifesteal.commands.command.RemoveHearts;
import me.modernadventurer.lifesteal.commands.command.pvpMode.pvp;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.permission.Permission;
import me.modernadventurer.lifesteal.commands.command.GiveAllItems;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.permission.PermissionLevel;
import net.minecraft.server.command.CommandManager;



public class CommandRegistry {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(CommandManager.literal("lifesteal")
                .then(CommandManager.literal("give_all_items")
                        .requires(source -> source.getPermissions().hasPermission(new Permission.Level(PermissionLevel.GAMEMASTERS)))
                        .then(CommandManager.argument("give_to_player", EntityArgumentType.players())
                            .executes(GiveAllItems::giveItems)
                        )
                )
                .then(CommandManager.literal("add_hearts")
                        .requires(source -> source.getPermissions().hasPermission(new Permission.Level(PermissionLevel.GAMEMASTERS)))
                        .then(CommandManager.argument("heart_amount", EntityArgumentType.players())
                                .then(CommandManager.argument("give_to_player", EntityArgumentType.players())

                                    .executes(AddHearts::Add)
                            )
                        )
                )
                .then(CommandManager.literal("remove_hearts")
                        .requires(source -> source.getPermissions().hasPermission(new Permission.Level(PermissionLevel.GAMEMASTERS)))
                        .then(CommandManager.argument("give_to_player", EntityArgumentType.players())
                                .then(CommandManager.argument("heart_amount", EntityArgumentType.players())

                                    .executes(RemoveHearts::remove)
                                )
                        )
                )
                .then(CommandManager.literal("give_hearts")
                        .then(CommandManager.argument("give_to_player", EntityArgumentType.players())
                                .then(CommandManager.argument("amount_hearts", IntegerArgumentType.integer(0))
                                        .executes(GiveHearts::give)
                                )
                        )
                        .then(CommandManager.literal("accept")
                                .executes(GiveHearts::accept)
                        )
                        .then(CommandManager.literal("cancel")
                                .executes(GiveHearts::cancel)
                        )
                )
                .then(CommandManager.literal("pvp")
                        .requires(source -> source.getPermissions().hasPermission(new Permission.Level(PermissionLevel.GAMEMASTERS)))
                        .then(CommandManager.literal("on")
                                .executes(pvp::pvpOn)
                        )
                        .then(CommandManager.literal("off")
                                .executes(pvp::pvpOff)
                        )
                        .then(CommandManager.literal("timer")
                                .then(CommandManager.literal("on")
                                        .executes(pvp::pvpTimerOn)
                                )
                                .then(CommandManager.literal("off")
                                        .executes(pvp::pvpTimerOff)
                                )
                                .then(CommandManager.literal("pause")
                                        .executes(pvp::pvpTimerPause)
                                )
                        )
                )
        ));
    }
}
