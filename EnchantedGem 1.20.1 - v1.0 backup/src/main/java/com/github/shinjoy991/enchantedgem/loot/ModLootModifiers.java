package com.github.shinjoy991.enchantedgem.loot;

import com.github.shinjoy991.enchantedgem.EnchantedGem;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModLootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, EnchantedGem.MODID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_RUNE_ITEM =
            LOOT_MODIFIER_SERIALIZERS.register("add_rune_item", AddArchaeologyRuneModifier.CODEC);

    public static void register(IEventBus eventBus) {
        LOOT_MODIFIER_SERIALIZERS.register(eventBus);
    }
}
