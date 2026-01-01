package me.bedepay.auctionhouse.database;

import me.bedepay.auctionhouse.database.data.AuctionData;

import java.util.HashMap;

public class StorageSystem {
    private final HashMap<Short, AuctionData> itemLots = new HashMap<>();

    public void saveAction(Short itemLot, AuctionData auctionData) {
        itemLots.put(itemLot, auctionData);
    }

    public AuctionData getItemLot(Short itemLot) {
        return itemLots.get(itemLot);
    }

    public boolean containsItemLot(Short itemLot) {
        return itemLots.containsKey(itemLot);
    }

    public void removeItemLot(Short itemLot) {
        itemLots.remove(itemLot);
    }

    public int itemLotsSize() {
        return itemLots.size();
    }

    public HashMap<Short, AuctionData> getHashItemLots() {
        return itemLots;
    }
}
