package me.modernadventurer.lifesteal;

import me.modernadventurer.lifesteal.commands.CommandRegistry;
import me.modernadventurer.lifesteal.events.LifestealEvents;
import me.modernadventurer.lifesteal.items.Echo;
import me.modernadventurer.lifesteal.items.ElderGardianSkin;
import me.modernadventurer.lifesteal.items.WardenHorns;
import me.modernadventurer.lifesteal.items.registry.CreativeItemGroupsRegistry;
import me.modernadventurer.lifesteal.items.registry.ItemRegistry;
import me.modernadventurer.lifesteal.polymer.LifestealResourcePackBuilder;
import net.fabricmc.api.ModInitializer;


import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.rule.GameRule;
import net.minecraft.world.rule.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Copyright 2021 BradBot_1
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <a href="http://www.apache.org/licenses/LICENSE-2.0">...</a>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//NOTICE: This file was modified to remove all configuration setup and instead establish gamerules.

public class Loader implements ModInitializer {

    public static int timer = 0;
    public static boolean running = false;
    public static boolean paused = false;
    public static int tickCounter = 0;
    public static boolean pvpEnabled = false;

	public static final String MOD_ID = "lifesteal";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static ConcurrentHashMap<UUID, Long> BANNED_PLAYERS = new ConcurrentHashMap<>();

    public static WardenHorns WARDENHORNS;
    public static ElderGardianSkin ELDERGUARDIANSKIN;
    public static Echo ECHO;

    public static final GameRule<Integer> BEACONYOFFSET = GameRuleBuilder.forInteger(5)
            .buildAndRegister(Identifier.of(MOD_ID,"beacon_y_offset"));
    public static final GameRule<Integer> BEACONLEVEL = GameRuleBuilder.forInteger(1)
            .buildAndRegister(Identifier.of(MOD_ID,"beacon_level"));

	public static final GameRule<Boolean> PLAYERRELATEDONLY = GameRuleBuilder.forBoolean(false)
            .buildAndRegister(Identifier.of(MOD_ID, "player_kill_only"));

    public static final GameRule<Integer> BANTIME = GameRuleBuilder.forInteger(5400)
            .buildAndRegister(Identifier.of(MOD_ID, "ban_time"));

	public static final GameRule<Boolean> BANWHENMINHEALTH = GameRuleBuilder.forBoolean(false)
            .buildAndRegister(Identifier.of(MOD_ID, "ban_when_min_health"));

	public static final GameRule<Integer> STEALAMOUNT = GameRuleBuilder.forInteger(2)
            .buildAndRegister(Identifier.of(MOD_ID, "steal_amount"));

    public static final GameRule<Integer> HEALTHLOST = GameRuleBuilder.forInteger(4)
            .buildAndRegister(Identifier.of(MOD_ID, "dead_health_lost"));

	public static final GameRule<Integer> MINPLAYERHEALTH = GameRuleBuilder.forInteger(0)
            .buildAndRegister(Identifier.of(MOD_ID, "min_player_health"));

	public static final GameRule<Integer> MAXPLAYERHEALTH = GameRuleBuilder.forInteger(0)
            .buildAndRegister(Identifier.of(MOD_ID, "max_player_health"));

    public static final GameRule<Integer> PENDINGHEARTSTIMEOUT = GameRuleBuilder.forInteger(120)
            .buildAndRegister(Identifier.of(MOD_ID, "pending_hearts_timeout"));

    public static final GameRule<Integer> PVPTIMERTIME = GameRuleBuilder.forInteger(300)
            .buildAndRegister(Identifier.of(MOD_ID, "pvp_timer_time"));

	@Override
	public void onInitialize() {
        LOGGER.info("LifeSteal Initializing");
        LOGGER.info("Building resource pack!");
        LifestealResourcePackBuilder.Build();
        LOGGER.info("Success creating main resource pack");
        LOGGER.info("Registering Items");
        ItemRegistry.register();
        LOGGER.info("Success registering items!");
        LOGGER.info("Registering Creative Item Groups");
        CreativeItemGroupsRegistry.register();
        LOGGER.info("Success registering creative item groups!");
        LOGGER.info("Registering lifesteal commands!");
        CommandRegistry.register();
        LOGGER.info("Success registering commands!");
        LOGGER.info("Finished LifeSteal Init!");

        ServerTickEvents.END_SERVER_TICK.register(server -> {

            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {

                if (player.getEntityWorld().isClient()) continue;

                LifestealEvents.onEyesDetected(player);
            }
            if (!Loader.running || Loader.paused) return;
            Loader.tickCounter++;

            if (Loader.tickCounter >= 20) { // 1 second
                Loader.tickCounter = 0;
                Loader.timer--;

                for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {

                    if (Loader.timer > 0) {
                        Loader.sendActionBar(player,
                                "§ePvP enables in: " + formatTime(Loader.timer)
                        );
                    } else {
                        Loader.sendActionBar(player, "§aPvP Enabled!");
                        server.getSpawnWorld().getGameRules().setValue(GameRules.PVP, true, server);
                    }
                }

                if (Loader.timer <= 0) {
                    Loader.running = false;
                    Loader.pvpEnabled = true;
                }
            }

        });

    }



    public static void sendActionBar(ServerPlayerEntity player, String message) {
        player.networkHandler.sendPacket(
                new OverlayMessageS2CPacket(Text.literal(message))
        );
    }

    private static String formatTime(long totalSeconds) {
        long days = totalSeconds / 86400;
        long hours = (totalSeconds % 86400) / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        if (days > 0) {
            return String.format("%dd %02dh %02dm %02ds", days, hours, minutes, seconds);
        } else if (hours > 0) {
            return String.format("%02dh %02dm %02ds", hours, minutes, seconds);
        } else {
            return String.format("%02dm %02ds", minutes, seconds);
        }
    }

    private static boolean isEye(ItemStack stack) {
        return stack.getItem() == Items.ENDER_EYE;
    }
}
