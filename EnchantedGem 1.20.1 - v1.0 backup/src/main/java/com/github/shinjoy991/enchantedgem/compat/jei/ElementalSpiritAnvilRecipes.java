package com.github.shinjoy991.enchantedgem.compat.jei;

import com.github.shinjoy991.enchantedgem.item.ElementalSpiritItem;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class ElementalSpiritAnvilRecipes implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("enchantedgem:anvil_recipes");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IVanillaRecipeFactory factory = registration.getVanillaRecipeFactory();
        List<IJeiAnvilRecipe> anvilRecipes = new ArrayList<>();

        List<ItemStack> spiritStack = new ArrayList<>();
        spiritStack.add(ModItems.ENCHANTED_ELEMENTAL_SPIRIT_FIRE.get().getDefaultInstance());
        spiritStack.add(ModItems.ENCHANTED_ELEMENTAL_SPIRIT_WATER.get().getDefaultInstance());
        spiritStack.add(ModItems.ENCHANTED_ELEMENTAL_SPIRIT_WOOD.get().getDefaultInstance());
        spiritStack.add(ModItems.ENCHANTED_ELEMENTAL_SPIRIT_DARK.get().getDefaultInstance());
        spiritStack.add(ModItems.ENCHANTED_ELEMENTAL_SPIRIT_LIGHT.get().getDefaultInstance());
        List<ItemStack> enchantableItems = List.of(
                Items.IRON_SWORD.getDefaultInstance(), Items.IRON_AXE.getDefaultInstance(),
                Items.IRON_PICKAXE.getDefaultInstance(), Items.IRON_SHOVEL.getDefaultInstance(),
                Items.IRON_HOE.getDefaultInstance(), Items.BOW.getDefaultInstance(), Items.CROSSBOW.getDefaultInstance(),
                Items.TRIDENT.getDefaultInstance()
        );
        for (ItemStack enchantableItems_component : enchantableItems) {
            anvilRecipes.add(factory.createAnvilRecipe(enchantableItems_component, spiritStack,
                    applySpiritEffect(enchantableItems_component, spiritStack), new ResourceLocation(
                            "enchantedgem:anvil_recipes")));
        }
        registration.addRecipes(RecipeTypes.ANVIL, anvilRecipes);
    }

    private List<ItemStack> applySpiritEffect(ItemStack input, List<ItemStack> gemTypeList) {
        List<ItemStack> output = new ArrayList<>();
        for (ItemStack input2 : gemTypeList) {
            ItemStack inputcopy = input.copy();
            CompoundTag tag = inputcopy.getOrCreateTag();
            ElementalSpiritItem spiritItem = (ElementalSpiritItem) input2.getItem();
            ElementalSpiritItem.SpiritType spiritType = spiritItem.getSpiritType();
            tag.putString("ENCHANTED_ELEMENTAL_SPIRIT", spiritType.name());

            CompoundTag displayTag = inputcopy.getOrCreateTagElement("display");
            ListTag loreList = displayTag.getList("Lore", 8);
            loreList.clear();

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

            ListTag newLoreList = new ListTag();
            newLoreList.add(StringTag.valueOf(Component.Serializer.toJson(baseLore)));

            displayTag.put("Lore", newLoreList);
            output.add(inputcopy);
        }
        return output;
    }
}