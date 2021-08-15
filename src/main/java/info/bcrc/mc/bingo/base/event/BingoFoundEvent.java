package info.bcrc.mc.bingo.base.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class BingoFoundEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    public Player getPlayer() {
        return player;
    }

    public ItemStack getItem() {
        return item;
    }

    private final Player player;
    private final ItemStack item;

    public BingoFoundEvent(Player player, ItemStack item) {
        this.player = player;
        this.item = item;
    }

    @Override
    public HandlerList getHandlers()
    {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
