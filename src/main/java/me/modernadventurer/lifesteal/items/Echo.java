package me.modernadventurer.lifesteal.items;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.networking.api.server.PolymerServerNetworking;
import me.modernadventurer.lifesteal.Loader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.Objects;

import static net.fabricmc.fabric.impl.attachment.sync.c2s.AcceptedAttachmentsPayloadC2S.PACKET_ID;

public class Echo extends Item implements PolymerItem {
    public Echo(Settings settings) {
        super(settings);
    }

    // 1. This tells the client to DISPLAY a Sculk Catalyst
    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
        if (PolymerServerNetworking.getSupportedVersion(Objects.requireNonNull(context.getPlayer()).networkHandler, PACKET_ID) > 0) {
            return this; // Client supports mod → show custom item
        } else {
            return Items.PAPER;
        }
    }

    // 2. This tells the client to USE your custom 2D/3D model ID
    @Override
    public Identifier getPolymerItemModel(ItemStack itemStack, PacketContext context) {
        return Identifier.of(Loader.MOD_ID, "echo");
    }


}