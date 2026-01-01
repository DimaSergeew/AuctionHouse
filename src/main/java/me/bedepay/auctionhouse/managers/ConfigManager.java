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
            return;
        }
        int minPrice = configSection.getInt("min-price", 100);
        int maxPrice = configSection.getInt("max-price", 99999999);
        int time_to_expiration = configSection.getInt("time-to-expiration", 60000);
        String name = configSection.getString("name", "Аукцион");
        ConfigData.AuctionSettingsData auctionSettingsData
                = new ConfigData.AuctionSettingsData(minPrice, maxPrice, time_to_expiration, name);
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
