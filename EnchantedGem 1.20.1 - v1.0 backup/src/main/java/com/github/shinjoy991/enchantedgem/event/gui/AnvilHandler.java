package com.github.shinjoy991.enchantedgem.event.gui;

import com.github.shinjoy991.enchantedgem.Config;
import com.github.shinjoy991.enchantedgem.item.ElementalSpiritItem;
import com.github.shinjoy991.enchantedgem.item.EnchantedGemItem;
import com.github.shinjoy991.enchantedgem.item.GemPickerItem;
import com.github.shinjoy991.enchantedgem.item.RuneItem;
import com.github.shinjoy991.enchantedgem.register.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.github.shinjoy991.enchantedgem.EnchantedGem.MODID;
import static com.github.shinjoy991.enchantedgem.helpers.DelayFunc.delayedTask;

@Mod.EventBusSubscriber(modid = MODID)
public class AnvilHandler {

    private static final Map<UUID, Boolean> cooldown = new HashMap<>();

    static String formatPower(double value) {
        return value == Math.floor(value) ? String.valueOf((int) value) : String.valueOf(value);
    }

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();

        if (right.getItem() instanceof EnchantedGemItem enchantedGemItem) {
            EnchantedGemItem.GemType gemType = enchantedGemItem.getGemType();
//            CompoundTag tagcheck = left.getTag();
//            boolean isUnbreakable = tagcheck != null && tagcheck.getBoolean("Unbreakable");

            if (left.getItem().isDamageable(left)) {

                ItemStack output = left.copy();
                CompoundTag tag = output.getOrCreateTag();
                int gemCount = tag.getInt("GEM_INLAY_COUNT");

                if (gemCount < 3) {
                    gemCount++;
                    tag.putInt("GEM_INLAY_COUNT", gemCount);
                    tag.putString("GEM_INLAY_" + gemCount, gemType.name());
                } else {
                    event.setCanceled(true);
                    return;
                }

                output.setTag(tag);

                CompoundTag displayTag = output.getOrCreateTagElement("display");
                ListTag loreList = displayTag.getList("Lore", 8);

                String gemstoneLore = "Gemstone:";
                MutableComponent baseLore =
                        Component.literal(gemstoneLore).setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD).withItalic(false));
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

                String gemLoreJson = Component.Serializer.toJson(baseLore);

                boolean added = false;

                for (int i = 0; i < loreList.size(); i++) {
                    String existingLore = loreList.getString(i);
                    if (existingLore.contains("Elemental Spirit")  && !added) {
                        newLoreList.add(StringTag.valueOf(gemLoreJson));
                        newLoreList.add(StringTag.valueOf(existingLore));
                        added = true;
                    }
                    else if (existingLore.contains("Gemstone:")) {
                        newLoreList.add(StringTag.valueOf(gemLoreJson));
                        added = true;
                    }
                    else newLoreList.add(StringTag.valueOf(existingLore));
                }

                if (!added) {
                    newLoreList.add(StringTag.valueOf(gemLoreJson));
                }

                displayTag.put("Lore", newLoreList);

