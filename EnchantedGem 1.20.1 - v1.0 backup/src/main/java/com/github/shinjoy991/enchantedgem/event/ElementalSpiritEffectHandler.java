package com.github.shinjoy991.enchantedgem.event;

import com.github.shinjoy991.enchantedgem.EnchantedGem;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static com.github.shinjoy991.enchantedgem.helpers.DelayFunc.delayedTask;

@Mod.EventBusSubscriber(modid = EnchantedGem.MODID)
public class ElementalSpiritEffectHandler {

    private static Map<UUID, Integer> saturationCooldown = new HashMap<>();

    @SubscribeEvent
    public static void onSpiritRightClick(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        ItemStack itemStack = player.getMainHandItem();

        CompoundTag tag = itemStack.getTag();
        if (tag == null || !tag.contains("ENCHANTED_ELEMENTAL_SPIRIT"))
            return;

        String spiritType = tag.getString("ENCHANTED_ELEMENTAL_SPIRIT");

        if (!"FIRE_SPIRIT".equals(spiritType))
            return;

        BlockPos pos = event.getPos();
        BlockState blockState = player.level().getBlockState(pos);
        try {
            if (!blockState.getValue(BlockStateProperties.LIT)) {
                // Set the block to be lit
                player.level().setBlock(pos, blockState.setValue(BlockStateProperties.LIT, Boolean.TRUE), 11);
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
                return;
            }
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    @SubscribeEvent
    public static void onSpiritUndeadHurt(LivingHurtEvent event) {
        try {
            if (!(event.getSource().getEntity() instanceof LivingEntity livingAttacker && livingAttacker.getMobType() == MobType.UNDEAD))
                return;
            if (event.getEntity() instanceof ServerPlayer player) {
                CompoundTag tag = player.getMainHandItem().getTag();
                if (tag == null || !tag.contains("ENCHANTED_ELEMENTAL_SPIRIT"))
                    return;
                String spiritType = tag.getString("ENCHANTED_ELEMENTAL_SPIRIT");
                if (spiritType.equals("LIGHT_SPIRIT")) {
                    event.setAmount(event.getAmount() * 0.9F);
                }
            }
        } catch (Exception ignored) {
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        // Check if the player is in water
        if (player.isInWater()) {
            ItemStack itemStack = player.getMainHandItem();
            CompoundTag tag = itemStack.getTag();

            // Check if the item has the ENCHANTED_ELEMENTAL_SPIRIT tag
            if (tag != null && tag.contains("ENCHANTED_ELEMENTAL_SPIRIT")) {
                String spiritType = tag.getString("ENCHANTED_ELEMENTAL_SPIRIT");

                // Check if the spirit type is WATER_SPIRIT
                if ("WATER_SPIRIT".equals(spiritType)) {
                    // Apply speed effect
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, 0, false, false, true));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        if (event.getEntity() instanceof Player player) {
            ItemStack itemStack = player.getMainHandItem();
            CompoundTag tag = itemStack.getTag();

            if (tag != null && tag.contains("ENCHANTED_ELEMENTAL_SPIRIT")) {
                String spiritType = tag.getString("ENCHANTED_ELEMENTAL_SPIRIT");

                if ("WATER_SPIRIT".equals(spiritType)) {
                    // Check if fall distance is 10 blocks or more
                    if (event.getDistance() >= 10.0F) {
                        BlockPos pos = player.blockPosition();
                        BlockState waterState = Blocks.WATER.defaultBlockState();

                        BlockState blockStateBelow = player.level().getBlockState(pos);

                        if (blockStateBelow.isAir()) {
                            player.level().setBlock(pos, waterState, 3);
                            event.setCanceled(true);
                            delayedTask(40, () -> {
                                if (player.level().getBlockState(pos).getBlock() == Blocks.WATER) {
                                    player.level().setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTickWoodSpirit(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        ItemStack itemStack = player.getMainHandItem();
        CompoundTag tag = itemStack.getTag();

        if (tag != null && tag.contains("ENCHANTED_ELEMENTAL_SPIRIT")) {
            String spiritType = tag.getString("ENCHANTED_ELEMENTAL_SPIRIT");

            if ("WOOD_SPIRIT".equals(spiritType)) {
                ResourceKey<Biome> biomeKey = player.level().getBiome(player.blockPosition()).unwrapKey().orElse(null);
                if (biomeKey != null && (biomeKey.equals(Biomes.FOREST) || biomeKey.equals(Biomes.BIRCH_FOREST) || biomeKey.equals(Biomes.DARK_FOREST) || biomeKey.equals(Biomes.TAIGA) || biomeKey.equals(Biomes.OLD_GROWTH_SPRUCE_TAIGA) || biomeKey.equals(Biomes.OLD_GROWTH_PINE_TAIGA))) {
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20, 0, false, false, true));
                }

                if (player.isInWater()) {
                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20, 0, false, false, true));
                }
            }
        }
    }


    @SubscribeEvent
    public static void onPlayerTickLightSpirit(TickEvent.PlayerTickEvent event) {
        if (!(event.player instanceof ServerPlayer))
            return;
        Player player = event.player;
        ItemStack itemStack = player.getMainHandItem();
        CompoundTag tag = itemStack.getTag();

        if (tag != null && tag.contains("ENCHANTED_ELEMENTAL_SPIRIT")) {
            String spiritType = tag.getString("ENCHANTED_ELEMENTAL_SPIRIT");

            if (spiritType.equals("LIGHT_SPIRIT")) {
                saturationCooldown.putIfAbsent(player.getUUID(), 0);
                if (saturationCooldown.getOrDefault(player.getUUID(), 0) >= 1200) {
                    saturationCooldown.put(player.getUUID(), 0);
                    player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 1, 0, false, false, false));
                } else {
                    saturationCooldown.put(player.getUUID(), saturationCooldown.getOrDefault(player.getUUID(), 0) + 1);
                }
            } else
                if (spiritType.equals("DARK_SPIRIT")) {
                    player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 220, 0, false, false, true));

                    if (player.hasEffect(MobEffects.DARKNESS)) {
                        player.removeEffect(MobEffects.DARKNESS);
                    }
                }
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                long gameTime = server.getNextTickTime();

                if (gameTime % 600 == 0) {
                    for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                        ItemStack itemStack = player.getMainHandItem();
                        CompoundTag tag = itemStack.getTag();

                        if (tag != null && "LIGHT_SPIRIT".equals(tag.getString("ENCHANTED_ELEMENTAL_SPIRIT"))) {
                            player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 1, 0, false, false, true));
                        }
                    }
                }
            }
        }
    }


    @SubscribeEvent
    public static void onPlayerChangeEquipSpirit(LivingHurtEvent event) {
        if (!(event.getSource().getEntity() instanceof LivingEntity player)) {
            return;
        }
        CompoundTag tag = player.getMainHandItem().getTag();
        if (tag == null || !tag.contains("ENCHANTED_ELEMENTAL_SPIRIT"))
            return;
        try {
            String spiritType = tag.getString("ENCHANTED_ELEMENTAL_SPIRIT");
            switch (spiritType) {
                case "FIRE_SPIRIT" -> {
                    boolean isInNether = player.level().dimension() == Level.NETHER;
                    ResourceKey<Biome> biomeKey = player.level().getBiome(player.blockPosition()).unwrapKey().orElse(null);
                    boolean isInDesert = biomeKey != null && biomeKey.equals(Biomes.DESERT);
                    if (isInNether || isInDesert) {
                        event.setAmount(event.getAmount() * 1.1F);
                    }
                    boolean isInWater = player.isInWater();
                    boolean isRaining = player.level().isRainingAt(player.blockPosition());

                    if (isInWater || isRaining) {
                        event.setAmount(event.getAmount() * 0.9F);
                    }
                    event.getEntity().setSecondsOnFire(6);
                    int random = new Random().nextInt(100);
                    if (random < 4) {
                        event.getSource().getEntity().setSecondsOnFire(6);
                    }
                }
                case "WATER_SPIRIT" -> {
                    boolean isInNether = player.level().dimension() == Level.NETHER;
                    ResourceKey<Biome> biomeKey = player.level().getBiome(player.blockPosition()).unwrapKey().orElse(null);
                    boolean isInDesert = biomeKey != null && biomeKey.equals(Biomes.DESERT);

                    if (isInNether || isInDesert) {
                        event.setAmount(event.getAmount() * 0.9F);
                    }

                    boolean isInWater = player.isInWater();
                    boolean isRaining = player.level().isRainingAt(player.blockPosition());

                    if (isInWater || isRaining) {
                        event.setAmount(event.getAmount() * 1.1F);
                    }
                }

                case "WOOD_SPIRIT" -> {
                    ResourceKey<Biome> biomeKey = player.level().getBiome(player.blockPosition()).unwrapKey().orElse(null);
                    boolean isInForest = biomeKey != null && (
                            biomeKey.equals(Biomes.FOREST) ||
                                    biomeKey.equals(Biomes.BIRCH_FOREST) ||
                                    biomeKey.equals(Biomes.DARK_FOREST) ||
                                    biomeKey.equals(Biomes.TAIGA)
                    );
                    boolean isInPlains = biomeKey != null && biomeKey.equals(Biomes.PLAINS);
                    if (isInForest || isInPlains) {
                        event.setAmount(event.getAmount() * 1.1F);
                    }
                }
                case "LIGHT_SPIRIT" -> {
                    boolean isDayTime = player.level().isDay();
                    boolean isInEnd = player.level().dimension() == Level.END;
                    boolean isInDark = player.level().getMaxLocalRawBrightness(player.blockPosition()) < 7;
                    if (isDayTime || isInEnd) {
                        event.setAmount(event.getAmount() * 1.1F);
                    }

                    if (isInDark) {
                        event.setAmount(event.getAmount() * 0.9F);
                    }

                }
                case "DARK_SPIRIT" -> {
                    boolean isDayTime = player.level().isDay();
                    boolean isInDark = player.level().getMaxLocalRawBrightness(player.blockPosition()) < 7;
                    if (isInDark) {
                        event.setAmount(event.getAmount() * 1.1F);
                    }

                    if (isDayTime) {
                        event.setAmount(event.getAmount() * 0.9F);
                    } else {
                        event.setAmount(event.getAmount() * 1.1F);
                    }
                }
            }

        } catch (Exception ignored) {
        }

    }

    private static int getGemBonusFromItem(ItemStack itemStack, String gemType) {
        if (!itemStack.hasTag()) {
            return 0;
        }
        CompoundTag tag = itemStack.getTag();
        if (tag == null || !tag.contains("GEM_INLAY_COUNT")) {
            return 0;
        }

        int gemCount = tag.getInt("GEM_INLAY_COUNT");
        int bonus = 0;
        for (int i = 1; i <= gemCount; i++) {
            if (gemType.equals(tag.getString("GEM_INLAY_" + i))) {
                bonus++;
            }
        }
        return bonus;
    }
}