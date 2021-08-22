package info.bcrc.mc.bingo.controller;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import info.bcrc.mc.bingo.Bingo;

public class BingoPreGameListener implements Listener {

    protected Bingo plugin;

    public BingoPreGameListener(Bingo plugin) {
        this.plugin = plugin;
    }

    public void register() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (plugin.bingoGame.isPlayerInGame(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (plugin.bingoGame.isPlayerInGame(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;
        
        if (plugin.bingoGame.isPlayerInGame((Player) event.getEntity()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;
        
        if (plugin.bingoGame.isPlayerInGame((Player) event.getEntity()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        if (plugin.bingoGame.isPlayerInGame(event.getPlayer()))
            event.setCancelled(true);
    }
}