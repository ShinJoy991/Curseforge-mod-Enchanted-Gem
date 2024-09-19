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

            MutableComponent itemName = input.getHoverName().copy();
            itemName = switch (spiritType) {
                case FIRE_SPIRIT -> itemName.setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.RED));
                case WATER_SPIRIT -> itemName.setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.DARK_BLUE));
                case WOOD_SPIRIT -> itemName.setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.DARK_GREEN));
                case LIGHT_SPIRIT -> itemName.setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.YELLOW));
                case DARK_SPIRIT -> itemName.setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.DARK_PURPLE));
                default -> itemName;
            };
            inputcopy.setHoverName(itemName);
            output.add(inputcopy);
        }
        return output;
    }
}