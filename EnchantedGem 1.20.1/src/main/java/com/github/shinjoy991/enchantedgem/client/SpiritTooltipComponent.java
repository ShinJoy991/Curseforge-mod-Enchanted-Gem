package com.github.shinjoy991.enchantedgem.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.joml.Matrix4f;

public class SpiritTooltipComponent implements ClientTooltipComponent {

    private final SpiritTooltipComponentData data;

    public SpiritTooltipComponent(SpiritTooltipComponentData data) {
        this.data = data;
    }

    private static Component getStringBySpiritName(String SpiritName) {
        MutableComponent baseLore =
                Component.literal("Elemental Spirit")
                        .setStyle(Style.EMPTY.withItalic(false).withBold(true).withUnderlined(true).withColor(TextColor.fromRgb(0xfc0362)))
                        .append(Component.literal(" | ")
                                .setStyle(Style.EMPTY.withItalic(false).withBold(true).withUnderlined(false).withColor(TextColor.fromRgb(0x02421B))));
        baseLore = switch (SpiritName) {
            case "FIRE_SPIRIT" -> baseLore
                    .append(Component.literal("Fire")
                            .setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.RED).withBold(false).withUnderlined(false)));
            case "WATER_SPIRIT" -> baseLore
                    .append(Component.literal("Water").withStyle()
                            .setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.DARK_BLUE).withBold(false).withUnderlined(false)));
            case "WOOD_SPIRIT" -> baseLore
                    .append(Component.literal("Wood")
                            .setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.DARK_GREEN).withBold(false).withUnderlined(false)));
            case "LIGHT_SPIRIT" -> baseLore
                    .append(Component.literal("Light")
                            .setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.YELLOW).withBold(false).withUnderlined(false)));
            case "DARK_SPIRIT" -> baseLore
                    .append(Component.literal("Dark")
                            .setStyle(Style.EMPTY.withItalic(false).withColor(ChatFormatting.DARK_PURPLE).withBold(false).withUnderlined(false)));
            default -> baseLore;
        };
        return baseLore;
    }

    @Override
    public int getHeight() {
        return 12;
    }

    @Override
    public int getWidth(Font font) {
        return Math.max(0, Math.min(220, font.width(getStringBySpiritName(data.spirit)) + 10));
    }

    @Override
    public void renderText(Font pFont, int pX, int pY, Matrix4f pMatrix4f, MultiBufferSource.BufferSource pBufferSource) {
        pFont.drawInBatch(getStringBySpiritName(data.spirit), pX, pY, 0xAABBCC, true, pMatrix4f, pBufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
    }

    public record SpiritTooltipComponentData(String spirit) implements TooltipComponent {
    }
}
