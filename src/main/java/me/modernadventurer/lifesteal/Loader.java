package me.modernadventurer.lifesteal;

import me.modernadventurer.lifesteal.commands.CommandRegistry;
import me.modernadventurer.lifesteal.items.Echo;
import me.modernadventurer.lifesteal.items.ElderGardianSkin;
import me.modernadventurer.lifesteal.items.WardenHorns;
import me.modernadventurer.lifesteal.items.registry.CreativeItemGroupsRegistry;
import me.modernadventurer.lifesteal.items.registry.ItemRegistry;
import me.modernadventurer.lifesteal.polymer.LifestealResourcePackBuilder;
import net.fabricmc.api.ModInitializer;


import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.world.rule.GameRule;
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

	public static final String MOD_ID = "lifesteal";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static ConcurrentHashMap<UUID, Long> BANNED_PLAYERS;

    public static WardenHorns WARDENHORNS;
    public static ElderGardianSkin ELDERGUARDIANSKIN;
    public static Echo ECHO;


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
    }
}
