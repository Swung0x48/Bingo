package info.bcrc.mc.bingo.controller;

import info.bcrc.mc.bingo.Bingo;
import info.bcrc.mc.bingo.base.service.BingoGame;
import info.bcrc.mc.bingo.util.MessageSender;

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
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

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

        plugin.getLogger().info("onPlayerInteractEvent");
        if (item != null
                && plugin.getBingoGame().getGameState() == BingoGame.GameState.RUNNING
                && item.getType().equals(Material.NETHER_STAR)) {
            event.setCancelled(true);
            plugin.getLogger().info("openBingoCard");
            plugin.getBingoGame().openBingoCard(player);
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem() != null) {
            if (plugin.getBingoGame().isPlayerInGame(player)
                    && event.getCurrentItem().getType().equals(Material.NETHER_STAR)
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

    // 用不了似乎
    @EventHandler
    public void onInventoryMoveItemEvent(InventoryMoveItemEvent event) {
        if (plugin.getItemDisplayer().testInv(event.getDestination())) {
            event.setCancelled(true);

            Player player = (Player) event.getSource().getHolder();
            ItemStack item = event.getItem();

            if (plugin.getItemDisplayer().testItem(player, item)) {
                plugin.getItemDisplayer().playerAchieve(player, item);
                if (item.getAmount() == 1) {
                    event.getItem().setType(Material.AIR);
                } else {
                    item.setAmount(item.getAmount() - 1);
                }
            }
        }
    }

    @EventHandler
    public void on(PlayerSwapHandItemsEvent event) {
        if (event.getOffHandItem().getType().equals(Material.NETHER_STAR)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();

        if (item.getType().equals(Material.NETHER_STAR)) {
            event.setCancelled(true);
            return;
        }

        if (plugin.getBingoGame().found(event.getPlayer(), item)) {
            // event.setCancelled(true);
//            plugin.getItemDisplayer().playerAchieve(event.getPlayer(), item);
            // if (item.getAmount() == 1) {
            onPlayerFound(event.getPlayer(), item);
            event.getItemDrop().remove();
            // } else {
            // item.setAmount(item.getAmount() - 1);
            // }
        }
    }

    private void onPlayerFound(Player player, ItemStack item) {
        plugin.getBingoGame().getPlayersInGame().forEach(inGamePlayer -> {
            MessageSender.sendRawMessage(inGamePlayer, 
                    "{\"text\": \"\", \"extra\": [{\"text\": \"[Bingo] \", \"color\": \"gold\"}, {\"selector\": \""
                    + player.getName() + "\"}, {\"text\": \" found [\"}, {\"translate\": \""
                    + MessageSender.getItemTranslationKey(item.getType()) + "\", \"color\": \"green\", \"hoverEvent\": {\"action\": \"show_item\", \"value\": \"{\\\"id\\\": \\\"" + item.getType().getKey().getKey() + "\\\", \\\"Count\\\": 1}\"}}, {\"text\": \"] !\"}]}");
        });
    }
}
