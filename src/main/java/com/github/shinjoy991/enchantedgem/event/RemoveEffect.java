package com.github.shinjoy991.enchantedgem.event;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.github.shinjoy991.enchantedgem.EnchantedGem.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class RemoveEffect {
    @SubscribeEvent
    public static void onEntityLeaving(EntityLeaveLevelEvent event) {
        if (!(event.getEntity() instanceof LivingEntity living)) {
            return;
        }
        for (ItemStack armorStack : living.getArmorSlots()) {
            removeAttributeBonus(living, armorStack);
        }
        removeAttributeBonus(living, living.getMainHandItem());

    }


    public static void removeAttributeBonus(LivingEntity player, ItemStack armor) {
        CompoundTag tag = armor.getTag();
        if (tag == null || !tag.contains("GEM_INLAY_COUNT"))
            return;
        for (int i = 1; i <= 3; i++) {
            String type = tag.getString("GEM_INLAY_" + i);
            try {
                switch (type) {
                    case "ATK_1" -> {
                        AttributeInstance attributeInstance =
                                player.getAttribute(Attributes.ATTACK_DAMAGE);
                        attributeInstance.setBaseValue(attributeInstance.getBaseValue() - 1);
                    }
                    case "DEF_1" -> {

                        AttributeInstance attributeInstance = player.getAttribute(Attributes.ARMOR);
                        attributeInstance.setBaseValue(attributeInstance.getBaseValue() - 1);
                    }
                    case "HP_1" -> {
                        AttributeInstance attributeInstance =
                                player.getAttribute(Attributes.MAX_HEALTH);
                        attributeInstance.setBaseValue(attributeInstance.getBaseValue() - 1);
                        player.setHealth(player.getHealth());
                    }
                    case "SPD_1" -> {
                        AttributeInstance attributeInstance =
                                player.getAttribute(Attributes.MOVEMENT_SPEED);
                        attributeInstance.setBaseValue(attributeInstance.getBaseValue() - 0.005);
                    }
                }
            } catch (Exception ignored) {
            }
        }
    }
}

