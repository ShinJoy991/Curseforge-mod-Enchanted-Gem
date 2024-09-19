package com.github.shinjoy991.enchantedgem.datagen;

import com.github.shinjoy991.enchantedgem.EnchantedGem;
import com.github.shinjoy991.enchantedgem.loot.AddArchaeologyRuneModifier;
import com.github.shinjoy991.enchantedgem.register.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

import java.util.List;

public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, EnchantedGem.MODID);
    }

    @Override
    protected void start() {
        ResourceLocation[] archaeologyLootTables = new ResourceLocation[]{
                new ResourceLocation("archaeology/desert_pyramid"),
                new ResourceLocation("archaeology/desert_well"),
                new ResourceLocation("archaeology/ocean_ruin_cold"),
                new ResourceLocation("archaeology/ocean_ruin_warm"),
                new ResourceLocation("archaeology/trail_ruins_common"),
                new ResourceLocation("archaeology/trail_ruins_rare")
        };
        for (ResourceLocation lootTable : archaeologyLootTables) {
            String modifierName = "rune_from_" + lootTable.getPath();
            add(modifierName, new AddArchaeologyRuneModifier(
                    new LootItemCondition[]{new LootTableIdCondition.Builder(lootTable).build()},
                    List.of(
                            ModItems.ENCHANTED_RUNE_ATK1.get(),
                            ModItems.ENCHANTED_RUNE_SHIELD1.get(),
                            ModItems.ENCHANTED_RUNE_RECOVER1.get(),
                            ModItems.ENCHANTED_RUNE_REGAIN_HUNGER1.get(),
                            ModItems.ENCHANTED_RUNE_HUNGER_VICTIM1.get(),
                            ModItems.ENCHANTED_RUNE_RECOVER_VICTIM1.get()
                    )
            ));
        }
    }
}
