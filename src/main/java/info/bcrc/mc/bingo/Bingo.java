package info.bcrc.mc.bingo;

import java.util.*;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class Bingo extends JavaPlugin {
    private ItemDisplayer itemDisplayer;
    public boolean setupBingo = false;
    public boolean startBingo = false;
    public World bingoWorld;

    public BingoCommandExecutor bingoCommandExecutor;
    public BingoListener listener;
    private ArrayList<Material> candidateItems;

    public Bingo() {
        itemDisplayer = new ItemDisplayer(this);
        bingoCommandExecutor = new BingoCommandExecutor(this);
        listener = new BingoListener(this);
    }

    public ItemDisplayer getItemDisplayer() {
        return itemDisplayer;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        List<String> items = this.getConfig().getStringList("candidate-items");
        if (items.size() < 25) {
            items = Arrays.stream("ACACIA_BOAT ACACIA_BUTTON ACACIA_DOOR ACACIA_FENCE ACACIA_FENCE_GATE ACACIA_LEAVES ACACIA_LOG ACACIA_PLANKS ACACIA_PRESSURE_PLATE ACACIA_SAPLING ACACIA_SIGN ACACIA_SLAB ACACIA_STAIRS ACACIA_TRAPDOOR ACACIA_WALL_SIGN ACACIA_WOOD ACTIVATOR_RAIL ALLIUM AMETHYST_BLOCK AMETHYST_CLUSTER AMETHYST_SHARD ANCIENT_DEBRIS ANDESITE ANDESITE_SLAB ANDESITE_STAIRS ANDESITE_WALL ANVIL APPLE ARMOR_STAND ARROW ATTACHED_MELON_STEM ATTACHED_PUMPKIN_STEM AXOLOTL_BUCKET AXOLOTL_SPAWN_EGG AZALEA AZALEA_LEAVES AZURE_BLUET BAKED_POTATO BAMBOO BAMBOO_SAPLING BARREL BARRIER BASALT BAT_SPAWN_EGG BEACON BEDROCK BEE_NEST BEE_SPAWN_EGG BEEF BEEHIVE BEETROOT BEETROOT_SEEDS BEETROOT_SOUP BEETROOTS BELL BIG_DRIPLEAF BIG_DRIPLEAF_STEM BIRCH_BOAT BIRCH_BUTTON BIRCH_DOOR BIRCH_FENCE BIRCH_FENCE_GATE BIRCH_LEAVES BIRCH_LOG BIRCH_PLANKS BIRCH_PRESSURE_PLATE BIRCH_SAPLING BIRCH_SIGN BIRCH_SLAB BIRCH_STAIRS BIRCH_TRAPDOOR BIRCH_WALL_SIGN BIRCH_WOOD BLACK_BANNER BLACK_BED BLACK_CANDLE BLACK_CANDLE_CAKE BLACK_CARPET".split(" ")).toList();
            this.getConfig().set("candidate-items", items);
        }
        for (String item: items) {
            candidateItems.add(Material.getMaterial(item));
        }

        this.getServer().getPluginManager().registerEvents(listener, this);

        saveConfig();
        Objects.requireNonNull(getCommand("bingo")).setExecutor(bingoCommandExecutor);
    }

    @Override
    public void onDisable() {

    }
}
