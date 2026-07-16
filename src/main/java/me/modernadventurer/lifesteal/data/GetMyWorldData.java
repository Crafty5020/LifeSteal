package me.modernadventurer.lifesteal.data;

import me.modernadventurer.lifesteal.Loader;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentStateType;

public class GetMyWorldData {
    public static MyWorldData get(ServerWorld world) {
        // Defines how to create and load your data
        PersistentStateType<MyWorldData> type = new PersistentStateType<>(
                Loader.MOD_ID,
                MyWorldData::new,          // Constructor for new data
                MyWorldData.CODEC,      // Method to load from NBT
                null                       // DataFixerType (optional)
        );

        // Loads "my_portal_pos.dat" from the world's /data folder
        return world.getPersistentStateManager().getOrCreate(type);
    }
}
