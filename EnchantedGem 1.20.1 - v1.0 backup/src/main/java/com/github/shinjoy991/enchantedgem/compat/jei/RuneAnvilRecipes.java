package com.github.shinjoy991.enchantedgem.compat.jei;

import com.github.shinjoy991.enchantedgem.Config;
import com.github.shinjoy991.enchantedgem.item.EnchantedGemItem;
import com.github.shinjoy991.enchantedgem.item.RuneItem;
import com.github.shinjoy991.enchantedgem.register.ModItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class RuneAnvilRecipes implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("enchantedgem:anvil_recipes");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IVanillaRecipeFactory factory = registration.getVanillaRecipeFactory();
        List<IJeiAnvilRecipe> anvilRecipes = new ArrayList<>();

        List<ItemStack> runeStack = new ArrayList<>();
        runeStack.add(ModItems.ENCHANTED_RUNE_ATK1.get().getDefaultInstance());
        runeStack.add(ModItems.ENCHANTED_RUNE_SHIELD1.get().getDefaultInstance());
        runeStack.add(ModItems.ENCHANTED_RUNE_RECOVER1.get().getDefaultInstance());
        runeStack.add(ModItems.ENCHANTED_RUNE_RECOVER_VICTIM1.get().getDefaultInstance());
        runeStack.add(ModItems.ENCHANTED_RUNE_REGAIN_HUNGER1.get().getDefaultInstance());
        runeStack.add(ModItems.ENCHANTED_RUNE_HUNGER_VICTIM1.get().getDefaultInstance());
        List<ItemStack> enchantableItems = List.of(
                applyGemTag(Items.IRON_SWORD.getDefaultInstance()), applyGemTag(Items.IRON_AXE.getDefaultInstance()),
                applyGemTag(Items.IRON_HELMET.getDefaultInstance()), applyGemTag(Items.IRON_CHESTPLATE.getDefaultInstance()),
                applyGemTag(Items.IRON_LEGGINGS.getDefaultInstance()), applyGemTag(Items.IRON_BOOTS.getDefaultInstance()),
                applyGemTag(Items.TRIDENT.getDefaultInstance())
        );
        for (ItemStack enchantableItems_component : enchantableItems) {
            anvilRecipes.add(factory.createAnvilRecipe(enchantableItems_component, runeStack,
                    applyRuneEffect(enchantableItems_component, runeStack), new ResourceLocation(
                            "enchantedgem:anvil_recipes")));
        }
        registration.addRecipes(RecipeTypes.ANVIL, anvilRecipes);
    }

    private ItemStack applyGemTag(ItemStack input) {
        ItemStack inputcopy = input.copy();
        CompoundTag tag = inputcopy.getOrCreateTag();
        tag.putInt("GEM_INLAY_COUNT", 3);
        tag.putString("GEM_INLAY_1", "ATK_1");
        tag.putString("GEM_INLAY_2", "ATK_1");
        tag.putString("GEM_INLAY_3", "ATK_1");
        return inputcopy;
    }
    static String formatPower(double value) {
        return value == Math.floor(value) ? String.valueOf((int) value) : String.valueOf(value);
    }

    private List<ItemStack> applyRuneEffect(ItemStack input, List<ItemStack> runeTypeList) {
        List<ItemStack> output = new ArrayList<>();
        for (ItemStack input2 : runeTypeList) {
            ItemStack inputcopy = input.copy();
            CompoundTag tag = inputcopy.getOrCreateTag();
            RuneItem runeItem = (RuneItem) input2.getItem();
            RuneItem.RuneType runeType = runeItem.getRuneType();
            tag.putInt("ENCHANTEDGEM_HASRUNE", 1);
            tag.putString("ENCHANTEDGEM_RUNE", runeType.name());

            CompoundTag displayTag = inputcopy.getOrCreateTagElement("display");
            ListTag loreList = displayTag.getList("Lore", 8);
            loreList.clear();

            MutableComponent baseLore = Component.literal("Gemstone:")
                    .setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD).withItalic(false))
                    .append(Component.literal("   +" + formatPower(Config.GEM_ATK_POWER) + " ATK" + "   +" + formatPower(Config.GEM_ATK_POWER) + " ATK" + "   +" + formatPower(Config.GEM_ATK_POWER) + " ATK")
                            .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0xFF0000))));
            ;

            MutableComponent runeLore =
                    Component.literal("------------").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(false))
                            .append(Component.literal("Rune")
                                    .setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.GOLD))
                                    .append(Component.literal("------------")
                                            .setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.GRAY))));

            MutableComponent baseLore1 =
                    Component.literal("").setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GREEN).withItalic(false));
            baseLore1 = switch (runeType.name()) {
                case "ATK_1" -> baseLore1.append(Component.literal("| | | | | |")
                        .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0x02421B)))
                        .append(Component.literal("Penetrative Rune")
                                .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0xCE76D6)).withBold(true))
                                .append(Component.literal("| | | | | |")
                                        .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0x02421B)).withBold(false)))));
                case "SHIELD_1" -> baseLore1.append(Component.literal("| | | | | | | | |")
                        .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0x02421B)))
                        .append(Component.literal("Giant Rune").withStyle()
                                .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0xCE76D6)).withBold(true))
                                .append(Component.literal("| | | | | | | | | |")
                                        .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0x02421B)).withBold(false)))));
                case "RECOVER_1" -> baseLore1.append(Component.literal("| | | | | | | |")
                        .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0x02421B)))
                        .append(Component.literal("Blessing Rune")
                                .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0xCE76D6)).withBold(true))
                                .append(Component.literal("| | | | | | | |")
                                        .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0x02421B)).withBold(false)))));
                case "RECOVER_VICTIM_1" -> baseLore1.append(Component.literal("| | | | | | | |")
                        .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0x02421B)))
                        .append(Component.literal("Vitality Rune")
                                .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0xCE76D6)).withBold(true))
                                .append(Component.literal("| | | | | | | | |")
                                        .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0x02421B)).withBold(false)))));
                case "REGAIN_HUNGER_1" -> baseLore1.append(Component.literal("| | | | | | | | | |")
                        .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0x02421B)))
                        .append(Component.literal("Wild Rune")
                                .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0xCE76D6)).withBold(true))
                                .append(Component.literal("| | | | | | | | | |")
                                        .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0x02421B)).withBold(false)))));
                case "HUNGER_VICTIM_1" -> baseLore1.append(Component.literal("| | | | | | | | |")
                        .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0x02421B)))
                        .append(Component.literal("Fierce Rune")
                                .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0xCE76D6)).withBold(true))
                                .append(Component.literal("| | | | | | | | |")
                                        .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0x02421B)).withBold(false)))));
                default -> baseLore1;
            };
            String gemLoreJson = Component.Serializer.toJson(baseLore);
            String runeLoreJson = Component.Serializer.toJson(baseLore1);

            ListTag newLoreList = new ListTag();

            newLoreList.add(StringTag.valueOf(gemLoreJson));
            newLoreList.add(StringTag.valueOf(Component.Serializer.toJson(runeLore)));
            newLoreList.add(StringTag.valueOf(runeLoreJson));

            displayTag.put("Lore", newLoreList);

            output.add(inputcopy);
        }
        return output;
    }

}