package com.github.shinjoy991.enchantedgem.compat.jei;

import com.github.shinjoy991.enchantedgem.item.EnchantedGemItem;
import com.github.shinjoy991.enchantedgem.register.ModItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class EnchantedGemAnvilRecipes implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("enchantedgem:anvil_recipes");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IVanillaRecipeFactory factory = registration.getVanillaRecipeFactory();
        List<IJeiAnvilRecipe> anvilRecipes = new ArrayList<>();

        List<ItemStack> gemStack = new ArrayList<>();
        gemStack.add(ModItems.ENCHANTED_GEM_ATK1.get().getDefaultInstance());
        gemStack.add(ModItems.ENCHANTED_GEM_DEF1.get().getDefaultInstance());
        gemStack.add(ModItems.ENCHANTED_GEM_HP1.get().getDefaultInstance());
        gemStack.add(ModItems.ENCHANTED_GEM_SPD1.get().getDefaultInstance());
        gemStack.add(ModItems.ENCHANTED_GEM_CRITDMG1.get().getDefaultInstance());
        List<ItemStack> enchantableItems = List.of(
                Items.IRON_SWORD.getDefaultInstance(), Items.IRON_AXE.getDefaultInstance(),
                Items.IRON_HELMET.getDefaultInstance(), Items.IRON_CHESTPLATE.getDefaultInstance(),
                Items.IRON_LEGGINGS.getDefaultInstance(), Items.IRON_BOOTS.getDefaultInstance(),
                Items.BOW.getDefaultInstance(), Items.CROSSBOW.getDefaultInstance(),
                Items.TRIDENT.getDefaultInstance()
        );
        for (ItemStack enchantableItems_component : enchantableItems) {
            anvilRecipes.add(factory.createAnvilRecipe(enchantableItems_component, gemStack,
                    applyGemEffect(enchantableItems_component, gemStack), new ResourceLocation(
                            "enchantedgem:anvil_recipes")));
        }
        registration.addRecipes(RecipeTypes.ANVIL, anvilRecipes);
    }

    private List<ItemStack> applyGemEffect(ItemStack input, List<ItemStack> gemTypeList) {
        List<ItemStack> output = new ArrayList<>();
        for (ItemStack input2 : gemTypeList) {
            ItemStack inputcopy = input.copy();
            CompoundTag tag = inputcopy.getOrCreateTag();
            tag.putInt("GEM_INLAY_COUNT", 1);
            EnchantedGemItem enchantedGemItem = (EnchantedGemItem) input2.getItem();
            EnchantedGemItem.GemType gemType = enchantedGemItem.getGemType();
            tag.putString("GEM_INLAY_1", gemType.name());

            output.add(inputcopy);
        }
        return output;
    }
}