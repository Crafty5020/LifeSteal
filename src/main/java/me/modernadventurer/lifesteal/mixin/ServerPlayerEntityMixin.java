package me.modernadventurer.lifesteal.mixin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.modernadventurer.lifesteal.Loader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.Set;

//NOTICE: file was modified to use gamerules instead
//of the configuration implementation and to use attributes, it also fixes the max health attribute
//being lost on death.

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {

	@Inject(method = "copyFrom", at = @At("TAIL"))
	public void preserveMaxHealth(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo callbackInfo) {
		EntityAttributeInstance oldHealth = oldPlayer.getAttributes().getCustomInstance(EntityAttributes.MAX_HEALTH);
		assert oldHealth != null;
		EntityAttributeInstance health = ((ServerPlayerEntity) (Object) this).getAttributes().getCustomInstance(EntityAttributes.MAX_HEALTH);
		assert health != null;
		health.setBaseValue(oldHealth.getBaseValue());
	}

	@Inject(method = "onDeath", at = @At("TAIL"))
	public void onDeathLowerMaxHealth(DamageSource source, CallbackInfo callbackInfo) {
		ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
		ServerWorld world = player.getEntityWorld();
		Entity entity = source.getAttacker();
		int stealAmount = world.getGameRules().getValue(Loader.STEALAMOUNT);
        int heartLostAmount = world.getGameRules().getValue(Loader.HEALTHLOST);
		if(entity instanceof ServerPlayerEntity) {
			updateValueOf((ServerPlayerEntity)entity, stealAmount);
            if (heartLostAmount != stealAmount) heartLostAmount= -heartLostAmount; else heartLostAmount = -stealAmount;
			updateValueOf(player, heartLostAmount);
		} else if(!world.getGameRules().getValue(Loader.PLAYERRELATEDONLY)) {
			updateValueOf(player, -stealAmount);
		}
        EntityAttributeInstance health = player.getAttributeInstance(EntityAttributes.MAX_HEALTH);
        if (world.getGameRules().getValue(Loader.BANWHENMINHEALTH)) {
            int minHealth = world.getGameRules().getValue(Loader.MINPLAYERHEALTH);
            assert health != null;
            if (health.getBaseValue() <= minHealth) {
                // Drop all items
                player.getInventory().getMainStacks().stream().filter(stack -> !stack.isEmpty()).forEach(stack ->
                        world.spawnEntity(new ItemEntity(world, player.getX(), player.getY(), player.getZ(), stack))
                );
                player.getInventory().clear();

                // Timed ban
                long banDurationMs = world.getGameRules().getValue(Loader.BANTIME) * 1000L; // ticks -> ms
                long endTime = System.currentTimeMillis() + banDurationMs;
                Loader.BANNED_PLAYERS.put(player.getUuid(), endTime);

                // Kick
                player.networkHandler.disconnect(Text.literal("You lost your last life! Banned for " + (banDurationMs/1000) + " seconds."));
            }
        }
	}

	@Inject(method = "onSpawn", at = @At("TAIL"))
	public void onSpawnCheckToBan(CallbackInfo callbackInfo) {
		ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
		ServerWorld world = player.getEntityWorld();
		int minHealth = player.getEntityWorld().getGameRules().getValue(Loader.MINPLAYERHEALTH);
		if(minHealth < 1) minHealth = 1;
		EntityAttributeInstance health = player.getAttributes().getCustomInstance(EntityAttributes.MAX_HEALTH);
		assert health != null;
		if (world.getGameRules().getValue(Loader.BANWHENMINHEALTH) && health.getBaseValue() < minHealth) {
			checkTimedBan(player);
		}
	}
	
	@Unique
    private static void updateValueOf(ServerPlayerEntity of, int by) {
		EntityAttributeInstance health = of.getAttributeInstance(EntityAttributes.MAX_HEALTH);
		assert health != null;
		double oldHealth = health.getValue();
		float newHealth = (float) (oldHealth + by);
		int maxHealth = of.getEntityWorld().getGameRules().getValue(Loader.MAXPLAYERHEALTH);
		if(maxHealth > 0 && newHealth > maxHealth) newHealth = maxHealth;
		of.setHealth(of.getHealth()+by);
		health.setBaseValue(newHealth);
	}
    @Unique
    private void checkTimedBan(ServerPlayerEntity player) {
        if (Loader.BANNED_PLAYERS.containsKey(player.getUuid())) {
            long endTime = Loader.BANNED_PLAYERS.get(player.getUuid());
            long now = System.currentTimeMillis();
            if (now < endTime) {
                long remainingSec = (endTime - now)/1000;
                player.networkHandler.disconnect(Text.literal("You are banned for another " + remainingSec + " seconds."));
            } else {
                Loader.BANNED_PLAYERS.remove(player.getUuid());
                // Teleport to spawn
                ServerWorld overworld = Objects.requireNonNull(player.getEntityWorld().getServer()).getOverworld();
                WorldProperties.SpawnPoint spawnpoint = overworld.getSpawnPoint();
                BlockPos spawnPos = spawnpoint.getPos();

                Set<PositionFlag> positionFlags = Set.of(PositionFlag.X, PositionFlag.Y, PositionFlag.Z);

                player.teleport(overworld, spawnPos.getX() + 0.5, (double) spawnPos.getY(), spawnPos.getZ() + 0.5, positionFlags,player.getYaw(), player.getPitch(), false);
            }
        }
    }


}
