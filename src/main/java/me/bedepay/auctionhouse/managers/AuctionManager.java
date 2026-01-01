package me.bedepay.auctionhouse.managers;

import io.papermc.paper.persistence.PersistentDataContainerView;
import me.bedepay.auctionhouse.AuctionHouse;
import me.bedepay.auctionhouse.database.data.AuctionData;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class AuctionManager {

    private final Random RANDOM = new Random();

    public void managerData(Player player, long price, AuctionHouse plugin) {
        ItemStack playerItem = player.getInventory().getItemInMainHand();
        ItemMeta metaItem = playerItem.getItemMeta();
        if (playerItem.getType() == Material.AIR) {
            player.sendRichMessage("<color:#ff2317>Ты не можешь продать воздух :)</color>");
            return;
        }
        UUID uuid = player.getUniqueId();
        List<Component> originalLore = metaItem.lore();
        metaItem.lore(List.of(Component.text("Владелец: " + player.getName() + " " + "Цена: " + price + " $")));
        playerItem.setItemMeta(metaItem);
        //random itemLot
        short itemLot = (short) RANDOM.nextInt(Short.MAX_VALUE);
        while (AuctionHouse.getStorageSystem().containsItemLot(itemLot)) {
            itemLot = (short) RANDOM.nextInt(Short.MAX_VALUE);
        }
        final short ITEM_LOT = itemLot;
        playerItem.editPersistentDataContainer((pdc) -> {
            pdc.set(new NamespacedKey(plugin, "auctionhouse_itemLot"), PersistentDataType.SHORT, ITEM_LOT);
        });
        AuctionHouse.getStorageSystem().saveAction(ITEM_LOT, new AuctionData(playerItem, price, uuid, originalLore, ITEM_LOT));
        player.sendRichMessage("<color:#00ff00>Товар успешно выставлен на продажу!</color>");
    }

    public AuctionData getAuctionData(ItemStack clickItem) {
        if (clickItem == null || clickItem.getType() == Material.AIR) {
            return null;
        }
        NamespacedKey keyLot = new NamespacedKey(AuctionHouse.getPlugin(AuctionHouse.class), "auctionhouse_itemLot");
        PersistentDataContainerView pdc = clickItem.getPersistentDataContainer();
        if (!pdc.has(keyLot)) {
            return null;
        }
        Short itemLot = clickItem.getPersistentDataContainer().get(keyLot, PersistentDataType.SHORT);
        if (itemLot == null) {
            return null;
        }
        return AuctionHouse.getStorageSystem().getItemLot(itemLot);
    }

    public void buyAuction(Player player, AuctionData auctionData) {
        Economy economy = AuctionHouse.getEconomy();
//        if (player.getUniqueId().equals(auctionData.seller())) {
//            player.sendRichMessage("<red>Ты не можешь купить свой товар</red>");
//            return;
//        }
        if (economy.getBalance(player) < auctionData.price()) {
            player.sendRichMessage("<red>У тебя недостаточно средств</red>");
            return;
        }
        economy.withdrawPlayer(player, (double) auctionData.price());
        ItemStack buyItem = auctionData.itemStack();
        ItemMeta buyItemMeta = buyItem.getItemMeta();
        if (buyItemMeta.lore() != null) {
            buyItemMeta.lore(auctionData.originalLore());
        }
        buyItem.setItemMeta(buyItemMeta);
        buyItem.editPersistentDataContainer((pdc) -> {
            pdc.remove(new NamespacedKey(AuctionHouse.getPlugin(AuctionHouse.class), "auctionhouse_itemLot"));
        });
        player.give(buyItem);
        player.sendRichMessage("<color:#00ff00>Товар успешно куплен!</color>");
        AuctionHouse.getStorageSystem().removeItemLot(auctionData.itemLot());
        AuctionHouse.getAuctionGUI().openMenu(player);
    }
}