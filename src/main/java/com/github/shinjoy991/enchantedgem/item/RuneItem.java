package com.github.shinjoy991.enchantedgem.item;

import com.github.shinjoy991.enchantedgem.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

import java.util.List;

public class RuneItem extends Item{

    private final RuneType runeType;

    public enum RuneType {
        ATK_1, SHIELD_1, RECOVER_1, RECOVER_VICTIM_1, REGAIN_HUNGER_1, HUNGER_VICTIM_1
    }

    public RuneItem(Item.Properties properties, RuneType runeType) {
        super(properties);
        this.runeType = runeType;
    }
    public boolean isFoil(ItemStack stack) {
        return true;
    }
    public RuneType getRuneType() {
        return runeType;
    }

    String formatPower(double value) {
        return value == Math.floor(value) ? String.valueOf((int) value) : String.valueOf(value);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        String gemstoneLore = "Rune effect: ";
        MutableComponent baseLore =
                Component.literal(gemstoneLore).setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GREEN).withItalic(false));
        tooltip.add(baseLore);
        if (this.runeType == RuneType.ATK_1)
            tooltip.add(Component.literal("+" + formatPower(Config.RUNE_ATK_POWER) + " Damage").setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0xFF0000))));
        else if (this.runeType == RuneType.SHIELD_1)
            tooltip.add(Component.literal("-" + formatPower(Config.RUNE_SHIELD_POWER) + " Damage taken").setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0xADD8E6))));
        else if (this.runeType == RuneType.RECOVER_1)
                tooltip.add(Component.literal("Recover 1 health point when dealing more than " + formatPower(Config.RUNE_RECOVER_POWER) + " damage").setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.YELLOW)));
            else if (this.runeType == RuneType.RECOVER_VICTIM_1)
                    tooltip.add(Component.literal("Heal 1 point when taking more than " + formatPower(Config.RUNE_RECOVER_VICTIM_POWER) + " damage").setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.GREEN)));
                else if (this.runeType == RuneType.REGAIN_HUNGER_1)
                        tooltip.add(Component.literal(formatPower(Config.RUNE_REGAIN_HUNGER_POWER) + "% chance fill 1 hunger point when " +
                                "attacking").setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.GOLD)));
                    else if (this.runeType == RuneType.HUNGER_VICTIM_1)
                            tooltip.add(Component.literal(formatPower(Config.RUNE_HUNGER_VICTIM_POWER) + "% chance to deplete 1 hunger point of the enemy").setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0xFC6203))));
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
