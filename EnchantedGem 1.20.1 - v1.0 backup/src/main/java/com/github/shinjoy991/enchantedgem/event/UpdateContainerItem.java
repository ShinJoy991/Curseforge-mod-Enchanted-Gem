package com.github.shinjoy991.enchantedgem.event;

import com.github.shinjoy991.enchantedgem.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class UpdateContainerItem {

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        for (ItemStack itemStack : event.getEntity().getInventory().items) {
            if (itemStack.hasTag() && itemStack.getTag().contains("GEM_INLAY_COUNT")) {
                updateGemstoneLore(itemStack);
            }
        }
    }

    @SubscribeEvent
    public static void onContainerOpen(PlayerContainerEvent.Open event) {
        for (ItemStack itemStack : event.getContainer().getItems()) {
            if (itemStack.hasTag() && itemStack.getTag().contains("GEM_INLAY_COUNT")) {
                updateGemstoneLore(itemStack);
            }
        }
    }

    static String formatPower(double value) {
        return value == Math.floor(value) ? String.valueOf((int) value) : String.valueOf(value);
    }

    private static void updateGemstoneLore(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        int gemCount = tag.getInt("GEM_INLAY_COUNT");

        if (gemCount > 0) {
            CompoundTag displayTag = itemStack.getOrCreateTagElement("display");
            ListTag loreList = displayTag.getList("Lore", 8);

            MutableComponent baseLore = Component.literal("Gemstone:")
                    .setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD).withItalic(false));

            for (int i = 0; i < gemCount; i++) {
                String gemTypeStr = tag.getString("GEM_INLAY_" + (i + 1));
                baseLore = switch (gemTypeStr) {
                    case "ATK_1" ->
                            baseLore.append(Component.literal("   +" + formatPower(Config.GEM_ATK_POWER) + " ATK").setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0xFF0000))));
                    case "DEF_1" ->
                            baseLore.append(Component.literal("   +" + formatPower(Config.GEM_DEF_POWER) + " DEF").setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0xADD8E6))));
                    case "HP_1" -> baseLore.append(Component.literal("   +" + formatPower(Config.GEM_HP_POWER) + " HP").setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.GREEN)));
                    case "SPD_1" -> baseLore.append(Component.literal("   +" + formatPower(Config.GEM_SPD_POWER) + " SPD").setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.YELLOW)));
                    case "CRITDMG_1" ->
                            baseLore.append(Component.literal("   +" + formatPower(Config.GEM_CRITDMG_POWER) + "% Crit DMG").setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0xFC6203))));
                    default -> baseLore;
                };
            }

            ListTag newLoreList = new ListTag();
            boolean added = false;

            for (int i = 0; i < loreList.size(); i++) {
                String existingLore = loreList.getString(i);
                if (existingLore.contains("Gemstone:")) {
                    newLoreList.add(StringTag.valueOf(Component.Serializer.toJson(baseLore)));
                    added = true;
                } else {
                    newLoreList.add(StringTag.valueOf(existingLore));
                }
            }

            if (added) {
                displayTag.put("Lore", newLoreList);
            }
        }
    }

}