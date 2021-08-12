package info.bcrc.mc.bingo;

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
        try {
            ItemStack item = event.getItem();
            Player player = event.getPlayer();

            if (item != null) {
                if (plugin.setupBingo && item.getType().equals(Material.NETHER_STAR)) {
                    event.setCancelled(true);
                    player.openInventory(plugin.display.getInvForPlayer(player));
                }
            } else {
                plugin.logger.info("nought");
            }

        } catch (NullPointerException e) {
            plugin.logger.info(e.toString() + "[onPlayerInteractEvent]");
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (event.getCurrentItem() != null) {
            if (event.getCurrentItem().getType().equals(Material.NETHER_STAR)
                    || plugin.display.testInv(event.getClickedInventory())) {
                event.setCancelled(true);
                return;
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
        if (plugin.display.testInv(event.getDestination())) {
            event.setCancelled(true);

            Player player = (Player) event.getSource().getHolder();
            ItemStack item = event.getItem();

            if (plugin.display.testItem(player, item)) {
                plugin.display.playerAchieve(player, item);
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

        if (plugin.display.testItem(event.getPlayer(), item)) {
            // event.setCancelled(true);
            plugin.display.playerAchieve(event.getPlayer(), item);
            // if (item.getAmount() == 1) {
            event.getItemDrop().remove();
            // } else {
            // item.setAmount(item.getAmount() - 1);
            // }
            return;
        }
    }

}
