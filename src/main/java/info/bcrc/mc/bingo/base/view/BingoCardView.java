package info.bcrc.mc.bingo.base.view;

import info.bcrc.mc.bingo.Bingo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class BingoCardView
{
    public BingoCardView(Bingo plugin, Player player, ItemStack[] items) {
        inventory = plugin.getServer().createInventory(null, 45, player.getName() + "'s Bingo Card");
        inventory.setContents(items);
        this.uuid = player.getUniqueId();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setFound(int index) {
        if (index < 0 || index > inventory.getSize())
            throw new ArrayIndexOutOfBoundsException("Attempted to toggle an out of bound cell.");

        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            throw new RuntimeException("Item meta cannot be retrieved.");
        }
        meta.setDisplayName(ChatColor.GREEN + "Achieved");
        item.setItemMeta(meta);
        updateInternal(index, item);
    }

    public void setFound(ItemStack item) {
        setFound(inventory.first(item));
    }

    private void updateInternal(int index, ItemStack item) {
        inventory.setItem(index, item);
    }

    public void openView() {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null)
            player.openInventory(inventory);
    }

    protected Inventory inventory;
    protected UUID uuid;
}
