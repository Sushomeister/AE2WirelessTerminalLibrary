package de.mari_023.fabric.ae2wtlib.wct;

import appeng.api.config.Actionable;
import appeng.api.config.SortDir;
import appeng.api.config.SortOrder;
import appeng.api.config.ViewItems;
import appeng.api.features.ILocatable;
import appeng.api.util.IConfigManager;
import appeng.container.ContainerLocator;
import appeng.core.AEConfig;
import appeng.core.Api;
import appeng.core.localization.GuiText;
import appeng.core.localization.PlayerMessages;
import appeng.util.ConfigManager;
import appeng.util.Platform;
import de.mari_023.fabric.ae2wtlib.IInfinityBoosterCardHolder;
import de.mari_023.fabric.ae2wtlib.ItemWT;
import de.mari_023.fabric.ae2wtlib.ae2wtlib;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;

public class ItemWCT extends ItemWT implements IInfinityBoosterCardHolder {

    public ItemWCT() {
        super(AEConfig.instance().getWirelessTerminalBattery(), new FabricItemSettings().group(ae2wtlib.ITEM_GROUP).maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(final World w, final PlayerEntity player, final Hand hand) {
        openWirelessTerminalGui(player.getStackInHand(hand), player, hand);
        return new TypedActionResult<>(ActionResult.SUCCESS, player.getStackInHand(hand));
    }

    private void openWirelessTerminalGui(ItemStack item, PlayerEntity player, Hand hand) {
        if(Platform.isClient()) {
            return;
        }

        final String unparsedKey = getEncryptionKey(item);
        if(unparsedKey.isEmpty()) {
            player.sendSystemMessage(PlayerMessages.DeviceNotLinked.get(), Util.NIL_UUID);
            return;
        }

        final long parsedKey = Long.parseLong(unparsedKey);
        final ILocatable securityStation = Api.instance().registries().locatable().getLocatableBy(parsedKey);
        if(securityStation == null) {
            player.sendSystemMessage(PlayerMessages.StationCanNotBeLocated.get(), Util.NIL_UUID);
            return;
        }

        if(hasPower(player, 0.5, item)) {
            WCTContainer.open(player, ContainerLocator.forHand(player, hand));
        } else {
            player.sendSystemMessage(PlayerMessages.DeviceNotPowered.get(), Util.NIL_UUID);
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(final ItemStack stack, final World world, final List<Text> lines, final TooltipContext advancedTooltips) {
        super.appendTooltip(stack, world, lines, advancedTooltips);

        if(stack.hasTag()) {
            final CompoundTag tag = stack.getOrCreateTag();
            if(tag != null) {
                final String encKey = tag.getString("encryptionKey");

                if(encKey == null || encKey.isEmpty()) {
                    lines.add(GuiText.Unlinked.text());
                } else {
                    lines.add(GuiText.Linked.text());
                }
            }
        } else {
            lines.add(new TranslatableText("AppEng.GuiITooltip.Unlinked"));
        }
    }

    @Override
    public boolean canHandle(ItemStack is) {
        return is.getItem() instanceof ItemWCT;
    }

    @Override
    public boolean usePower(PlayerEntity player, double amount, ItemStack is) {
        return extractAEPower(is, amount, Actionable.MODULATE) >= amount - 0.5;
    }

    @Override
    public boolean hasPower(PlayerEntity player, double amount, ItemStack is) {
        return getAECurrentPower(is) >= amount;
    }

    @Override
    public IConfigManager getConfigManager(ItemStack is) {
        final ConfigManager out = new ConfigManager((manager, settingName, newValue) -> {
            final CompoundTag data = is.getOrCreateTag();
            manager.writeToNBT(data);
        });

        out.registerSetting(appeng.api.config.Settings.SORT_BY, SortOrder.NAME);
        out.registerSetting(appeng.api.config.Settings.VIEW_MODE, ViewItems.ALL);
        out.registerSetting(appeng.api.config.Settings.SORT_DIRECTION, SortDir.ASCENDING);

        out.readFromNBT(is.getOrCreateTag().copy());
        return out;
    }

    @Override
    public String getEncryptionKey(ItemStack item) {
        final CompoundTag tag = item.getOrCreateTag();
        return tag.getString("encryptionKey");
    }

    @Override
    public void setEncryptionKey(ItemStack item, String encKey, String name) {
        final CompoundTag tag = item.getOrCreateTag();
        tag.putString("encryptionKey", encKey);
        tag.putString("name", name);
    }

    @Override
    public boolean hasBoosterCard(ItemStack item) {
        return item.getItem() instanceof IInfinityBoosterCardHolder && item.getTag() != null && item.getTag().getBoolean("hasboostercard");
    }

    @Override
    public void setBoosterCard(ItemStack item, boolean hasBoosterCard) {
        if(item.getItem() instanceof IInfinityBoosterCardHolder && item.getTag() != null && hasBoosterCard(item) != hasBoosterCard) {
            item.getTag().putBoolean("hasboostercard", hasBoosterCard);
        }
    }

    @Override
    public ItemStack boosterCard(ItemStack item) {
        if(hasBoosterCard(item)) return new ItemStack(ae2wtlib.INFINITY_BOOSTER);
        return ItemStack.EMPTY;
    }

    public ItemStack getMagnetCard(ItemStack item) {
        if(!(item.getItem() instanceof ItemWCT) || item.getTag() == null) return ItemStack.EMPTY;
        return ItemStack.fromTag(item.getTag().getCompound("magnet_card"));
    }

    public void setMagnetCard(ItemStack item, ItemStack magnet_card) {
        if(!(item.getItem() instanceof ItemWCT)) return;
        CompoundTag wctTag = item.getTag();
        if(magnet_card.isEmpty()) {
            if(wctTag == null) return;
            wctTag.put("magnet_card", ItemStack.EMPTY.toTag(new CompoundTag()));
        } else {
            if(wctTag == null) wctTag = new CompoundTag();
            wctTag.put("magnet_card", magnet_card.toTag(new CompoundTag()));
        }
        item.setTag(wctTag);
    }
}