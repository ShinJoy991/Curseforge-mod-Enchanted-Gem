package com.github.shinjoy991.enchantedgem.event.gui;

import com.github.shinjoy991.enchantedgem.item.ElementalSpiritItem;
import com.github.shinjoy991.enchantedgem.item.EnchantedGemItem;
import com.github.shinjoy991.enchantedgem.item.GemPickerItem;
import com.github.shinjoy991.enchantedgem.item.RuneItem;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.github.shinjoy991.enchantedgem.EnchantedGem.MODID;
import static com.github.shinjoy991.enchantedgem.helpers.DelayFunc.delayedTask;

@Mod.EventBusSubscriber(modid = MODID)
public class AnvilHandler {

    private static final Map<UUID, Boolean> cooldown = new HashMap<>();

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();

        if (right.getItem() instanceof EnchantedGemItem enchantedGemItem) {
            EnchantedGemItem.GemType gemType = enchantedGemItem.getGemType();

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

                event.setOutput(output);
                event.setCost(1);
                event.setMaterialCost(1);
            }
        } else
            if (right.getItem() instanceof GemPickerItem) {
                if (left.hasTag() && left.getTag().contains("GEM_INLAY_COUNT")) {
                    ItemStack resultItem = removeGemFromItem(left);
//                    NonNullList<ItemStack> removedGems = getRemovedGems(left);

//                    if (!removedGems.isEmpty()) {
                        event.setOutput(resultItem);
                        event.setCost(1);
                        event.setMaterialCost(1);
//                    }
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
                        event.setOutput(output);
                        event.setCost(1);
                        event.setMaterialCost(1);
                    }
                } else
                    if (right.getItem() instanceof ElementalSpiritItem spiritItem) {
                        ElementalSpiritItem.SpiritType spiritType = spiritItem.getSpiritType();

                        if (left.getItem().isDamageable(left) && !(left.getItem() instanceof ArmorItem || left.getItem() instanceof ElytraItem)) {

                            ItemStack output = left.copy();
                            CompoundTag tag = output.getOrCreateTag();
                            tag.putString("ENCHANTED_ELEMENTAL_SPIRIT", spiritType.name());
                            output.setTag(tag);

                            MutableComponent itemName = output.getHoverName().copy();
                            itemName = switch (spiritType) {
                                case FIRE_SPIRIT -> itemName.setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.RED));
                                case WATER_SPIRIT -> itemName.setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.DARK_BLUE));
                                case WOOD_SPIRIT -> itemName.setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.DARK_GREEN));
                                case LIGHT_SPIRIT -> itemName.setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.YELLOW));
                                case DARK_SPIRIT -> itemName.setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.DARK_PURPLE));
                                default -> itemName;
                            };
                            output.setHoverName(itemName);

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
            for (int i = 1; i < gemCount; i++) {
                tag.putString("GEM_INLAY_" + i, tag.getString("GEM_INLAY_" + (i + 1)));
            }
            tag.remove("GEM_INLAY_" + gemCount);
            if (gemCount == 1) {
                tag.remove("GEM_INLAY_COUNT");
            } else {
                tag.putInt("GEM_INLAY_COUNT", gemCount - 1);
            }
        }

        return resultItem;
    }

//    private static NonNullList<ItemStack> getRemovedGems(ItemStack item) {
//        NonNullList<ItemStack> removedGems = NonNullList.create();
//        CompoundTag tag = item.getOrCreateTag();
//
//        for (int i = 1; i <= 3; i++) {
//            String gemTypeStr = tag.getString("GEM_INLAY_" + i);
//            switch (gemTypeStr) {
//                case "ATK_1" -> removedGems.add(new ItemStack(ModItems.ENCHANTED_GEM_ATK1.get()));
//                case "DEF_1" -> removedGems.add(new ItemStack(ModItems.ENCHANTED_GEM_DEF1.get()));
//                case "HP_1" -> removedGems.add(new ItemStack(ModItems.ENCHANTED_GEM_HP1.get()));
//                case "SPD_1" -> removedGems.add(new ItemStack(ModItems.ENCHANTED_GEM_SPD1.get()));
//                case "CRITDMG_1" -> removedGems.add(new ItemStack(ModItems.ENCHANTED_GEM_CRITDMG1.get()));
//            }
//        }
//        return removedGems;
//    }
}