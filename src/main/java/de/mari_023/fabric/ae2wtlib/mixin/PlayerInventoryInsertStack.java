package de.mari_023.fabric.ae2wtlib.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import appeng.api.config.Actionable;
import appeng.api.stacks.AEItemKey;
import appeng.me.helpers.PlayerSource;

import de.mari_023.fabric.ae2wtlib.wct.CraftingTerminalHandler;
import de.mari_023.fabric.ae2wtlib.wct.magnet_card.MagnetHandler;
import de.mari_023.fabric.ae2wtlib.wct.magnet_card.MagnetMode;

@Mixin(Inventory.class)
public class PlayerInventoryInsertStack {

    @Shadow
    @Final
    public Player player;

    @Inject(method = "add(Lnet/minecraft/world/item/ItemStack;)Z", at = @At(value = "HEAD"), cancellable = true)
    public void insertStackInME(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.isEmpty())
            return;
        CraftingTerminalHandler cTHandler = CraftingTerminalHandler.getCraftingTerminalHandler(player);
        ItemStack terminal = cTHandler.getCraftingTerminal();
        if (MagnetHandler.getMagnetSettings(terminal).magnetMode == MagnetMode.PICKUP_ME && cTHandler.inRange()) {
            if (cTHandler.getTargetGrid().getStorageService() == null)
                return;
            long inserted = cTHandler.getTargetGrid().getStorageService().getInventory().insert(AEItemKey.of(stack),
                    stack.getCount(), Actionable.MODULATE, new PlayerSource(player, cTHandler.getSecurityStation()));
            int leftover = (int) (stack.getCount() - inserted);
            if (leftover == 0) {
                stack.setCount(0);
                cir.setReturnValue(true);
            } else
                stack.setCount(leftover);
        }
    }
}
