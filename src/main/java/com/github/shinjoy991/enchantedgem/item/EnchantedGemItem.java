package com.github.shinjoy991.enchantedgem.item;

import com.github.shinjoy991.enchantedgem.Config;
import com.github.shinjoy991.enchantedgem.EnchantedGem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public class EnchantedGemItem extends Item {

    private final GemType gemType;

    public enum GemType {
        ATK_1, DEF_1, HP_1, SPD_1, CRITDMG_1
    }

    public EnchantedGemItem(Properties properties, GemType gemType) {
        super(properties);
        this.gemType = gemType;
    }
    public boolean isFoil(ItemStack p_43138_) {
        return true;
    }
    public GemType getGemType() {
        return gemType;
    }

    String formatPower(double value) {
        return value == Math.floor(value) ? String.valueOf((int) value) : String.valueOf(value);
    }
    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        String gemstoneLore = "When applied: ";
        MutableComponent baseLore =
                Component.literal(gemstoneLore).setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GREEN).withItalic(false));
        tooltip.add(baseLore);
        if (this.gemType == GemType.ATK_1)
            tooltip.add(Component.literal("+" + formatPower(Config.GEM_ATK_POWER) + " ATK").setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0xFF0000))));
        else if (this.gemType == GemType.DEF_1)
            tooltip.add(Component.literal("+" + formatPower(Config.GEM_DEF_POWER) + " DEF").setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0xADD8E6))));
        else if (this.gemType == GemType.HP_1)
            tooltip.add(Component.literal("+" + formatPower(Config.GEM_HP_POWER) + " HP").setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.GREEN)));
        else if (this.gemType == GemType.SPD_1)
            tooltip.add(Component.literal("+" + formatPower(Config.GEM_SPD_POWER) + " SPD").setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.YELLOW)));
        else if (this.gemType == GemType.CRITDMG_1)
            tooltip.add(Component.literal("+" + formatPower(Config.GEM_CRITDMG_POWER) + "% Crit DMG").setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0xFC6203))));
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return true;
    }

}
