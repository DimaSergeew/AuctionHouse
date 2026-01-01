package me.bedepay.auctionhouse;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.bedepay.auctionhouse.GUI.AuctionGUI;
import me.bedepay.auctionhouse.command.AuctionCommand;
import me.bedepay.auctionhouse.database.StorageSystem;
import me.bedepay.auctionhouse.managers.ConfigManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;

public class AuctionHouse extends JavaPlugin {

    private static Economy economy;
    private static ConfigManager configManager;
    private static StorageSystem storageSystem;
    private static AuctionGUI auctionGUI;

    @Override
    public void onEnable() {
        PaperCommandManager<CommandSourceStack> cmdManager = PaperCommandManager.builder()
                .executionCoordinator(ExecutionCoordinator.simpleCoordinator())
                .buildOnEnable(this);
        AnnotationParser<CommandSourceStack> annotationParser = new AnnotationParser(cmdManager, this.getClass());
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            getLogger().warning("AuctionHouse requires Vault to be installed and enabled!");
            getServer().getPluginManager().disablePlugin(this);
        } else {
            economy = rsp.getProvider();
        }
        saveResource("messages.yml", false);
        saveDefaultConfig();
        configManager = new ConfigManager(this);
        storageSystem = new StorageSystem();
        annotationParser.parse(new AuctionCommand(this));
        getLogger().info("AuctionHouse has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("AuctionHouse has been disabled!");
    }

    public static Economy getEconomy() {
        return economy;
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }

    public static StorageSystem getStorageSystem() {
        return storageSystem;
    }

    public static AuctionGUI getAuctionGUI() {
        return auctionGUI;
    }

}
