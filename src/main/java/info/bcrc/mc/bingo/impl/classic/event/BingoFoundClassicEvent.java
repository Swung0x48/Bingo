package info.bcrc.mc.bingo.impl.classic.event;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import info.bcrc.mc.bingo.base.event.BingoFoundEvent;

public class BingoFoundClassicEvent extends BingoFoundEvent {
    public BingoFoundClassicEvent(Player player, ItemStack item, int itemsToWin) {
        super(player, item);
        this.itemsToWin = itemsToWin;
    }

    public int getItemsToWin() {
        return itemsToWin;
    }

    private final int itemsToWin;
}
