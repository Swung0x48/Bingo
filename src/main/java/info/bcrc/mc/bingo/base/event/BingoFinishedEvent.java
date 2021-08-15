package info.bcrc.mc.bingo.base.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BingoFinishedEvent extends Event
{
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;

    public BingoFinishedEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
