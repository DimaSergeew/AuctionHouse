package me.bedepay.auctionhouse.command;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.bedepay.auctionhouse.AuctionHouse;
import me.bedepay.auctionhouse.GUI.AuctionGUI;
import me.bedepay.auctionhouse.util.PathShortening;
import org.bukkit.entity.Player;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.Default;

public class AuctionCommand {
    private final AuctionHouse plugin;

    public AuctionCommand(AuctionHouse plugin) {
        this.plugin = plugin;
    }

    @Command("ah|auction")
    public void onCommand(CommandSourceStack css) {
        if (!(css.getSender() instanceof Player player)) {
            return;
        }
        AuctionGUI.getGUI(player).openMenu(player);
    }

    @Command("ah|auction sell [price]")
    public void onSellCommand(CommandSourceStack css, @Default("100") @Argument("price") double price) {
        if (!(css.getSender() instanceof Player player)) {
            return;
        }
        if (price < PathShortening.getMin_Price()) {
            player.sendRichMessage("<red>Цена не может быть меньше чем:</red> " + PathShortening.getMin_Price());
            return;
        }
        if (price > PathShortening.getMax_Price()) {
            player.sendRichMessage("<red>Цена не может быть больше чем:</red> " + PathShortening.getMax_Price());
            return;
        }
        AuctionHouse.getAuctionManager().managerData(player, price, plugin);
        player.getInventory().setItemInMainHand(null);
    }
}
