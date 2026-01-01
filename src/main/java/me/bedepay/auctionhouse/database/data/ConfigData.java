package me.bedepay.auctionhouse.database.data;

public record ConfigData(AuctionSettingsData auctionSettingsData) {

    public record AuctionSettingsData(int min_price, int max_price, int time_to_expiration, String name) {

        public AuctionSettingsData {
            if (min_price < 0) {
                throw new IllegalArgumentException("The price " + min_price + " cannot be less than 0");
            }
        }
    }
}
