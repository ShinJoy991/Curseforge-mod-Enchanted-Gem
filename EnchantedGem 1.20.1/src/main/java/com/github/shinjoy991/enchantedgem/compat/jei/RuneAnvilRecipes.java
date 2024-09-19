package com.github.shinjoy991.enchantedgem.compat.jei;

import com.github.shinjoy991.enchantedgem.item.RuneItem;
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
                applyGemTag(Items.BOW.getDefaultInstance()), applyGemTag(Items.CROSSBOW.getDefaultInstance()),
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

    private List<ItemStack> applyRuneEffect(ItemStack input, List<ItemStack> runeTypeList) {
        List<ItemStack> output = new ArrayList<>();
        for (ItemStack input2 : runeTypeList) {
            ItemStack inputcopy = input.copy();
            CompoundTag tag = inputcopy.getOrCreateTag();
            RuneItem runeItem = (RuneItem) input2.getItem();
            RuneItem.RuneType runeType = runeItem.getRuneType();
            tag.putInt("ENCHANTEDGEM_HASRUNE", 1);
            tag.putString("ENCHANTEDGEM_RUNE", runeType.name());

            output.add(inputcopy);
        }
        return output;
    }

}