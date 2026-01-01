package me.bedepay.auctionhouse.util;

import me.bedepay.auctionhouse.AuctionHouse;

public class PathShortening {

    public static int getMin_Price() {
        return AuctionHouse.getConfigManager().getData().auctionSettingsData().min_price();
    }

    public static int getMax_Price() {
        return AuctionHouse.getConfigManager().getData().auctionSettingsData().max_price();
    }

    public static int time_to_expiration() {
        return AuctionHouse.getConfigManager().getData().auctionSettingsData().time_to_expiration();
    }

    public static String getName() {
        return AuctionHouse.getConfigManager().getData().auctionSettingsData().name();
    }
}
