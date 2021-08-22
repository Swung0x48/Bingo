package info.bcrc.mc.bingo.controller;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import info.bcrc.mc.bingo.Bingo;
import info.bcrc.mc.bingo.base.event.BingoFinishedEvent;
import info.bcrc.mc.bingo.base.event.BingoPlayerQuitEvent;
import info.bcrc.mc.bingo.base.service.BingoGame.GameState;
import info.bcrc.mc.bingo.impl.classic.event.BingoFoundClassicEvent;
import info.bcrc.mc.bingo.util.MessageSender;

public class BingoListener implements Listener {

    protected Bingo plugin;

    public BingoListener(Bingo plugin) {
        this.plugin = plugin;
    }

    public void register() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        plugin.getBingoGame().initializePlayer(event.getPlayer(), true);
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (plugin.getBingoGame() == null)
            return;

        ItemStack item = event.getItem();
        Player player = event.getPlayer();

        if (item != null && plugin.getBingoGame().getGameState() == GameState.RUNNING
                && item.getType().equals(Material.NETHER_STAR)) {
            event.setCancelled(true);
            plugin.getBingoGame().openBingoCard(player);
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        try {
            if (plugin.getBingoGame().isActiveBingoCardView(event.getClickedInventory()))
                event.setCancelled(true);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onInventoryPickupItemEvent(InventoryPickupItemEvent event) {
        if (event.getItem().getItemStack().getType().equals(Material.NETHER_STAR))
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        if (event.getOffHandItem().getType().equals(Material.NETHER_STAR)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();

        if (item.getType().equals(Material.NETHER_STAR)) {
            event.setCancelled(true);
            return;
        }

        if (plugin.getBingoGame().playerThrows(event.getPlayer(), item)) {
            event.getItemDrop().remove();
        }
    }

    @EventHandler
    public void onPlayerFoundClassic(BingoFoundClassicEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getItem();
        int itemsToWin = e.getItemsToWin();
        plugin.getMessageSender().broadcastRawBingoMessage(
                "{\"text\": \"\", \"extra\": [" + MessageSender.bingoJsonPrefix + ", {\"selector\": \""
                        + player.getName() + "\"}, {\"text\": \" found [\"}, {\"translate\": \""
                        + plugin.getMessageSender().getItemTranslationKey(item.getType())
                        + "\", \"color\": \"green\", \"hoverEvent\": {\"action\": \"show_item\", \"value\": \"{\\\"id\\\": \\\""
                        + item.getType().getKey().getKey() + "\\\", \\\"Count\\\": 1}\"}}, {\"text\": \"]!\"}]}");
        plugin.getMessageSender().playBingoSound(player, "entity.firework_rocket.launch");
        plugin.getBingoScoreboard().increaseFoundItems(player, itemsToWin);
        plugin.getBingoScoreboard().setItemsToWin(player, itemsToWin);
    }

    @EventHandler
    public void onPlayerFinished(BingoFinishedEvent e) {
        Player player = e.getPlayer();
        plugin.getMessageSender().broadcastBingoMessage(
                MessageSender.bingoPrefix + player.getName() + " has finished the Bingo!");
        plugin.getMessageSender().broadcastBingoTitle("Bingo!",
                ChatColor.GOLD + player.getName() + ChatColor.AQUA + " has finished the Bingo!");
        plugin.getMessageSender().playBingoSound(player, "entity.firework_rocket.twinkle_far");
    }

    @EventHandler
    public void onPlayerQuitBingo(BingoPlayerQuitEvent e) {
        Player player = e.getPlayer();
        plugin.getBingoScoreboard().removePlayer(player);
        plugin.getMessageSender().broadcastMessage(ChatColor.GOLD + "[Bingo] " + ChatColor.AQUA + player.getName() + ChatColor.RESET + " has quitted the bingo game.");
    }
}
