package de.mari_023.ae2wtlib;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import appeng.api.config.IncludeExclude;

public final class TextConstants {

    private TextConstants() {
    }

    public static final Style STYLE_RED = Style.EMPTY.withColor(ChatFormatting.RED);
    public static final Style STYLE_GREEN = Style.EMPTY.withColor(ChatFormatting.GREEN);
    public static final Style STYLE_GRAY = Style.EMPTY.withColor(ChatFormatting.GRAY);

    public static final MutableComponent RESTOCK_ON = new TranslatableComponent("gui.ae2wtlib.restock")
            .append(new TranslatableComponent("gui.ae2wtlib.on").setStyle(STYLE_GREEN));
    public static final MutableComponent RESTOCK_OFF = new TranslatableComponent("gui.ae2wtlib.restock")
            .append(new TranslatableComponent("gui.ae2wtlib.off").setStyle(STYLE_RED));

    public static final MutableComponent HOTKEY_MAGNETCARD_INVENTORY = new TranslatableComponent(
            "gui.ae2wtlib.magnetcard.hotkey")
                    .append(new TranslatableComponent("gui.ae2wtlib.magnetcard.desc.inv").setStyle(STYLE_GREEN));
    public static final MutableComponent HOTKEY_MAGNETCARD_ME = new TranslatableComponent(
            "gui.ae2wtlib.magnetcard.hotkey")
                    .append(new TranslatableComponent("gui.ae2wtlib.magnetcard.desc.me").setStyle(STYLE_GREEN));
    public static final MutableComponent HOTKEY_MAGNETCARD_OFF = new TranslatableComponent(
            "gui.ae2wtlib.magnetcard.hotkey")
                    .append(new TranslatableComponent("gui.ae2wtlib.magnetcard.desc.off").setStyle(STYLE_RED));
    public static final MutableComponent MAGNETCARD_OFF = new TranslatableComponent("gui.ae2wtlib.magnetcard")
            .append("\n").append(new TranslatableComponent("gui.ae2wtlib.magnetcard.desc.off"));
    public static final MutableComponent MAGNETCARD_INVENTORY = new TranslatableComponent("gui.ae2wtlib.magnetcard")
            .append("\n").append(new TranslatableComponent("gui.ae2wtlib.magnetcard.desc.inv"));
    public static final MutableComponent MAGNETCARD_ME = new TranslatableComponent("gui.ae2wtlib.magnetcard")
            .append("\n").append(new TranslatableComponent("gui.ae2wtlib.magnetcard.desc.me"));
    public static final TranslatableComponent MAGNETCARD_TOOLTIP = new TranslatableComponent(
            "item.ae2wtlib.magnet_card.desc");

    public static final MutableComponent UNIVERSAL = new TranslatableComponent(
            "item.ae2wtlib.wireless_universal_terminal.desc").withStyle(STYLE_GRAY);
    public static final MutableComponent CRAFTING = new TranslatableComponent("item.ae2.wireless_crafting_terminal")
            .withStyle(STYLE_GRAY);
    public static final MutableComponent PATTERN_ENCODING = new TranslatableComponent(
            "item.ae2wtlib.wireless_pattern_encoding_terminal").withStyle(STYLE_GRAY);
    public static final MutableComponent PATTERN_ACCESS = new TranslatableComponent(
            "item.ae2wtlib.wireless_pattern_access_terminal").withStyle(STYLE_GRAY);

    public static final TextComponent TERMINAL_EMPTY = new TextComponent(
            "This terminal does not contain any other Terminals");

    public static final TranslatableComponent BOOSTER = new TranslatableComponent(
            "item.ae2wtlib.infinity_booster_card.desc");

    public static final TranslatableComponent CYCLE = new TranslatableComponent("gui.ae2wtlib.cycle_terminal");
    public static final TranslatableComponent CYCLE_TOOLTIP = new TranslatableComponent(
            "gui.ae2wtlib.cycle_terminal.desc");

    public static final MutableComponent DELETE = new TranslatableComponent("gui.ae2wtlib.emptytrash").append("\n")
            .append(new TranslatableComponent("gui.ae2wtlib.emptytrash.desc"));

    public static final TranslatableComponent MAGNET_FILTER = new TranslatableComponent("gui.ae2wtlib.Magnet");

    private static final TranslatableComponent WHITELIST = new TranslatableComponent("gui.ae2wtlib.whitelist");
    private static final TranslatableComponent BLACKLIST = new TranslatableComponent("gui.ae2wtlib.blacklist");

    private static final TranslatableComponent PICKUP = new TranslatableComponent("gui.ae2wtlib.pickup");
    private static final TranslatableComponent INSERT = new TranslatableComponent("gui.ae2wtlib.insert");

    private static final TranslatableComponent FILTER = new TranslatableComponent("gui.ae2wtlib.filter");

    public static final MutableComponent PICKUP_WHITELIST = PICKUP.copy().append(FILTER).append(WHITELIST);
    public static final MutableComponent PICKUP_BLACKLIST = PICKUP.copy().append(FILTER).append(BLACKLIST);
    public static final MutableComponent INSERT_WHITELIST = INSERT.copy().append(FILTER).append(WHITELIST);
    public static final MutableComponent INSERT_BLACKLIST = INSERT.copy().append(FILTER).append(BLACKLIST);

    public static final MutableComponent COPY_PICKUP = new TranslatableComponent("gui.ae2wtlib.copy_pickup");
    public static final MutableComponent COPY_INSERT = new TranslatableComponent("gui.ae2wtlib.copy_insert");

    public static MutableComponent getPickupMode(IncludeExclude includeExclude) {
        return switch (includeExclude) {
            case WHITELIST -> PICKUP_WHITELIST;
            case BLACKLIST -> PICKUP_BLACKLIST;
        };
    }

    public static MutableComponent getInsertMode(IncludeExclude includeExclude) {
        return switch (includeExclude) {
            case WHITELIST -> INSERT_WHITELIST;
            case BLACKLIST -> INSERT_BLACKLIST;
        };
    }

    public static final TranslatableComponent SWITCH = new TranslatableComponent("gui.ae2wtlib.switch");
}
