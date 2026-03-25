package me.modernadventurer.lifesteal.items.registry;

import me.modernadventurer.lifesteal.Loader;
import me.modernadventurer.lifesteal.items.Echo;
import me.modernadventurer.lifesteal.items.ElderGardianSkin;
import me.modernadventurer.lifesteal.items.WardenHorns;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import static me.modernadventurer.lifesteal.Loader.MOD_ID;

public class ItemRegistry {
    public static void register() {
        Loader.WARDENHORNS = Registry.register(Registries.ITEM,
                Identifier.of(MOD_ID, "warden_horns"),
                new WardenHorns(new Item.Settings().registryKey(RegistryKey.of(Registries.ITEM.getKey(), Identifier.of(MOD_ID, "warden_horns")))));

        Loader.ELDERGUARDIANSKIN = Registry.register(Registries.ITEM,
                Identifier.of(MOD_ID, "elder_guardian_skin"),
                new ElderGardianSkin(new Item.Settings().registryKey(RegistryKey.of(Registries.ITEM.getKey(), Identifier.of(MOD_ID, "elder_guardian_skin")))));

        Loader.ECHO = Registry.register(Registries.ITEM,
                Identifier.of(MOD_ID, "echo"),
                new Echo(new Item.Settings().registryKey(RegistryKey.of(Registries.ITEM.getKey(), Identifier.of(MOD_ID, "echo")))));
    }
}
