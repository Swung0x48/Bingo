package info.bcrc.mc.bingo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import info.bcrc.mc.bingo.Bingo;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;

public class BingoConfig {
    public ArrayList<Material> getCandidateItems() {
        return candidateItems;
    }

    public BingoConfig(Configuration config) {
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
            minCoordinate = 10;
            maxCoordinate = 20;

            config.set("coordinate-range.min", minCoordinate);
            config.set("coordinate-range.max", maxCoordinate);
        }
    }

    public double getMinCoordinate() {
        return minCoordinate;
    };

    public double getMaxCoordinate() {
        return maxCoordinate;
    };

    private Configuration config;
    private final ArrayList<Material> candidateItems;
    private double minCoordinate;
    private double maxCoordinate;
}
