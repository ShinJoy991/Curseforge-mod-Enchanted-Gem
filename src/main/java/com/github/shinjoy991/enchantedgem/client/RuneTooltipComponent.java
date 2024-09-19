package com.github.shinjoy991.enchantedgem.client;

import com.github.shinjoy991.enchantedgem.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.joml.Matrix4f;

public class RuneTooltipComponent implements ClientTooltipComponent {

    private final RuneTooltipComponentData data;

    public RuneTooltipComponent(RuneTooltipComponentData data) {
        this.data = data;
    }

    private static Component getStringByRuneTypeName(String runeTypeName) {
        MutableComponent runeLore =
                Component.literal("Rune")
                        .setStyle(Style.EMPTY.withItalic(false).withBold(true)
                                .withUnderlined(true).withColor(TextColor.fromRgb(0xfc0362)));
        MutableComponent baseLore =
                Component.literal("").setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GREEN).withItalic(false).withUnderlined(false));
        baseLore = switch (runeTypeName) {
            case "ATK_1" -> baseLore.append(Component.literal(" | ")
                    .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0x02421B)))
                    .append(Component.literal("Penetrative Rune ")
                            .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0xCE76D6)).withBold(true))
                            .append(Component.literal("+" + formatPower(Config.RUNE_ATK_POWER) + " Damage")
                                    .setStyle(Style.EMPTY.withItalic(false).withBold(false).withColor(TextColor.fromRgb(0xFF0000))))));
            case "SHIELD_1" -> baseLore.append(Component.literal(" | ")
                    .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0x02421B)))
                    .append(Component.literal("Giant Rune ").withStyle()
                            .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0xCE76D6)).withBold(true))
                            .append(Component.literal("-" + formatPower(Config.RUNE_SHIELD_POWER) + " Damage taken")
                                    .setStyle(Style.EMPTY.withItalic(false).withBold(false).withColor(TextColor.fromRgb(0xADD8E6))))));
            case "RECOVER_1" -> baseLore.append(Component.literal(" | ")
                    .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0x02421B)))
                    .append(Component.literal("Blessing Rune ")
                            .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0xCE76D6)).withBold(true))
                            .append(Component.literal("Recover 1 health point when dealing more than " + formatPower(Config.RUNE_RECOVER_POWER) + " damage")
                                    .setStyle(Style.EMPTY.withItalic(false).withBold(false).withColor(ChatFormatting.YELLOW)))));
            case "RECOVER_VICTIM_1" -> baseLore.append(Component.literal(" | ")
                    .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0x02421B)))
                    .append(Component.literal("Vitality Rune ")
                            .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0xCE76D6)).withBold(true))
                            .append(Component.literal("Heal 1 point when taking more than " + formatPower(Config.RUNE_RECOVER_VICTIM_POWER) + " damage")
                                    .setStyle(Style.EMPTY.withItalic(false).withBold(false).withColor(ChatFormatting.GREEN)))));
            case "REGAIN_HUNGER_1" -> baseLore.append(Component.literal(" | ")
                    .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0x02421B)))
                    .append(Component.literal("Wild Rune ")
                            .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0xCE76D6)).withBold(true))
                            .append(Component.literal(formatPower(Config.RUNE_REGAIN_HUNGER_POWER) + "% chance fill 1 hunger point when " +
                                    "attacking").setStyle(Style.EMPTY.withItalic(false).withBold(false).withColor(ChatFormatting.GOLD)))));
            case "HUNGER_VICTIM_1" -> baseLore.append(Component.literal(" | ")
                    .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0x02421B)))
                    .append(Component.literal("Fierce Rune ")
                            .setStyle(Style.EMPTY.withItalic(false).withColor(TextColor.fromRgb(0xCE76D6)).withBold(true))
                            .append(Component.literal(formatPower(Config.RUNE_HUNGER_VICTIM_POWER) + "% chance to deplete 1 hunger point of the enemy")
                                    .setStyle(Style.EMPTY.withItalic(false).withBold(false).withColor(TextColor.fromRgb(0xFC6203))))));
            default -> baseLore;
        };

        return runeLore.append(baseLore);
    }

    static String formatPower(double value) {
        return value == Math.floor(value) ? String.valueOf((int) value) : String.valueOf(value);
    }

    @Override
    public int getHeight() {
        Component text = getStringByRuneTypeName(data.rune);
        String stringText = text.getString();
        int maxCharsPerLine = 40;
        int numberOfLines = (int) Math.ceil((double) stringText.length() / maxCharsPerLine);

        return numberOfLines * 10 + 2;
    }

    @Override
    public int getWidth(Font font) {
        int maxWidth = 0;
        for (FormattedCharSequence formattedcharsequence : font.split(getStringByRuneTypeName(data.rune), 220)) {
            int sequenceWidth = font.width(formattedcharsequence);
            maxWidth = Math.max(maxWidth, sequenceWidth);
        }
        return maxWidth + 5;
    }

    @Override
    public void renderText(Font pFont, int pX, int pY, Matrix4f pMatrix4f, MultiBufferSource.BufferSource pBufferSource) {
        for (FormattedCharSequence formattedcharsequence : pFont.split(getStringByRuneTypeName(data.rune), 220)) {
            pFont.drawInBatch(formattedcharsequence, pX, pY, 0xAABBCC, true, pMatrix4f, pBufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
            pY += 10;
        }
    }

    public record RuneTooltipComponentData(String rune) implements TooltipComponent {
    }
}
