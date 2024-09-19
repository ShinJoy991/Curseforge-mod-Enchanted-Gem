package com.github.shinjoy991.enchantedgem.event;

import com.github.shinjoy991.enchantedgem.Config;
import com.github.shinjoy991.enchantedgem.register.ModItems;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;

import static com.github.shinjoy991.enchantedgem.EnchantedGem.LOGGER;
import static com.github.shinjoy991.enchantedgem.EnchantedGem.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class LootTableInjection {
    private static final Set<ResourceLocation> LOOT_TABLES = Sets.newHashSet();
    private static final Map<String, ResourceLocation> INJECTION_TABLES = Maps.newHashMap();
    public static final ResourceLocation bastion_treasure = registerInject("bastion_treasure");
    public static final ResourceLocation jungle_temple = registerInject("jungle_temple");
    public static final ResourceLocation stronghold_crossing = registerInject("stronghold_crossing");
    public static final ResourceLocation nether_bridge = registerInject("nether_bridge");
    public static final ResourceLocation pillager_outpost = registerInject("pillager_outpost");
    public static final ResourceLocation woodland_mansion = registerInject("woodland_mansion");
    public static final ResourceLocation deepslate_emerald_ore = registerInjectBlock(
            "deepslate_emerald_ore");
    public static final ResourceLocation emerald_ore = registerInjectBlock(
            "emerald_ore");
    public static final ResourceLocation blaze = registerInjectEntities(
            "blaze");
    public static final ResourceLocation creeper = registerInjectEntities(
            "creeper");
    public static final ResourceLocation drowned = registerInjectEntities(
            "drowned");
    public static final ResourceLocation elder_guardian = registerInjectEntities(
            "elder_guardian");
    public static final ResourceLocation ender_dragon = registerInjectEntities(
            "ender_dragon");
    public static final ResourceLocation evoker = registerInjectEntities(
            "evoker");
    public static final ResourceLocation ghast = registerInjectEntities(
            "ghast");
    public static final ResourceLocation guardian = registerInjectEntities(
            "guardian");
    public static final ResourceLocation hoglin = registerInjectEntities(
            "hoglin");
    public static final ResourceLocation husk = registerInjectEntities(
            "husk");
    public static final ResourceLocation illusioner = registerInjectEntities(
            "illusioner");
    public static final ResourceLocation phantom = registerInjectEntities(
            "phantom");
    public static final ResourceLocation piglin_brute = registerInjectEntities(
            "piglin_brute");
    public static final ResourceLocation pillager = registerInjectEntities(
            "pillager");
    public static final ResourceLocation ravager = registerInjectEntities(
            "ravager");
    public static final ResourceLocation shulker = registerInjectEntities(
            "shulker");
    public static final ResourceLocation skeleton = registerInjectEntities(
            "skeleton");
    public static final ResourceLocation stray = registerInjectEntities(
            "stray");
    public static final ResourceLocation vindicator = registerInjectEntities(
            "vindicator");
    public static final ResourceLocation witch = registerInjectEntities(
            "witch");
    public static final ResourceLocation wither = registerInjectEntities(
            "wither");
    public static final ResourceLocation wither_skeleton = registerInjectEntities(
            "wither_skeleton");
    public static final ResourceLocation zoglin = registerInjectEntities(
            "zoglin");
    public static final ResourceLocation zombie = registerInjectEntities(
            "zombie");
    public static final ResourceLocation zombie_villager = registerInjectEntities(
            "zombie_villager");
    public static final ResourceLocation zombified_piglin = registerInjectEntities(
            "zombified_piglin");

    static ResourceLocation registerInject(String resourceName) {
        ResourceLocation registryName = register("inject/" + resourceName);
        INJECTION_TABLES.put(resourceName, registryName);
        return registryName;
    }

    static ResourceLocation registerInjectBlock(String resourceName) {
        ResourceLocation registryName = register("inject_block/" + resourceName);
        INJECTION_TABLES.put(resourceName, registryName);
        return registryName;
    }

    static ResourceLocation registerInjectEntities(String resourceName) {
        ResourceLocation registryName = register("inject_entities/" + resourceName);
        INJECTION_TABLES.put(resourceName, registryName);
        return registryName;
    }

    static ResourceLocation register(String resourceName) {
        return register(new ResourceLocation("enchantedgem", resourceName));
    }

    static ResourceLocation register(@Nonnull ResourceLocation resourceLocation) {
        LOOT_TABLES.add(resourceLocation);
        return resourceLocation;
    }

    @SubscribeEvent
    public static void onLootLoad(LootTableLoadEvent event) {
        String prefix = "minecraft:chests/";
        String prefixBlock = "minecraft:blocks/";
        String prefixEntities = "minecraft:entities/";
        String name = event.getName().toString();
        if (name.startsWith(prefix)) {
            String file = name.substring(name.indexOf(prefix) + prefix.length());
            if (INJECTION_TABLES.containsKey(file)) {
                try {
                    event.getTable().addPool(getInjectPoolGemChest());
                } catch (NullPointerException var5) {
                    LOGGER.error("Loot table is broken, {}", name);
                }
            }
        } else
            if (name.startsWith(prefixBlock)) {
                String file = name.substring(name.indexOf(prefixBlock) + prefixBlock.length());
                if (INJECTION_TABLES.containsKey(file)) {
                    try {
                        event.getTable().addPool(getInjectPoolGemBlock());
                    } catch (NullPointerException var5) {
                        LOGGER.error("Loot block table is broken, {}", name);
                    }
                }
            } else
                if (name.startsWith(prefixEntities)) {
                    String file = name.substring(name.indexOf(prefixEntities) + prefixEntities.length());
                    if (INJECTION_TABLES.containsKey(file)) {
                        try {
                            event.getTable().addPool(getInjectPoolSpirit());
                        } catch (NullPointerException var5) {
                            LOGGER.error("Loot of entity is broken, {}", name);
                        }
                    }
                }
    }


    private static LootPool getInjectPoolGemChest() {
        return LootPool.lootPool()
                .name("enchantedgem_inject_pool")
                .setRolls(UniformGenerator.between(0.0F, Config.CHEST_CHANCE))
                .setBonusRolls(UniformGenerator.between(0.0F, 1.0F))
                .add(createEntry(ModItems.ENCHANTED_GEM_ATK1.get(), Config.GEM_ATK_CHANCE))
                .add(createEntry(ModItems.ENCHANTED_GEM_DEF1.get(), Config.GEM_DEF_CHANCE))
                .add(createEntry(ModItems.ENCHANTED_GEM_HP1.get(), Config.GEM_HP_CHANCE))
                .add(createEntry(ModItems.ENCHANTED_GEM_SPD1.get(), Config.GEM_SPD_CHANCE))
                .add(createEntry(ModItems.ENCHANTED_GEM_CRITDMG1.get(), Config.GEM_CRITDMG_CHANCE))
                .build();
    }

    private static LootPool getInjectPoolGemBlock() {
        return LootPool.lootPool()
                .name("enchantedgem_inject_pool")
                .setRolls(ConstantValue.exactly(1.0F))
                .setBonusRolls(UniformGenerator.between(0.0F, 0.0F))
                .add(createEntry(ModItems.ENCHANTED_GEM_ATK1.get(), Config.GEM_BLOCK_CHANCE))
                .add(createEntry(ModItems.ENCHANTED_GEM_DEF1.get(), Config.GEM_BLOCK_CHANCE))
                .add(createEntry(ModItems.ENCHANTED_GEM_HP1.get(), Config.GEM_BLOCK_CHANCE))
                .add(createEntry(ModItems.ENCHANTED_GEM_SPD1.get(), Config.GEM_BLOCK_CHANCE))
                .add(createEntry(ModItems.ENCHANTED_GEM_CRITDMG1.get(), Config.GEM_BLOCK_CHANCE))
                .build();
    }

    private static LootPool getInjectPoolSpirit() {
        return LootPool.lootPool()
                .name("enchantedgem_inject_pool")
                .setRolls(ConstantValue.exactly(1.0F))
                .setBonusRolls(UniformGenerator.between(0.0F, 1.0F))
                .add(createEntry(ModItems.ENCHANTED_ELEMENTAL_SPIRIT_FIRE.get(), Config.SPIRIT_FIRE_CHANCE))
                .add(createEntry(ModItems.ENCHANTED_ELEMENTAL_SPIRIT_WATER.get(), Config.SPIRIT_WATER_CHANCE))
                .add(createEntry(ModItems.ENCHANTED_ELEMENTAL_SPIRIT_WOOD.get(), Config.SPIRIT_WOOD_CHANCE))
                .add(createEntry(ModItems.ENCHANTED_ELEMENTAL_SPIRIT_LIGHT.get(), Config.SPIRIT_LIGHT_CHANCE))
                .add(createEntry(ModItems.ENCHANTED_ELEMENTAL_SPIRIT_DARK.get(), Config.SPIRIT_DARK_CHANCE))
                .build();
    }

    private static LootPoolEntryContainer.Builder<?> createEntry(Item item, float chance) {
        return LootItem.lootTableItem(item)
                .when(LootItemRandomChanceCondition.randomChance(chance));
    }
}