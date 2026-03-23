package me.modernadventurer.lifesteal.items.registry;

import eu.pb4.polymer.core.api.item.PolymerItemGroupUtils;
import me.modernadventurer.lifesteal.Loader;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static me.modernadventurer.lifesteal.Loader.*;

public class CreativeItemGroupsRegistry {
    public static void register() {
        ItemGroup lifestealGroup = PolymerItemGroupUtils.builder()
                .icon(() -> new ItemStack(ECHO)) // icon for the tab
                .displayName(Text.literal("Lifesteal")) // tab name
                .entries((context, entries) -> {
                    entries.add(ECHO);
                    entries.add(WARDENHORNS);
                    entries.add(ELDERGUARDIANSKIN);
                })
                .build();

        PolymerItemGroupUtils.registerPolymerItemGroup(
                Identifier.of(Loader.MOD_ID, "lifesteal_group"),
                lifestealGroup
        );
    }
}
