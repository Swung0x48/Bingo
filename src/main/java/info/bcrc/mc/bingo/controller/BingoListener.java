package info.bcrc.mc.bingo.controller;

import info.bcrc.mc.bingo.Bingo;
import info.bcrc.mc.bingo.base.event.BingoFinishedEvent;
import info.bcrc.mc.bingo.base.event.BingoFoundEvent;
import info.bcrc.mc.bingo.base.service.BingoGame;
import info.bcrc.mc.bingo.util.MessageSender;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class BingoListener implements Listener {

    protected Bingo plugin;

    public BingoListener(Bingo plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (plugin.getBingoGame() == null)
            return;

        ItemStack item = event.getItem();
        Player player = event.getPlayer();

        if (item != null
                && plugin.getBingoGame().getGameState() == BingoGame.GameState.RUNNING
                && item.getType().equals(Material.NETHER_STAR)) {
            event.setCancelled(true);
            plugin.getBingoGame().openBingoCard(player);
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (plugin.getBingoGame() == null || plugin.getBingoGame().isPlayerInGame(player))
            return;

        if (event.getCurrentItem() != null) {
            if (event.getCurrentItem().getType().equals(Material.NETHER_STAR)
                    || plugin.getBingoGame().cardViewBelongsToPlayer(event.getClickedInventory(), player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryPickupItemEvent(InventoryPickupItemEvent event) {
        if (event.getItem().getItemStack().getType().equals(Material.NETHER_STAR)) {
            event.setCancelled(true);
        }
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
    public void onPlayerFound(BingoFoundEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getItem();
        plugin.getMessageSender().broadcastRawBingoMessage(
            "{\"text\": \"\", \"extra\": [{\"text\": \"[Bingo] \", \"color\": \"gold\"}, {\"selector\": \""
                + player.getName() + "\"}, {\"text\": \" found [\"}, {\"translate\": \""
                + plugin.getMessageSender().getItemTranslationKey(item.getType()) + "\", \"color\": \"green\", \"hoverEvent\": {\"action\": \"show_item\", \"value\": \"{\\\"id\\\": \\\"" + item.getType().getKey().getKey() + "\\\", \\\"Count\\\": 1}\"}}, {\"text\": \"] !\"}]}"
        );
        plugin.getMessageSender().playBingoSound(player, "entity.firework_rocket.launch");
    }

    @EventHandler
    public void onPlayerFinished(BingoFinishedEvent e) {
        Player player = e.getPlayer();
        plugin.getMessageSender().broadcastBingoMessage(ChatColor.GOLD + "[Bingo] " + ChatColor.RESET + player.getName() + " has finished the Bingo!");
        plugin.getMessageSender().broadcastBingoTitle(" ", ChatColor.GOLD + player.getName() + ChatColor.AQUA + " has finished the Bingo!");
        plugin.getMessageSender().playBingoSound(player, "entity.firework_rocket.twinkle_far");
    }
}
