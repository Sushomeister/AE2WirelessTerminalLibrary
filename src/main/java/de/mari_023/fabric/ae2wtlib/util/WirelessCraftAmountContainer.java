package de.mari_023.fabric.ae2wtlib.util;

import appeng.api.config.SecurityPermissions;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.crafting.ICraftingGrid;
import appeng.api.networking.crafting.ICraftingJob;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.IActionSource;
import appeng.api.storage.ITerminalHost;
import appeng.api.storage.data.IAEItemStack;
import appeng.container.AEBaseContainer;
import appeng.container.ContainerLocator;
import appeng.container.ContainerOpener;
import appeng.container.SlotSemantic;
import appeng.container.guisync.GuiSync;
import appeng.container.me.crafting.WirelessCraftConfirmContainer;
import appeng.container.slot.InaccessibleSlot;
import appeng.core.AELog;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.ConfirmAutoCraftPacket;
import appeng.me.helpers.PlayerSource;
import appeng.tile.inventory.AppEngInternalInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;

import java.util.concurrent.Future;

public class WirelessCraftAmountContainer extends AEBaseContainer {

    public static ScreenHandlerType<WirelessCraftAmountContainer> TYPE;

    private static final ContainerHelper<WirelessCraftAmountContainer, ITerminalHost> helper = new ContainerHelper<>(
            WirelessCraftAmountContainer::new, ITerminalHost.class);

    /**
     * This slot is used to synchronize a visual representation of what is to be crafted to the client.
     */
    private final Slot craftingItem;

    /**
     * This item (server-only) indicates what should actually be crafted.
     */
    private IAEItemStack itemToCreate;

    @GuiSync(1)
    private int initialAmount = -1;


    public WirelessCraftAmountContainer(int id, PlayerInventory ip, final ITerminalHost te) {
        super(TYPE, id, ip, te);
        this.craftingItem = new InaccessibleSlot(new AppEngInternalInventory(null, 1), 0);
        this.addSlot(this.craftingItem, SlotSemantic.MACHINE_OUTPUT);
    }

    @Override
    public void sendContentUpdates() {
        super.sendContentUpdates();
        this.verifyPermissions(SecurityPermissions.CRAFT, false);
    }

    public IGrid getGrid() {
        final IActionHost h = (IActionHost) this.getTarget();
        return h.getActionableNode().getGrid();
    }

    public World getWorld() {
        return this.getPlayerInventory().player.world;
    }

    public IActionSource getActionSrc() {
        return new PlayerSource(this.getPlayerInventory().player, (IActionHost) this.getTarget());
    }

    private void setItemToCraft(final IAEItemStack itemToCreate, int initialAmount) {
        // Make a copy because this stack will be modified with the requested amount
        this.itemToCreate = itemToCreate.copy();
        this.initialAmount = initialAmount;
        this.craftingItem.setStack(itemToCreate.asItemStackRepresentation());
    }

    /**
     * Confirms the craft request. If called client-side, automatically sends a packet to the server to perform the
     * action there instead.
     *
     * @param amount    The number of items to craft.
     * @param autoStart Start crafting immediately when the planning is done.
     */
    public void confirm(int amount, boolean autoStart) {
        if(!isServer()) {
            NetworkHandler.instance().sendToServer(new ConfirmAutoCraftPacket(amount, autoStart));
            return;
        }

        final Object target = getTarget();
        if(target instanceof IActionHost) {
            final IActionHost ah = (IActionHost) target;
            final IGridNode gn = ah.getActionableNode();
            if(gn == null) return;

            final IGrid g = gn.getGrid();
            if(g == null || this.itemToCreate == null) return;

            this.itemToCreate.setStackSize(amount);

            Future<ICraftingJob> futureJob = null;
            try {
                final ICraftingGrid cg = g.getCache(ICraftingGrid.class);
                futureJob = cg.beginCraftingJob(getWorld(), getGrid(), getActionSrc(),
                        this.itemToCreate, null);

                final ContainerLocator locator = getLocator();
                if(locator != null) {
                    PlayerEntity player = this.getPlayerInventory().player;
                    ContainerOpener.openContainer(WirelessCraftConfirmContainer.TYPE, player, locator);

                    if(player.currentScreenHandler instanceof WirelessCraftConfirmContainer) {
                        final WirelessCraftConfirmContainer ccc = (WirelessCraftConfirmContainer) player.currentScreenHandler;
                        ccc.setAutoStart(autoStart);
                        ccc.setItemToCreate(this.itemToCreate.copy());
                        ccc.setJob(futureJob);
                        sendContentUpdates();
                    }
                }
            } catch(final Throwable e) {
                if(futureJob != null) futureJob.cancel(true);
                AELog.info(e);
            }
        }
    }

    public int getInitialAmount() {
        return initialAmount;
    }
}