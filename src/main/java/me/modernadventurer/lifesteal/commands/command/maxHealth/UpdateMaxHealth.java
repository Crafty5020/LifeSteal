package me.modernadventurer.lifesteal.commands.command.maxHealth;

import me.modernadventurer.lifesteal.Loader;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Collection;

public class UpdateMaxHealth {
    public static void update(Collection<ServerPlayerEntity> players, int heartsAmount) {
        for (ServerPlayerEntity player : players) {
            EntityAttributeInstance health = player.getAttributeInstance(EntityAttributes.MAX_HEALTH);
            assert health != null;
            double oldHealth = health.getValue();
            float newHealth = (float) (oldHealth + heartsAmount);
            int maxHealth = player.getEntityWorld().getGameRules().getValue(Loader.MAXPLAYERHEALTH);
            if(maxHealth > 0 && newHealth > maxHealth) newHealth = maxHealth;
            player.setHealth(player.getHealth()+heartsAmount);
            health.setBaseValue(newHealth);
        }
    }
}
