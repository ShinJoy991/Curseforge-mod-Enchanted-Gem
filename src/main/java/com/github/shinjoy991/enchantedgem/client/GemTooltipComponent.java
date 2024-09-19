package com.github.shinjoy991.enchantedgem.client;

import com.github.shinjoy991.enchantedgem.Config;
import com.github.shinjoy991.enchantedgem.EnchantedGem;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.joml.Matrix4f;

import java.util.List;

public class GemTooltipComponent implements ClientTooltipComponent {

    private final ResourceLocation gemSlotTexture = new ResourceLocation(EnchantedGem.MODID, "textures/gui/gemslot.png");;
    private final int width = 16;
    private final int height = 16;
    private final int spacing = Minecraft.getInstance().font.lineHeight + 4;
    private final GemTooltipComponentData data;

    public GemTooltipComponent(GemTooltipComponentData data) {
        this.data = data;
    }

    private static Component getStringByGemTypeName(String gemTypeName) {
        return switch (gemTypeName) {
            case "ATK_1" -> Component.literal("+" + formatPower(Config.GEM_ATK_POWER) + " ATK").setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0xFF0000)));
            case "DEF_1" -> Component.literal("+" + formatPower(Config.GEM_DEF_POWER) + " DEF").setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0xADD8E6)));
            case "HP_1" -> Component.literal("+" + formatPower(Config.GEM_HP_POWER) + " HP").setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.GREEN));
            case "SPD_1" -> Component.literal("+" + formatPower(Config.GEM_SPD_POWER) + " SPD").setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.YELLOW));
            case "CRITDMG_1" -> Component.literal("+" + formatPower(Config.GEM_CRITDMG_POWER) + "% Crit DMG").setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0xFC6203)));
            default -> Component.literal("null");
        };
    }

    static String formatPower(double value) {
        return value == Math.floor(value) ? String.valueOf((int) value) : String.valueOf(value);
    }

    public static ResourceLocation getGemTextureByType(String gemType) {
        return switch (gemType) {
            case "ATK_1" -> new ResourceLocation("enchantedgem", "textures/item/enchanted_gem_atk1.png");
            case "DEF_1" -> new ResourceLocation("enchantedgem", "textures/item/enchanted_gem_def1.png");
            case "HP_1" -> new ResourceLocation("enchantedgem", "textures/item/enchanted_gem_hp1.png");
            case "SPD_1" -> new ResourceLocation("enchantedgem", "textures/item/enchanted_gem_spd1.png");
            case "CRITDMG_1" -> new ResourceLocation("enchantedgem", "textures/item/enchanted_gem_critdmg1.png");
            default -> null;
        };
    }

    @Override
    public int getHeight()  {
        return this.spacing * this.data.gems.size();
    }

    @Override
    public int getWidth(Font font) {
        int maxWidth = 0;
        for (String gemTypeName : data.gems()) {
            maxWidth = Math.max(maxWidth, font.width(getStringByGemTypeName(gemTypeName)) + 24);
        }
        return maxWidth;
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        int index = 0;
        for (String gemtype : data.gems) {
            RenderSystem.setShaderTexture(0, getGemTextureByType(gemtype));
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(x - 3 , y + (spacing * index), 0);
            guiGraphics.pose().scale(0.6f, 0.6f, 1.0f);
            guiGraphics.blit(gemSlotTexture, 0, 0, 0, 0, 24, 17, 24,  17);
            guiGraphics.blit(getGemTextureByType(gemtype), 7, 0, 0, 0, width, height, width, height);
            guiGraphics.pose().popPose();
            index++;
        }
    }

    @Override
    public void renderText(Font pFont, int pX, int pY, Matrix4f pMatrix4f, MultiBufferSource.BufferSource pBufferSource) {
        int index = 0;
        for (String gemtype : data.gems) {
            pFont.drawInBatch(getStringByGemTypeName(gemtype), pX + 16, pY + 2 + (this.spacing * index), 0xAABBCC, true, pMatrix4f, pBufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
            index++;
        }
    }

    public record GemTooltipComponentData(List<String> gems) implements TooltipComponent {
    }
}
