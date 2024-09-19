package com.github.shinjoy991.enchantedgem.loot;

import com.github.shinjoy991.enchantedgem.Config;
import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class AddArchaeologyRuneModifier extends LootModifier {
    public static final Supplier<Codec<AddArchaeologyRuneModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.create(inst -> codecStart(inst).and(
                            ForgeRegistries.ITEMS.getCodec().listOf().fieldOf("items").forGetter(m -> m.items))
                    .apply(inst, AddArchaeologyRuneModifier::new)));

    private final List<Item> items;

    public AddArchaeologyRuneModifier(LootItemCondition[] conditionsIn, List<Item> items) {
        super(conditionsIn);
        this.items = items;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        for (LootItemCondition condition : this.conditions) {
            if (!condition.test(context)) {
                return generatedLoot;
            }
        }

        if (context.getRandom().nextFloat() < Config.RUNE_CHANCE) {
            generatedLoot.clear();
            Item randomItem = items.get(context.getRandom().nextInt(items.size()));
            generatedLoot.add(new ItemStack(randomItem));
        }

        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
