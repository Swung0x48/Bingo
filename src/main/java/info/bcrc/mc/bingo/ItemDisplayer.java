package info.bcrc.mc.bingo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import info.bcrc.mc.bingo.util.BingoItemGenerator;

public class ItemDisplayer {

    protected Logger logger;
    protected Bingo plugin;
    protected BingoItemGenerator creator;

    private HashMap<String, Inventory> playerInventory = new HashMap<>();
    private HashMap<String, boolean[]> finished = new HashMap<>();

    public ItemDisplayer(Bingo plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.creator = new BingoItemGenerator(plugin.getBingoConfig());

        ItemStack[] newItems = creator.generateNewList().toArray(new ItemStack[45]);

        boolean[] boolAchChart = new boolean[45];
        for (int i = 0; i < 45; i++) {
            boolAchChart[i] = false;
        }

        for (Player p : plugin.getServer().getOnlinePlayers()) {
            Inventory inv = plugin.getServer().createInventory(null, 45, p.getName() + "'s Bingo Map");
            inv.setContents(newItems);
            playerInventory.put(p.getName(), inv);

            finished.put(p.getName(), boolAchChart.clone());
        }
    }

    public Inventory getInventoryByPlayer(Player player) {
        return playerInventory.get(player.getName());
    }

    // Get the replacement when player achieved a goal
    private ItemStack returnDefaultItem() {
        // The replacement
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.GREEN + "Achieved");
        item.setItemMeta(meta);

        return item;
    }

    public void playerAchieve(Player player, ItemStack item) {
        Inventory inv = getInventoryByPlayer(player);
        boolean[] hasFinished = finished.get(player.getName());
        int index = inv.first(item);

        inv.setItem(index, returnDefaultItem());
        hasFinished[index] = true;

        if (testAchieve(player.getName(), index)) {
            player.sendMessage("You have made it all finished");
            logger.info(player.getName() + " has made it all finished");
        } else {
            player.sendMessage("You have achieved " + item.getType().getKey().getKey());
            logger.info(player.getName() + " has achieved " + item.getType().getKey().getKey());
        }

    }

    public boolean testItem(Player player, ItemStack item) {
        return Arrays.asList(playerInventory.get(player.getName()).getContents()).contains(item);
    }

    public boolean testInv(Inventory inv) {
        return playerInventory.containsValue(inv);
    }

    public boolean testAchieve(String playerName, int index) {
        boolean[] chart = finished.get(playerName);

        boolean testLin = true;
        int lin = index / 9 * 9 + 2;
        for (int i = 0; i < 5; i++) {
            if (!chart[lin]) {
                testLin = false;
            }
            lin += 1;
        }

        boolean testCol = true;
        int col = index % 9;
        for (int i = 0; i < 5; i++) {
            if (!chart[col]) {
                testCol = false;
            }
            col += 9;
        }

        return testCol || testLin;
    }
}
