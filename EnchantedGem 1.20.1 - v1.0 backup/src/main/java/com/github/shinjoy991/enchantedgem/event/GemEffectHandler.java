package com.github.shinjoy991.enchantedgem.event;

import com.github.shinjoy991.enchantedgem.Config;
import com.github.shinjoy991.enchantedgem.EnchantedGem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = EnchantedGem.MODID)
public class GemEffectHandler {

    private static final String GEM_ATK_UUID = "6a2e8e8a-45a1-41b9-b738-09cbd3ab0b87";
    private static final String GEM_DEF_UUID = "a67f6de3-8d42-4e63-a1f2-87e0f3e1b0a3";
    private static final String GEM_HP_UUID = "f0f79b3b-f3f0-4e28-a789-091b66a1c8db";
    private static final String GEM_SPD_UUID = "c8ed3d12-8e53-4c57-85b4-4c3d1af1bca1";

    @SubscribeEvent
    public static void onLivingHurt2(CriticalHitEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer attacker))
            return;
        try {
            int bonus = 0;
            for (ItemStack armorStack : attacker.getInventory().armor) {
                bonus += getGemBonusFromItem(armorStack, "CRITDMG_1");
            }
            bonus += getGemBonusFromItem(attacker.getMainHandItem(), "CRITDMG_1");
            if (event.isVanillaCritical()) {
                float val = (float) (event.getOldDamageModifier() + bonus * Config.GEM_CRITDMG_POWER * 0.01F);
                event.setDamageModifier(val);
            }
        } catch (Exception ignored) {}
    }

    @SubscribeEvent
    public static void onPlayerChangeEquip(LivingEquipmentChangeEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        removeAttributeBonus(player, event.getFrom());

        if (event.getTo().is(Items.AIR)) {
            return;
        }
        CompoundTag tag = event.getTo().getTag();
        if (tag == null || !tag.contains("GEM_INLAY_COUNT"))
            return;
        applyAttributeBonus(player, tag);
    }

    public static void removeAttributeBonus(ServerPlayer player, ItemStack armor) {
        CompoundTag tag = armor.getTag();
        if (tag == null || !tag.contains("GEM_INLAY_COUNT"))
            return;
        for (int i = 1; i <= 3; i++) {
            String type = tag.getString("GEM_INLAY_" + i);
            try {
                switch (type) {
                    case "ATK_1" -> {
                        AttributeInstance attributeInstance = player.getAttribute(Attributes.ATTACK_DAMAGE);
                        if (attributeInstance != null && tag.hasUUID("GemATKUUID_" + i)) {
                            attributeInstance.removeModifier(tag.getUUID("GemATKUUID_" + i));
                        }
                    }
                    case "DEF_1" -> {
                        AttributeInstance attributeInstance = player.getAttribute(Attributes.ARMOR);
                        if (attributeInstance != null && tag.hasUUID("GemDEFUUID_" + i)) {
                            attributeInstance.removeModifier(tag.getUUID("GemDEFUUID_" + i));
                        }
                    }
                    case "HP_1" -> {
                        AttributeInstance attributeInstance = player.getAttribute(Attributes.MAX_HEALTH);
                        if (attributeInstance != null && tag.hasUUID("GemHPUUID_" + i)) {
                            attributeInstance.removeModifier(tag.getUUID("GemHPUUID_" + i));
                            player.setHealth(player.getHealth());
                        }
                    }
                    case "SPD_1" -> {
                        AttributeInstance attributeInstance = player.getAttribute(Attributes.MOVEMENT_SPEED);
                        if (attributeInstance != null && tag.hasUUID("GemSPDUUID_" + i)) {
                            attributeInstance.removeModifier(tag.getUUID("GemSPDUUID_" + i));
                        }
                    }
                }
            } catch (Exception ignored) {
            }
        }
    }

    private static void applyAttributeBonus(Player player, CompoundTag tag) {
        try {
            int gemCount = tag.getInt("GEM_INLAY_COUNT");
            for (int i = 1; i <= gemCount; i++) {
                String gemName = tag.getString("GEM_INLAY_" + i);
                switch (gemName) {
                    case "ATK_1" -> {
                        AttributeInstance attributeInstance = player.getAttribute(Attributes.ATTACK_DAMAGE);
                        if (attributeInstance != null) {
                            UUID uuid = UUID.randomUUID();
                            AttributeModifier modifier = new AttributeModifier(uuid, "Gem ATK Bonus", Config.GEM_ATK_POWER, AttributeModifier.Operation.ADDITION);
                            attributeInstance.addTransientModifier(modifier);
                            tag.putUUID("GemATKUUID_" + i, uuid);
                        }
                    }
                    case "DEF_1" -> {
                        AttributeInstance attributeInstance = player.getAttribute(Attributes.ARMOR);
                        if (attributeInstance != null) {
                            UUID uuid = UUID.randomUUID();
                            AttributeModifier modifier = new AttributeModifier(uuid, "Gem DEF Bonus", Config.GEM_DEF_POWER, AttributeModifier.Operation.ADDITION);
                            attributeInstance.addTransientModifier(modifier);
                            tag.putUUID("GemDEFUUID_" + i, uuid);
                        }
                    }

                    case "HP_1" -> {
                        AttributeInstance attributeInstance = player.getAttribute(Attributes.MAX_HEALTH);
                        if (attributeInstance != null) {
                            UUID uuid = UUID.randomUUID();
                            AttributeModifier modifier = new AttributeModifier(uuid, "Gem HP Bonus", Config.GEM_HP_POWER, AttributeModifier.Operation.ADDITION);
                            attributeInstance.addTransientModifier(modifier);
                            tag.putUUID("GemHPUUID_" + i, uuid);
                            player.setHealth(player.getHealth());
                        }
                    }
                    case "SPD_1" -> {
                        AttributeInstance attributeInstance = player.getAttribute(Attributes.MOVEMENT_SPEED);
                        if (attributeInstance != null) {
                            UUID uuid = UUID.randomUUID();
                            AttributeModifier modifier = new AttributeModifier(uuid, "Gem SPD Bonus", Config.GEM_SPD_POWER, AttributeModifier.Operation.ADDITION);
                            attributeInstance.addTransientModifier(modifier);
                            tag.putUUID("GemSPDUUID_" + i, uuid);
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }


    private static int getGemBonusFromItem(ItemStack itemStack, String gemType) {
        if (!itemStack.hasTag()) {
            return 0;
        }
        CompoundTag tag = itemStack.getTag();
        if (tag == null || !tag.contains("GEM_INLAY_COUNT")) {
            return 0;
        }

        int gemCount = tag.getInt("GEM_INLAY_COUNT");
        int bonus = 0;
        for (int i = 1; i <= gemCount; i++) {
            if (gemType.equals(tag.getString("GEM_INLAY_" + i))) {
                bonus++;
            }
        }
        return bonus;
    }
}