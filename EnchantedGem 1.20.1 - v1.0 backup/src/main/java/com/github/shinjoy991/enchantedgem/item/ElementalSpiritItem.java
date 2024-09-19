package com.github.shinjoy991.enchantedgem.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

import java.util.List;

public class ElementalSpiritItem extends Item {

    private final SpiritType spiritType;

    public ElementalSpiritItem(Properties properties, SpiritType spiritType) {
        super(properties);
        this.spiritType = spiritType;
    }

    public boolean isFoil(ItemStack p_43138_) {
        return true;
    }

    public SpiritType getSpiritType() {
        return spiritType;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        String spiritstoneLore = "Spirit stone activation after imbue: ";
        MutableComponent baseLore =
                Component.literal(spiritstoneLore).setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GREEN).withItalic(false));
        tooltip.add(baseLore);
        if (this.spiritType == SpiritType.FIRE_SPIRIT) {
            tooltip.add(Component.literal("- Increase damage in nether and desert biomes").setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.RED)));
            tooltip.add(Component.literal("- Set target on fire").setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.RED)));
            tooltip.add(Component.literal("- Can lit some block in fire").setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.RED)));
            tooltip.add(Component.literal("- Reduce damage in water and rain").setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.RED)));
            tooltip.add(Component.literal("- 3% chance player catch fire when attacking").setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.RED)));
        } else
            if (this.spiritType == SpiritType.WATER_SPIRIT) {
                tooltip.add(Component.literal("- Increase damage in water and rain").setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.BLUE)));
                tooltip.add(Component.literal("- Gain speed buff in water").setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.BLUE)));
                tooltip.add(Component.literal("- Save you from falling too high").setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.BLUE)));
                tooltip.add(Component.literal("- Reduce damage in nether and desert biomes").setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.BLUE)));

            } else
                if (this.spiritType == SpiritType.WOOD_SPIRIT) {
                    tooltip.add(Component.literal("- Increase damage in forest and plain biomes").setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.GREEN)));
                    tooltip.add(Component.literal("- Gain resistance in forest").setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.GREEN)));
                    tooltip.add(Component.literal("- Gain regeneration in water").setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.GREEN)));
                } else
                    if (this.spiritType == SpiritType.LIGHT_SPIRIT) {
                        tooltip.add(Component.literal("- Increase damage in daylight and the end dimension").setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.YELLOW)));
                        tooltip.add(Component.literal("- Gain saturation").setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.YELLOW)));
                        tooltip.add(Component.literal("- Reduce melee damage from undead").setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.YELLOW)));
                        tooltip.add(Component.literal("- Reduce damage in darkness area").setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.YELLOW)));
                    } else
                        if (this.spiritType == SpiritType.DARK_SPIRIT) {
                            tooltip.add(Component.literal("- Increase damage in darkness area and night time").setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.LIGHT_PURPLE)));
                            tooltip.add(Component.literal("- Gain night vision").setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.LIGHT_PURPLE)));
                            tooltip.add(Component.literal("- Immune darkness effect").setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.LIGHT_PURPLE)));

                        }
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

    public enum SpiritType {
        FIRE_SPIRIT, WATER_SPIRIT, WOOD_SPIRIT, LIGHT_SPIRIT, DARK_SPIRIT
    }

}
