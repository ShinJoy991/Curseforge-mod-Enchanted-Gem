package com.github.shinjoy991.enchantedgem.client;

import com.github.shinjoy991.enchantedgem.EnchantedGem;
import com.mojang.datafixers.util.Either;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

public class RendererRegister {

    @SubscribeEvent
    public static void onTooltipGatherComponents(RenderTooltipEvent.GatherComponents event) {
        ItemStack stack = event.getItemStack();
        if (stack.hasTag() && stack.getTag().contains("ENCHANTED_ELEMENTAL_SPIRIT")) {

            int insertPosition = event.getTooltipElements().size();
            for (int i = event.getTooltipElements().size() - 1; i >= 1; i--) {
                String loopString = event.getTooltipElements().get(i).toString();
                if (loopString.contains("key='item.nbt_tags'")) {
                    if (event.getTooltipElements().get(i - 1).toString().contains("color=dark_gray")) {
                        insertPosition = i - 1;
                    }
                }

                if (loopString.startsWith("Left[translation{key='item.modifiers")) {
                    insertPosition = i - 1;
                    break;
                }
            }

            TooltipComponent componentSpirit = new SpiritTooltipComponent.SpiritTooltipComponentData(stack.getTag().getString("ENCHANTED_ELEMENTAL_SPIRIT"));
            if (stack.hasTag() && stack.getTag().contains("ENCHANTEDGEM_RUNE")) {
                TooltipComponent componentRune = new RuneTooltipComponent.RuneTooltipComponentData(stack.getTag().getString("ENCHANTEDGEM_RUNE"));
                event.getTooltipElements().add(insertPosition, Either.right(componentRune));
            }
            event.getTooltipElements().add(insertPosition, Either.right(componentSpirit));

        } else if (stack.hasTag() && stack.getTag().contains("ENCHANTEDGEM_RUNE")) {
            int insertPosition = event.getTooltipElements().size() ;
            for (int i = event.getTooltipElements().size() - 1; i >= 1; i--) {
                String loopString = event.getTooltipElements().get(i).toString();
                if (loopString.contains("key='item.nbt_tags'")) {
                    if (event.getTooltipElements().get(i - 1).toString().contains("color=dark_gray")) {
                        insertPosition = i - 1;
                    }
                }
                if (loopString.startsWith("Left[translation{key='item.modifiers")) {
                    insertPosition = i - 1 ;
                    break;
                }
            }
            TooltipComponent componentRune = new RuneTooltipComponent.RuneTooltipComponentData(stack.getTag().getString("ENCHANTEDGEM_RUNE"));
            event.getTooltipElements().add(insertPosition, Either.right(componentRune));
        }

        if (stack.hasTag() && stack.getTag().contains("GEM_INLAY_COUNT")) {
            int gemCount = stack.getTag().getInt("GEM_INLAY_COUNT");

            List<String> gemTypes = new ArrayList<>();
            for (int i = 1; i <= gemCount; i++) {
                gemTypes.add(stack.getTag().getString("GEM_INLAY_" + i));
            }

            int insertPosition = event.getTooltipElements().size();
            for (int i = event.getTooltipElements().size() - 1; i >= 1; i--) {
                String loopString = event.getTooltipElements().get(i).toString();
                if (loopString.contains("key='item.nbt_tags'")) {
                    if (event.getTooltipElements().get(i - 1).toString().contains("color=dark_gray")) {
                        insertPosition = i - 1;
                    }
                }
                if (loopString.startsWith("Left[translation{key='item.modifiers")) {
                    insertPosition = i - 1;
                    break;
                }
            }

            TooltipComponent component = new GemTooltipComponent.GemTooltipComponentData(gemTypes);
            event.getTooltipElements().add(insertPosition, Either.right(component));
        }
    }

    @Mod.EventBusSubscriber(modid = EnchantedGem.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBusSub {
        @SubscribeEvent
        public static void registerTooltipComponents(RegisterClientTooltipComponentFactoriesEvent event) {
            event.register(GemTooltipComponent.GemTooltipComponentData.class, GemTooltipComponent::new);
            event.register(RuneTooltipComponent.RuneTooltipComponentData.class, RuneTooltipComponent::new);
            event.register(SpiritTooltipComponent.SpiritTooltipComponentData.class, SpiritTooltipComponent::new);
        }
    }
}
