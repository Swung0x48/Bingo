package info.bcrc.mc.bingo.util;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class BingoConfig {
    public ArrayList<Material> getCandidateItems() {
        return candidateItems;
    }

    public BingoConfig(FileConfiguration config) {
        this.config = config;

        candidateItems = new ArrayList<>();

        List<String> items = config.getStringList("candidate-items");
        if (items.size() < 25) {
            ArrayList<String> allMaterials = new ArrayList<>();
            for (Material item : Material.values()) {
                allMaterials.add(item.name());
            }
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
            MessageSender.logger.warning("Found maximum coordinate smaller than minimum. Swapping them.");
            double tempCoordinate = minCoordinate;
            minCoordinate = maxCoordinate;
            maxCoordinate = tempCoordinate;

            config.set("coordinate-range.min", minCoordinate);
            config.set("coordinate-range.max", maxCoordinate);
        }
        MessageSender.logger.info(Double.toString(minCoordinate) + " " + Double.toString(maxCoordinate));
    }

    public double getMinCoordinate() {
        return minCoordinate;
    };

    public double getMaxCoordinate() {
        return maxCoordinate;
    };

    private FileConfiguration config;
    private final ArrayList<Material> candidateItems;
    private double minCoordinate;
    private double maxCoordinate;
}
