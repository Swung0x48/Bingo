package info.bcrc.mc.bingo.util;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import info.bcrc.mc.bingo.Bingo;

import java.util.ArrayList;
import java.util.List;

public class BingoConfig {
//    public ArrayList<Material> getCandidateItems() {
//        return candidateItems;
//    }

    public BingoConfig(Bingo plugin, FileConfiguration config) {
//        candidateItems = new ArrayList<>();
//
//        List<String> items = config.getStringList("candidate-items");
//        if (items.size() < 25) {
//            ArrayList<String> allMaterials = new ArrayList<>();
//            for (Material item : Material.values()) {
//                allMaterials.add(item.name());
//            }
//            config.set("candidate-items", allMaterials.toArray(new String[0]));
//            items = allMaterials;
//        }
//        for (String item : items) {
//            if (item != null) {
//                candidateItems.add(Material.getMaterial(item));
//            }
//        }

        minCoordinate = config.getInt("coordinate-range.min");
        maxCoordinate = config.getInt("coordinate-range.max");

        if (minCoordinate >= maxCoordinate) {
            plugin.getLogger().warning("Found maximum coordinate smaller than minimum. Swapping them.");
            int tempCoordinate = minCoordinate;
            minCoordinate = maxCoordinate;
            maxCoordinate = tempCoordinate;

            config.set("coordinate-range.min", minCoordinate);
            config.set("coordinate-range.max", maxCoordinate);
        }

        config.options().copyDefaults(true);
    }

    public int getMinCoordinate() {
        return minCoordinate;
    };

    public int getMaxCoordinate() {
        return maxCoordinate;
    };

//    private final ArrayList<Material> candidateItems;
    private int minCoordinate;
    private int maxCoordinate;
}
