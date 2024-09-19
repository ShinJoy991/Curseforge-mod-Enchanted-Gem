package com.github.shinjoy991.enchantedgem;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = EnchantedGem.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final Map<String, ForgeConfigSpec.IntValue> configValues = new HashMap<>();
    private static final Map<String, ForgeConfigSpec.DoubleValue> configValuesDouble = new HashMap<>();

    static {
        configValues.put("CHEST_CHANCE", BUILDER.comment("Chance in % to generate in available chests")
                .defineInRange("Chest_Chance", 20, 0, 100));
        configValues.put("GEM_ATK_CHANCE", BUILDER.comment("Chance in % of gem type if it is generated in chests")
                .defineInRange("Gem_Atk_Chance", 10, 0, 100));
        configValues.put("GEM_DEF_CHANCE", BUILDER.defineInRange("Gem_Def_Chance", 10, 0, 100));
        configValues.put("GEM_HP_CHANCE", BUILDER.defineInRange("Gem_HP_Chance", 10, 0, 100));
        configValues.put("GEM_SPD_CHANCE", BUILDER.defineInRange("Gem_SPD_Chance", 10, 0, 100));
        configValues.put("GEM_CRITDMG_CHANCE", BUILDER.defineInRange("Gem_CritDmg_Chance", 10, 0, 100));
        configValues.put("GEM_BLOCK_CHANCE", BUILDER.defineInRange("Gem_Block_Chance", 10, 0, 100));
        configValues.put("RUNE_CHANCE", BUILDER.defineInRange("Rune_Chance", 10, 0, 100));
        configValues.put("SPIRIT_FIRE_CHANCE", BUILDER.defineInRange("Spirit_Fire_Chance", 1, 0, 100));
        configValues.put("SPIRIT_WATER_CHANCE", BUILDER.defineInRange("Spirit_Water_Chance", 1, 0, 100));
        configValues.put("SPIRIT_WOOD_CHANCE", BUILDER.defineInRange("Spirit_Wood_Chance", 1, 0, 100));
        configValues.put("SPIRIT_LIGHT_CHANCE", BUILDER.defineInRange("Spirit_Light_Chance", 1, 0, 100));
        configValues.put("SPIRIT_DARK_CHANCE", BUILDER.defineInRange("Spirit_Dark_Chance", 1, 0, 100));
        configValuesDouble.put("GEM_ATK_POWER", BUILDER.comment("Below is the power of each gem type")
                .defineInRange("Gem_Atk_Power", 0.3, 0, Double.MAX_VALUE));
        configValuesDouble.put("GEM_DEF_POWER", BUILDER.defineInRange("Gem_Def_Power", 0.3, 0, Double.MAX_VALUE));
        configValuesDouble.put("GEM_HP_POWER", BUILDER.defineInRange("Gem_Hp_Power", 0.3, 0, Double.MAX_VALUE));
        configValuesDouble.put("GEM_SPD_POWER", BUILDER.defineInRange("Gem_Spd_Power", 0.005, 0, Double.MAX_VALUE));
        configValuesDouble.put("GEM_CRITDMG_POWER", BUILDER.defineInRange("Gem_CritDmg_Power", 7, 0, Double.MAX_VALUE));
        configValuesDouble.put("RUNE_ATK_POWER", BUILDER.defineInRange("Rune_Atk_Power", 1, 0, Double.MAX_VALUE));
        configValuesDouble.put("RUNE_SHIELD_POWER", BUILDER.defineInRange("Rune_Shield_Power", 1, 0, Double.MAX_VALUE));
        configValuesDouble.put("RUNE_RECOVER_POWER", BUILDER.defineInRange("Rune_Recover_Power", 12, 0, Double.MAX_VALUE));
        configValuesDouble.put("RUNE_RECOVER_VICTIM_POWER", BUILDER.defineInRange("Rune_Recover_Victim_Power", 6, 0, Double.MAX_VALUE));
        configValuesDouble.put("RUNE_REGAIN_HUNGER_POWER", BUILDER.defineInRange("Rune_Regain_Hunger_Power", 7, 0, Double.MAX_VALUE));
        configValuesDouble.put("RUNE_HUNGER_VICTIM_POWER", BUILDER.defineInRange("Rune_Hunger_Victim_Power", 7, 0, Double.MAX_VALUE));

        SPEC = BUILDER.build();
    }

    static final ForgeConfigSpec SPEC;

    public static float CHEST_CHANCE;
    public static float GEM_ATK_CHANCE;
    public static float GEM_DEF_CHANCE;
    public static float GEM_HP_CHANCE;
    public static float GEM_SPD_CHANCE;
    public static float GEM_CRITDMG_CHANCE;
    public static float GEM_BLOCK_CHANCE;
    public static float RUNE_CHANCE;
    public static float SPIRIT_FIRE_CHANCE;
    public static float SPIRIT_WATER_CHANCE;
    public static float SPIRIT_WOOD_CHANCE;
    public static float SPIRIT_LIGHT_CHANCE;
    public static float SPIRIT_DARK_CHANCE;
    public static double GEM_ATK_POWER;
    public static double GEM_DEF_POWER;
    public static double GEM_HP_POWER;
    public static double GEM_SPD_POWER;
    public static double GEM_CRITDMG_POWER;
    public static double RUNE_ATK_POWER;
    public static double RUNE_SHIELD_POWER;
    public static double RUNE_RECOVER_POWER;
    public static double RUNE_RECOVER_VICTIM_POWER;
    public static double RUNE_REGAIN_HUNGER_POWER;
    public static double RUNE_HUNGER_VICTIM_POWER;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        CHEST_CHANCE = configValues.get("CHEST_CHANCE").get() / 100F;
        GEM_ATK_CHANCE = configValues.get("GEM_ATK_CHANCE").get() / 100F;
        GEM_DEF_CHANCE = configValues.get("GEM_DEF_CHANCE").get() / 100F;
        GEM_HP_CHANCE = configValues.get("GEM_HP_CHANCE").get() / 100F;
        GEM_SPD_CHANCE = configValues.get("GEM_SPD_CHANCE").get() / 100F;
        GEM_CRITDMG_CHANCE = configValues.get("GEM_CRITDMG_CHANCE").get() / 100F;
        GEM_BLOCK_CHANCE = configValues.get("GEM_BLOCK_CHANCE").get() / 100F;
        RUNE_CHANCE = configValues.get("RUNE_CHANCE").get() / 100F;
        SPIRIT_FIRE_CHANCE = configValues.get("SPIRIT_FIRE_CHANCE").get() / 100F;
        SPIRIT_WATER_CHANCE = configValues.get("SPIRIT_WATER_CHANCE").get() / 100F;
        SPIRIT_WOOD_CHANCE = configValues.get("SPIRIT_WOOD_CHANCE").get() / 100F;
        SPIRIT_LIGHT_CHANCE = configValues.get("SPIRIT_LIGHT_CHANCE").get() / 100F;
        SPIRIT_DARK_CHANCE = configValues.get("SPIRIT_DARK_CHANCE").get() / 100F;
        GEM_ATK_POWER = configValuesDouble.get("GEM_ATK_POWER").get();
        GEM_DEF_POWER = configValuesDouble.get("GEM_DEF_POWER").get();
        GEM_HP_POWER = configValuesDouble.get("GEM_HP_POWER").get();
        GEM_SPD_POWER = configValuesDouble.get("GEM_SPD_POWER").get();
        GEM_CRITDMG_POWER = configValuesDouble.get("GEM_CRITDMG_POWER").get();
        RUNE_ATK_POWER = configValuesDouble.get("RUNE_ATK_POWER").get();
        RUNE_SHIELD_POWER = configValuesDouble.get("RUNE_SHIELD_POWER").get();
        RUNE_RECOVER_POWER = configValuesDouble.get("RUNE_RECOVER_POWER").get();
        RUNE_RECOVER_VICTIM_POWER = configValuesDouble.get("RUNE_RECOVER_VICTIM_POWER").get();
        RUNE_REGAIN_HUNGER_POWER = configValuesDouble.get("RUNE_REGAIN_HUNGER_POWER").get();
        RUNE_HUNGER_VICTIM_POWER = configValuesDouble.get("RUNE_HUNGER_VICTIM_POWER").get();
    }
}
