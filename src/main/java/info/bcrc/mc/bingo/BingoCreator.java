package info.bcrc.mc.bingo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class BingoCreator {
    private Bingo plugin;

    private static Material[] allAllowedItems = { Material.ACACIA_BOAT, Material.ACACIA_BUTTON, Material.ACACIA_DOOR,
        Material.ACACIA_FENCE, Material.ACACIA_FENCE_GATE, Material.ACACIA_LEAVES, Material.ACACIA_LOG,
        Material.ACACIA_PLANKS, Material.ACACIA_PRESSURE_PLATE, Material.ACACIA_SAPLING, Material.ACACIA_SIGN,
        Material.ACACIA_SLAB, Material.ACACIA_STAIRS, Material.ACACIA_TRAPDOOR, Material.ACACIA_WALL_SIGN,
        Material.ACACIA_WOOD, Material.ACTIVATOR_RAIL, Material.ALLIUM, Material.AMETHYST_BLOCK,
        Material.AMETHYST_CLUSTER, Material.AMETHYST_SHARD, Material.ANCIENT_DEBRIS, Material.ANDESITE,
        Material.ANDESITE_SLAB, Material.ANDESITE_STAIRS, Material.ANDESITE_WALL, Material.ANVIL, Material.APPLE,
        Material.ARMOR_STAND, Material.ARROW, Material.ATTACHED_MELON_STEM, Material.ATTACHED_PUMPKIN_STEM,
        Material.AXOLOTL_BUCKET, Material.AXOLOTL_SPAWN_EGG, Material.AZALEA, Material.AZALEA_LEAVES,
        Material.AZURE_BLUET, Material.BAKED_POTATO, Material.BAMBOO, Material.BAMBOO_SAPLING, Material.BARREL,
        Material.BARRIER, Material.BASALT, Material.BAT_SPAWN_EGG, Material.BEACON, Material.BEDROCK,
        Material.BEE_NEST, Material.BEE_SPAWN_EGG, Material.BEEF, Material.BEEHIVE, Material.BEETROOT,
        Material.BEETROOT_SEEDS, Material.BEETROOT_SOUP, Material.BEETROOTS, Material.BELL, Material.BIG_DRIPLEAF,
        Material.BIG_DRIPLEAF_STEM, Material.BIRCH_BOAT, Material.BIRCH_BUTTON, Material.BIRCH_DOOR,
        Material.BIRCH_FENCE, Material.BIRCH_FENCE_GATE, Material.BIRCH_LEAVES, Material.BIRCH_LOG,
        Material.BIRCH_PLANKS, Material.BIRCH_PRESSURE_PLATE, Material.BIRCH_SAPLING, Material.BIRCH_SIGN,
        Material.BIRCH_SLAB, Material.BIRCH_STAIRS, Material.BIRCH_TRAPDOOR, Material.BIRCH_WALL_SIGN,
        Material.BIRCH_WOOD, Material.BLACK_BANNER, Material.BLACK_BED, Material.BLACK_CANDLE,
        Material.BLACK_CANDLE_CAKE, Material.BLACK_CARPET };

    private ItemStack[] chestList = new ItemStack[45];

    public BingoCreator(Bingo plugin) {
        this.plugin = plugin;
    };

    public ItemStack[] generateNewList() {
        for (int i = 0; i < 45; i++) {
            chestList[i] = new ItemStack(Material.AIR);
        }

        List<Material> bingoList = new ArrayList<>();

        Random rand = new Random();
        for (int i = 2; i < 45;) {
            Material mat = allAllowedItems[rand.nextInt(allAllowedItems.length)];
            if (!bingoList.contains(mat)) {
                bingoList.add(mat);
                chestList[i] = new ItemStack(mat);
                i++;
            }
            if (i % 9 == 7) {
                i += 4;
            }
        }
        return chestList;
    }

    public void readAllowedItems() {
        FileConfiguration data = YamlConfiguration.loadConfiguration(plugin.getResource("data.yml"));
    }

}
