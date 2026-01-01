package me.bedepay.auctionhouse.database.data;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public record AuctionData(ItemStack itemStack, double price, UUID seller, List<Component> originalLore) {
    public AuctionData {
        if (price < 0) {
            throw new IllegalArgumentException("The price " + price + " cannot be less than 0");
        }
    }
}
