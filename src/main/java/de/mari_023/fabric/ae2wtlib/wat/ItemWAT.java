package de.mari_023.fabric.ae2wtlib.wat;

import appeng.core.AEConfig;
import appeng.menu.MenuLocator;
import appeng.menu.MenuOpener;
import de.mari_023.fabric.ae2wtlib.AE2wtlib;
import de.mari_023.fabric.ae2wtlib.terminal.ItemWT;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.player.PlayerEntity;

public class ItemWAT extends ItemWT {

    public ItemWAT() {
        super(AEConfig.instance().getWirelessTerminalBattery(), new FabricItemSettings().group(AE2wtlib.ITEM_GROUP).maxCount(1));
    }

    @Override
    public void open(final PlayerEntity player, final MenuLocator locator) {
        MenuOpener.open(WATMenu.TYPE, player, locator);
    }
}