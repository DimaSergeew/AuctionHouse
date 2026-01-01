package me.bedepay.auctionhouse.managers;

import me.bedepay.auctionhouse.AuctionHouse;
import me.bedepay.auctionhouse.database.data.ConfigData;
import org.bukkit.configuration.ConfigurationSection;

public final class ConfigManager {

    private ConfigData data;

    private final AuctionHouse plugin;

    public ConfigManager(AuctionHouse plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {
        ConfigurationSection configSection = plugin.getConfig().getConfigurationSection("AuctionSettings");
        if (configSection == null) {
            throw new IllegalArgumentException("AuctionSettings not found in config.yml");
        }
        int minPrice = configSection.getInt("min-price", 100);
        int maxPrice = configSection.getInt("max-price", 99_999_999);
        int timeExpiration = configSection.getInt("time-expiration", 60_000);
        String name = configSection.getString("name", "Аукцион");
        if (minPrice < 0) {
            throw new IllegalArgumentException("The price " + minPrice + " cannot be less than 0");
        }
        if (maxPrice < minPrice) {
            throw new IllegalArgumentException(
                    "The max price " + maxPrice + " cannot be less than min price " + minPrice);
        }
        ConfigData.AuctionSettingsData auctionSettingsData =
                new ConfigData.AuctionSettingsData(minPrice, maxPrice, timeExpiration, name);
        data = new ConfigData(auctionSettingsData);
    }

    public void reload() {
        plugin.reloadConfig();
        load();
    }

    public ConfigData getData() {
        return data;
    }
}
