package com.github.shinjoy991.enchantedgem.event;

import com.github.shinjoy991.enchantedgem.Config;
import com.github.shinjoy991.enchantedgem.EnchantedGem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = EnchantedGem.MODID)
public class RuneEffectHandler {

    @SubscribeEvent
    public static void onLivingHurtRune(LivingHurtEvent event) {
        LivingEntity victim = event.getEntity();
        if (victim == null)
            return;
        LivingEntity attacker = null;
        try {
            attacker = (LivingEntity) event.getSource().getEntity();
        } catch (Exception e) {
            return;
        }
        if (attacker == null)
            return;
        // attacker
        List<ItemStack> iterable = new ArrayList<>();
        iterable.add(attacker.getMainHandItem());
        for (ItemStack armorStack : attacker.getArmorSlots()) {
            iterable.add(armorStack);
        }
        for (ItemStack itemstack : iterable) {
            String runeType = getRuneEffect(itemstack);
            switch (runeType) {
                case "ATK_1" -> event.setAmount((float) (event.getAmount() + Config.RUNE_ATK_POWER));
                case "RECOVER_1" -> {
                    if (event.getAmount() > Config.RUNE_RECOVER_POWER) {
                        attacker.heal(1);
                    }
                }
                case "REGAIN_HUNGER_1" -> {
                    if (attacker instanceof ServerPlayer player) {
                        if (Math.random() < Config.RUNE_REGAIN_HUNGER_POWER * 0.01) {
                            player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() + 1);
                        }
                    }
                }
                case "HUNGER_VICTIM_1" -> {
                    if (victim instanceof ServerPlayer player) {
                        if (Math.random() < Config.RUNE_HUNGER_VICTIM_POWER * 0.01) {
                            player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() - 1);
                        }
                    } else {
                        if (Math.random() < Config.RUNE_HUNGER_VICTIM_POWER * 0.01) {
                            MobEffectInstance slowness =
                                    new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0);
                            victim.addEffect(slowness);
                        }
                    }
                }
            }
        }
        // victim
        List<ItemStack> iterablevictim = new ArrayList<>();
        iterablevictim.add(victim.getMainHandItem());
        for (ItemStack armorStack : victim.getArmorSlots()) {
            iterablevictim.add(armorStack);
        }
        for (ItemStack itemstack : iterablevictim) {
            String runeType = getRuneEffect(itemstack);
            switch (runeType) {
                case "SHIELD_1" -> event.setAmount((float) (event.getAmount() - Config.RUNE_SHIELD_POWER));
                case "RECOVER_VICTIM_1" -> {
                    if (event.getAmount() > Config.RUNE_RECOVER_VICTIM_POWER) {
                        victim.heal(1);
                    }
                }
            }
        }
    }
    private static String getRuneEffect(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains("ENCHANTEDGEM_RUNE")) {
            return "NONE";
        }
        return tag.getString("ENCHANTEDGEM_RUNE");
    }
}
