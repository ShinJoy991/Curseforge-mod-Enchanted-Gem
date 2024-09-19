package com.github.shinjoy991.enchantedgem.register;

import com.github.shinjoy991.enchantedgem.item.ElementalSpiritItem;
import com.github.shinjoy991.enchantedgem.item.EnchantedGemItem;
import com.github.shinjoy991.enchantedgem.item.GemPickerItem;
import com.github.shinjoy991.enchantedgem.item.RuneItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.github.shinjoy991.enchantedgem.EnchantedGem.MODID;

@EventBusSubscriber(modid = MODID, bus = Bus.MOD)
public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Item> ENCHANTED_GEM_ATK1 = ITEMS.register("enchanted_gem_atk1",
            () -> new EnchantedGemItem(new Item.Properties().rarity(Rarity.RARE).setNoRepair(), EnchantedGemItem.GemType.ATK_1)
    );
    public static final RegistryObject<Item> ENCHANTED_GEM_DEF1 = ITEMS.register("enchanted_gem_def1",
            () -> new EnchantedGemItem(new Item.Properties().rarity(Rarity.RARE).setNoRepair(), EnchantedGemItem.GemType.DEF_1)
    );
    public static final RegistryObject<Item> ENCHANTED_GEM_HP1 = ITEMS.register("enchanted_gem_hp1",
            () -> new EnchantedGemItem(new Item.Properties().rarity(Rarity.RARE).setNoRepair(),
                    EnchantedGemItem.GemType.HP_1)
    );
    public static final RegistryObject<Item> ENCHANTED_GEM_SPD1 = ITEMS.register("enchanted_gem_spd1",
            () -> new EnchantedGemItem(new Item.Properties().rarity(Rarity.RARE).setNoRepair(),
                    EnchantedGemItem.GemType.SPD_1)
    );
    public static final RegistryObject<Item> ENCHANTED_GEM_CRITDMG1 = ITEMS.register(
            "enchanted_gem_critdmg1",
            () -> new EnchantedGemItem(new Item.Properties().rarity(Rarity.RARE).setNoRepair(),
                    EnchantedGemItem.GemType.CRITDMG_1)
    );

    public static final RegistryObject<Item> EXTRACT_GEM_HAMMER = ITEMS.register("gem_picker",
            () -> new GemPickerItem(new Item.Properties()));

    public static final RegistryObject<Item> ENCHANTED_RUNE_ATK1 = ITEMS.register(
            "enchanted_rune_atk1",
            () -> new RuneItem(new Item.Properties().rarity(Rarity.RARE).setNoRepair(),
                    RuneItem.RuneType.ATK_1)
    );
    public static final RegistryObject<Item> ENCHANTED_RUNE_SHIELD1 = ITEMS.register(
            "enchanted_rune_shield1",
            () -> new RuneItem(new Item.Properties().rarity(Rarity.RARE).setNoRepair(),
                    RuneItem.RuneType.SHIELD_1)
    );
    public static final RegistryObject<Item> ENCHANTED_RUNE_RECOVER1 = ITEMS.register(
            "enchanted_rune_recover1",
            () -> new RuneItem(new Item.Properties().rarity(Rarity.RARE).setNoRepair(),
                    RuneItem.RuneType.RECOVER_1)
    );
    public static final RegistryObject<Item> ENCHANTED_RUNE_RECOVER_VICTIM1 = ITEMS.register(
            "enchanted_rune_recover_victim1",
            () -> new RuneItem(new Item.Properties().rarity(Rarity.RARE).setNoRepair(),
                    RuneItem.RuneType.RECOVER_VICTIM_1)
    );
    public static final RegistryObject<Item> ENCHANTED_RUNE_REGAIN_HUNGER1 = ITEMS.register(
            "enchanted_rune_regain_hunger1",
            () -> new RuneItem(new Item.Properties().rarity(Rarity.RARE).setNoRepair(),
                    RuneItem.RuneType.REGAIN_HUNGER_1)
    );
    public static final RegistryObject<Item> ENCHANTED_RUNE_HUNGER_VICTIM1 = ITEMS.register(
            "enchanted_rune_hunger_victim1",
            () -> new RuneItem(new Item.Properties().rarity(Rarity.RARE).setNoRepair(),
                    RuneItem.RuneType.HUNGER_VICTIM_1)
    );

    public static final RegistryObject<Item> ENCHANTED_ELEMENTAL_SPIRIT_FIRE = ITEMS.register(
            "enchanted_elemental_spirit_fire",
            () -> new ElementalSpiritItem(new Item.Properties().rarity(Rarity.RARE).setNoRepair(),
                    ElementalSpiritItem.SpiritType.FIRE_SPIRIT)
    );
    public static final RegistryObject<Item> ENCHANTED_ELEMENTAL_SPIRIT_WATER = ITEMS.register(
            "enchanted_elemental_spirit_water",
            () -> new ElementalSpiritItem(new Item.Properties().rarity(Rarity.RARE).setNoRepair(),
                    ElementalSpiritItem.SpiritType.WATER_SPIRIT)
    );
    public static final RegistryObject<Item> ENCHANTED_ELEMENTAL_SPIRIT_WOOD = ITEMS.register(
            "enchanted_elemental_spirit_wood",
            () -> new ElementalSpiritItem(new Item.Properties().rarity(Rarity.RARE).setNoRepair(),
                    ElementalSpiritItem.SpiritType.WOOD_SPIRIT)
    );
    public static final RegistryObject<Item> ENCHANTED_ELEMENTAL_SPIRIT_LIGHT = ITEMS.register(
            "enchanted_elemental_spirit_light",
            () -> new ElementalSpiritItem(new Item.Properties().rarity(Rarity.RARE).setNoRepair(),
                    ElementalSpiritItem.SpiritType.LIGHT_SPIRIT)
    );
    public static final RegistryObject<Item> ENCHANTED_ELEMENTAL_SPIRIT_DARK = ITEMS.register(
            "enchanted_elemental_spirit_dark",
            () -> new ElementalSpiritItem(new Item.Properties().rarity(Rarity.RARE).setNoRepair(),
                    ElementalSpiritItem.SpiritType.DARK_SPIRIT)
    );

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<CreativeModeTab>  ENCHANTMENT_TAB =
            CREATIVE_MODE_TAB.register("enchantedgem_tab",
                    () -> CreativeModeTab.builder()
                            .icon(() -> new ItemStack(ModItems.ENCHANTED_GEM_ATK1.get()))
                            .displayItems((parameters, output) -> {
                                output.accept(new ItemStack(ModItems.ENCHANTED_GEM_ATK1.get()));
                                output.accept(new ItemStack(ModItems.ENCHANTED_GEM_DEF1.get()));
                                output.accept(new ItemStack(ModItems.ENCHANTED_GEM_HP1.get()));
                                output.accept(new ItemStack(ModItems.ENCHANTED_GEM_SPD1.get()));
                                output.accept(new ItemStack(ModItems.ENCHANTED_GEM_CRITDMG1.get()));
                                output.accept(new ItemStack(ModItems.EXTRACT_GEM_HAMMER.get()));
                                output.accept(new ItemStack(ModItems.ENCHANTED_RUNE_ATK1.get()));
                                output.accept(new ItemStack(ModItems.ENCHANTED_RUNE_SHIELD1.get()));
                                output.accept(new ItemStack(ModItems.ENCHANTED_RUNE_RECOVER1.get()));
                                output.accept(new ItemStack(ModItems.ENCHANTED_RUNE_RECOVER_VICTIM1.get()));
                                output.accept(new ItemStack(ModItems.ENCHANTED_RUNE_REGAIN_HUNGER1.get()));
                                output.accept(new ItemStack(ModItems.ENCHANTED_RUNE_HUNGER_VICTIM1.get()));
                                output.accept(new ItemStack(ModItems.ENCHANTED_ELEMENTAL_SPIRIT_FIRE.get()));
                                output.accept(new ItemStack(ModItems.ENCHANTED_ELEMENTAL_SPIRIT_WATER.get()));
                                output.accept(new ItemStack(ModItems.ENCHANTED_ELEMENTAL_SPIRIT_WOOD.get()));
                                output.accept(new ItemStack(ModItems.ENCHANTED_ELEMENTAL_SPIRIT_LIGHT.get()));
                                output.accept(new ItemStack(ModItems.ENCHANTED_ELEMENTAL_SPIRIT_DARK.get()));
                            })
                            .title(Component.translatable("itemGroup.enchantedgem_tab"))
                            .build()
            );

}
