package info.bcrc.mc.bingo.base.view;

import info.bcrc.mc.bingo.Bingo;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BingoCardView
{
    protected Inventory inventory;

    public BingoCardView(Bingo plugin, Player player) {
        inventory = plugin.getServer().createInventory(null, 45, player.getName() + "'s Bingo Card");
    }

    public void setup(ItemStack[] items) {
        inventory.setContents(items);
    }

    public void toggle(int index) {
        if (index < 0 || index > inventory.getSize())
            throw new ArrayIndexOutOfBoundsException("Attempted to toggle an out of bound cell.");

        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            throw new RuntimeException("Item meta cannot be retrieved.");
        }
        meta.setDisplayName(ChatColor.GREEN + "Achieved");
        item.setItemMeta(meta);
        update(index, item);
    }

    public void update(int index, ItemStack item) {
        inventory.setItem(index, item);
    }
}
