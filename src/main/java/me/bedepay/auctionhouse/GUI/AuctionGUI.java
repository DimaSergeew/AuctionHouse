package me.bedepay.auctionhouse.GUI;

import dev.masmc05.invapi.api.InventoryApi;
import dev.masmc05.invapi.api.slot.Slot;
import dev.masmc05.invapi.api.view.InventoryViewConstructor;
import me.bedepay.auctionhouse.AuctionHouse;
import me.bedepay.auctionhouse.database.StorageSystem;
import me.bedepay.auctionhouse.database.data.AuctionData;
import me.bedepay.auctionhouse.util.PathShortening;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

public class AuctionGUI implements InventoryViewConstructor {

    private final Inventory ahInventory = InventoryApi.createFlexibleInventory(9 * 6);
    private final Player player;

    public AuctionGUI(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("player null AuctionGUI construct");
        }
        this.player = player;
        StorageSystem storageSystem = AuctionHouse.getStorageSystem();
        if (storageSystem == null) {
            throw new IllegalArgumentException("storageSystem null AuctionGUI construct");
        }
        for (AuctionData auctionData : storageSystem.getHashItemLots().values()) {
            if (auctionData == null
                    || auctionData.itemStack() == null
                    || auctionData.itemStack().getType() == Material.AIR) {
                continue;
            }
            ahInventory.addItem(auctionData.itemStack());
        }
    }

    @Override
    public @NotNull Inventory getTopInventory() {
        return ahInventory;
    }

    @Override
    public @NotNull PlayerInventory getPlayerInventory() {
        return player.getInventory();
    }

    @Override
    public int getTopSize() {
        return ahInventory.getSize();
    }

    @Override
    public void openMenu(Player player) {
        InventoryApi.createAndOpen(player, this);
    }

    @Override
    public @NotNull Component title() {
        return Component.text(PathShortening.getName(), NamedTextColor.GOLD);
    }

    @Override
    public @NotNull Slot createTopSlot(int index) {
        return new Slot() {
            @Override
            public @NotNull Inventory inventory() {
                return ahInventory;
            }

            @Override
            public int index() {
                return index;
            }

            @Override
            public boolean canPlace(@NotNull ItemStack itemStack) {
                return false;
            }

            @Override
            public boolean canTake() {
                return false;
            }

            @Override
            public void changed() {
                ItemStack currentItem = ahInventory.getItem(index);
                if (currentItem == null || currentItem.getType() == Material.AIR) {
                    return;
                }
                Short itemLot = AuctionHouse.getAuctionManager().getAuctionData(currentItem);
                if (itemLot == null) {
                    return;
                }
                AuctionHouse.getAuctionManager().buyAuction(player, itemLot);
            }
        };
    }

    public static AuctionGUI getGUI(Player player) {
        return new AuctionGUI(player);
    }
}
