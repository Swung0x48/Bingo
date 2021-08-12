package info.bcrc.mc.bingo;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BingoConfig
{
    public Material[] getCandidateItems()
    {
        return (Material[]) candidateItems.toArray();
    }

    public BingoConfig(org.bukkit.configuration.file.FileConfiguration config) {
        candidateItems = new ArrayList<>();

        List<String> items = config.getStringList("candidate-items");
        if (items.size() < 25) {
            items = Arrays.stream("ACACIA_BOAT ACACIA_BUTTON ACACIA_DOOR ACACIA_FENCE ACACIA_FENCE_GATE ACACIA_LEAVES ACACIA_LOG ACACIA_PLANKS ACACIA_PRESSURE_PLATE ACACIA_SAPLING ACACIA_SIGN ACACIA_SLAB ACACIA_STAIRS ACACIA_TRAPDOOR ACACIA_WALL_SIGN ACACIA_WOOD ACTIVATOR_RAIL ALLIUM AMETHYST_BLOCK AMETHYST_CLUSTER AMETHYST_SHARD ANCIENT_DEBRIS ANDESITE ANDESITE_SLAB ANDESITE_STAIRS ANDESITE_WALL ANVIL APPLE ARMOR_STAND ARROW ATTACHED_MELON_STEM ATTACHED_PUMPKIN_STEM AXOLOTL_BUCKET AXOLOTL_SPAWN_EGG AZALEA AZALEA_LEAVES AZURE_BLUET BAKED_POTATO BAMBOO BAMBOO_SAPLING BARREL BARRIER BASALT BAT_SPAWN_EGG BEACON BEDROCK BEE_NEST BEE_SPAWN_EGG BEEF BEEHIVE BEETROOT BEETROOT_SEEDS BEETROOT_SOUP BEETROOTS BELL BIG_DRIPLEAF BIG_DRIPLEAF_STEM BIRCH_BOAT BIRCH_BUTTON BIRCH_DOOR BIRCH_FENCE BIRCH_FENCE_GATE BIRCH_LEAVES BIRCH_LOG BIRCH_PLANKS BIRCH_PRESSURE_PLATE BIRCH_SAPLING BIRCH_SIGN BIRCH_SLAB BIRCH_STAIRS BIRCH_TRAPDOOR BIRCH_WALL_SIGN BIRCH_WOOD BLACK_BANNER BLACK_BED BLACK_CANDLE BLACK_CANDLE_CAKE BLACK_CARPET".split(" ")).toList();
            config.set("candidate-items", items);
        }
        for (String item: items) {
            if (item != null) {
                candidateItems.add(Material.getMaterial(item));
            }
        }
    }

    private final ArrayList<Material> candidateItems;
}