                event.setOutput(output);
                event.setCost(1);
                event.setMaterialCost(1);
            }
        } else
            if (right.getItem() instanceof GemPickerItem) {
                if (left.hasTag() && left.getTag().contains("GEM_INLAY_COUNT")) {
                    ItemStack resultItem = removeGemFromItem(left);
                    NonNullList<ItemStack> removedGems = getRemovedGems(left);

                    if (!removedGems.isEmpty()) {
                        event.setOutput(resultItem);
                        event.setCost(1);
                        event.setMaterialCost(1);
                    }
                }
            } else
                if (right.getItem() instanceof RuneItem runeItem) {
                    RuneItem.RuneType runeType = runeItem.getRuneType();

                    if (left.getItem().isDamageable(left)) {

                        ItemStack output = left.copy();
                        CompoundTag tag = output.getOrCreateTag();
                        int gemCount = tag.getInt("GEM_INLAY_COUNT");

                        if (gemCount < 3) {
                                UUID playeruuid = event.getPlayer().getUUID();
                                if (cooldown.getOrDefault(playeruuid, false))
                                    return;
                                cooldown.put(playeruuid, true);
                                delayedTask(20, () -> {
                                    cooldown.remove(playeruuid);
                                });
                                MutableComponent message = Component.literal("[EnchantedGem]")
                                        .setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD))
                                        .append(Component.literal(" You need to fill 3 gem slots " +
                                                "first!").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
                                event.getPlayer().sendSystemMessage(message);
                                event.setCanceled(true);
                                return;
                        }
                        int runeTypeAlready = tag.getInt("ENCHANTEDGEM_HASRUNE");
                        if (runeTypeAlready == 1) {
                            UUID playeruuid = event.getPlayer().getUUID();
                            if (cooldown.getOrDefault(playeruuid, false))
                                return;
                            cooldown.put(playeruuid, true);
                            delayedTask(20, () -> {
                                cooldown.remove(playeruuid);
                            });
                            MutableComponent message = Component.literal("[EnchantedGem]")
                                    .setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD))
                                    .append(Component.literal(" This equipment already has a " +
                                            "rune").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
                            event.getPlayer().sendSystemMessage(message);
                            event.setCanceled(true);
                            return;
                        }
                        tag.putInt("ENCHANTEDGEM_HASRUNE", 1);
                        tag.putString("ENCHANTEDGEM_RUNE", runeType.name());

                        output.setTag(tag);

                        CompoundTag displayTag = output.getOrCreateTagElement("display");
                        ListTag loreList = displayTag.getList("Lore", 8);
                        MutableComponent runeLore =
                                Component.literal("------------").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(false))
                                        .append(Component.literal("Rune")
                                                .setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.GOLD))
                                                .append(Component.literal("------------")
                                                        .setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.GRAY))));

                        MutableComponent baseLore =
                                Component.literal("").setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GREEN).withItalic(false));
                        baseLore = switch (runeType.name()) {
                            case "ATK_1" -> baseLore.append(Component.literal("| | | | | |")
                                    .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0x02421B)))
                                    .append(Component.literal("Penetrative Rune")
                                            .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0xCE76D6)).withBold(true))
                                            .append(Component.literal("| | | | | |")
                                                    .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0x02421B)).withBold(false)))));
                            case "SHIELD_1" -> baseLore.append(Component.literal("| | | | | | | | |")
                                    .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0x02421B)))
                                    .append(Component.literal("Giant Rune").withStyle()
                                            .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0xCE76D6)).withBold(true))
                                            .append(Component.literal("| | | | | | | | | |")
                                                    .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0x02421B)).withBold(false)))));
                            case "RECOVER_1" -> baseLore.append(Component.literal("| | | | | | | |")
                                    .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0x02421B)))
                                    .append(Component.literal("Blessing Rune")
                                            .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0xCE76D6)).withBold(true))
                                            .append(Component.literal("| | | | | | | |")
                                                    .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0x02421B)).withBold(false)))));
                            case "RECOVER_VICTIM_1" -> baseLore.append(Component.literal("| | | | | | | |")
                                    .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0x02421B)))
                                    .append(Component.literal("Vitality Rune")
                                            .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0xCE76D6)).withBold(true))
                                            .append(Component.literal("| | | | | | | | |")
                                                    .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0x02421B)).withBold(false)))));
                            case "REGAIN_HUNGER_1" -> baseLore.append(Component.literal("| | | | | | | | | |")
                                    .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0x02421B)))
                                    .append(Component.literal("Wild Rune")
                                            .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0xCE76D6)).withBold(true))
                                            .append(Component.literal("| | | | | | | | | |")
                                                    .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0x02421B)).withBold(false)))));
                            case "HUNGER_VICTIM_1" -> baseLore.append(Component.literal("| | | | | | | | |")
                                    .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0x02421B)))
                                    .append(Component.literal("Fierce Rune")
                                            .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0xCE76D6)).withBold(true))
                                            .append(Component.literal("| | | | | | | | |")
                                                    .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0x02421B)).withBold(false)))));
                            default -> baseLore;
                        };

                        String runeLoreJson = Component.Serializer.toJson(baseLore);

                        ListTag newLoreList = new ListTag();
                        boolean added = false;

                        for (int i = 0; i < loreList.size(); i++) {
                            String existingLore = loreList.getString(i);
                            if (existingLore.contains("Elemental Spirit")) {
                                if (loreList.getString(i - 1).contains("Gemstone:") && !added) {
                                    newLoreList.add(StringTag.valueOf(existingLore));
                                    newLoreList.add(StringTag.valueOf(Component.Serializer.toJson(runeLore)));
                                    newLoreList.add(StringTag.valueOf(runeLoreJson));
                                    added = true;
                                }
                            } else newLoreList.add(StringTag.valueOf(existingLore));
                        }

                        if (!added) {
                            newLoreList.add(StringTag.valueOf(Component.Serializer.toJson(runeLore)));
                            newLoreList.add(StringTag.valueOf(runeLoreJson));
                        }

                        displayTag.put("Lore", newLoreList);

                        event.setOutput(output);
                        event.setCost(1);
                        event.setMaterialCost(1);
                    }
                } else
                    if (right.getItem() instanceof ElementalSpiritItem spiritItem) {
                        ElementalSpiritItem.SpiritType spiritType = spiritItem.getSpiritType();

                        if (left.getItem().isDamageable(left) && !(left.getItem() instanceof ArmorItem)) {

                            ItemStack output = left.copy();
                            CompoundTag tag = output.getOrCreateTag();

                            tag.putString("ENCHANTED_ELEMENTAL_SPIRIT", spiritType.name());

                            output.setTag(tag);

                            CompoundTag displayTag = output.getOrCreateTagElement("display");
                            ListTag loreList = displayTag.getList("Lore", 8);
                            MutableComponent baseLore =
                                    Component.literal("Elemental Spirit")
                                                    .setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.GOLD))
                                                    .append(Component.literal(": ")
                                                            .setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.GRAY)));

                            baseLore = switch (spiritType.name()) {
                                case "FIRE_SPIRIT" -> baseLore
                                        .append(Component.literal("Fire")
                                                .setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.RED).withBold(false)));
                                case "WATER_SPIRIT" -> baseLore
                                        .append(Component.literal("Water").withStyle()
                                                .setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.DARK_BLUE).withBold(false)));
                                case "WOOD_SPIRIT" -> baseLore
                                        .append(Component.literal("Wood")
                                                .setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.DARK_GREEN).withBold(false)));
                                case "LIGHT_SPIRIT" -> baseLore
                                        .append(Component.literal("Light")
                                                .setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.YELLOW).withBold(false)));
                                case "DARK_SPIRIT" -> baseLore
                                        .append(Component.literal("Dark")
                                                .setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.DARK_PURPLE).withBold(false)));
                                default -> baseLore;
                            };

                            String elementalLoreJson = Component.Serializer.toJson(baseLore);

                            ListTag newLoreList = new ListTag();
                            boolean added = false;

                            for (int i = 0; i < loreList.size(); i++) {
                                String existingLore = loreList.getString(i);
                                if (!(existingLore.contains("Elemental Spirit"))) {
                                    newLoreList.add(StringTag.valueOf(existingLore));
                                }
                                if (existingLore.contains("Gemstone:") && !added) {
                                    newLoreList.add(StringTag.valueOf(elementalLoreJson));
                                    added = true;
                                }
                            }

                            if (!added) {
                                newLoreList.add(StringTag.valueOf(elementalLoreJson));
                            }

                            displayTag.put("Lore", newLoreList);

                            event.setOutput(output);
                            event.setCost(1);
                            event.setMaterialCost(1);
                        }
                    }

    }

    private static ItemStack removeGemFromItem(ItemStack item) {
        ItemStack resultItem = item.copy();
        CompoundTag tag = resultItem.getOrCreateTag();
        int gemCount = tag.getInt("GEM_INLAY_COUNT");

        if (gemCount > 0) {
            String removedGemType = tag.getString("GEM_INLAY_1");
            tag.remove("GEM_INLAY_1");
            gemCount--;

            for (int i = 1; i <= gemCount; i++) {
                tag.putString("GEM_INLAY_" + i, tag.getString("GEM_INLAY_" + (i + 1)));
            }
            tag.remove("GEM_INLAY_" + (gemCount + 1)); // Remove the now redundant last entry
            tag.putInt("GEM_INLAY_COUNT", gemCount);

            CompoundTag displayTag = resultItem.getOrCreateTagElement("display");
            ListTag loreList = displayTag.getList("Lore", 8);

            loreList.clear();
            if (gemCount > 0) {
                MutableComponent baseLore = Component.literal("Gemstone:")
                        .setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GREEN).withItalic(false));

                for (int i = 0; i < gemCount; i++) {
                    String gemTypeStr = tag.getString("GEM_INLAY_" + (i + 1));
                    baseLore = switch (gemTypeStr) {
                        case "ATK_1" -> baseLore.append(Component.literal("   +" + formatPower(Config.GEM_ATK_POWER) + " ATK").setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0xFF0000))));
                        case "DEF_1" -> baseLore.append(Component.literal("   +" + formatPower(Config.GEM_DEF_POWER) + " DEF").setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0xADD8E6))));
                        case "HP_1" -> baseLore.append(Component.literal("   +" + formatPower(Config.GEM_HP_POWER) + " HP").setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.GREEN)));
                        case "SPD_1" -> baseLore.append(Component.literal("   +" + formatPower(Config.GEM_SPD_POWER) + " SPD").setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.YELLOW)));
                        case "CRITDMG_1" -> baseLore.append(Component.literal("   +" + formatPower(Config.GEM_CRITDMG_POWER) + "% Crit DMG").setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.GOLD)));
                        default -> baseLore;
                    };

                }

                loreList.add(StringTag.valueOf(Component.Serializer.toJson(baseLore)));
            }
            displayTag.put("Lore", loreList);
        }
        return resultItem;
    }

    private static NonNullList<ItemStack> getRemovedGems(ItemStack item) {
        NonNullList<ItemStack> removedGems = NonNullList.create();
        CompoundTag tag = item.getOrCreateTag();

        for (int i = 1; i <= 3; i++) {
            String gemTypeStr = tag.getString("GEM_INLAY_" + i);
            switch (gemTypeStr) {
                case "ATK_1" -> removedGems.add(new ItemStack(ModItems.ENCHANTED_GEM_ATK1.get()));
                case "DEF_1" -> removedGems.add(new ItemStack(ModItems.ENCHANTED_GEM_DEF1.get()));
                case "HP_1" -> removedGems.add(new ItemStack(ModItems.ENCHANTED_GEM_HP1.get()));
                case "SPD_1" -> removedGems.add(new ItemStack(ModItems.ENCHANTED_GEM_SPD1.get()));
                case "CRITDMG_1" -> removedGems.add(new ItemStack(ModItems.ENCHANTED_GEM_CRITDMG1.get()));
            }
        }
        return removedGems;
    }
}