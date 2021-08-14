package info.bcrc.mc.bingo.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public class BingoConfig {
    public ArrayList<Material> getCandidateItems() {
        return candidateItems;
    }

    public BingoConfig(FileConfiguration config) {
        candidateItems = new ArrayList<>();

        List<String> items = config.getStringList("candidate-items");
        if (items.size() < 25) {
            ArrayList<String> allMaterials = new ArrayList<>();
            for (Material item : Material.values()) {
                allMaterials.add(item.name());
            }
            System.out.println("Size: " + allMaterials.size());
            config.set("candidate-items", allMaterials.toArray(new String[0]));
            items = allMaterials;
        }
        for (String item : items) {
            if (item != null) {
                candidateItems.add(Material.getMaterial(item));
            }
        }

        minCoordinate = config.getDouble("coordinate-range.min");
        maxCoordinate = config.getDouble("coordinate-range.max");

        if (minCoordinate >= maxCoordinate) {
            config.set("coordinate-range.min", 10);
            config.set("coordinate-range.max", 20);
        }
        assert minCoordinate < maxCoordinate;
    }

    public double getMinCoordinate() {
        return minCoordinate;
    };

    public double getMaxCoordinate() {
        return maxCoordinate;
    };

    private final ArrayList<Material> candidateItems;
    private final double minCoordinate;
    private final double maxCoordinate;
}
