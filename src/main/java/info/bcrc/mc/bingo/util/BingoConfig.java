package info.bcrc.mc.bingo.util;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class BingoConfig
{
    public ArrayList<Material> getCandidateItems()
    {
        return candidateItems;
    }

    public BingoConfig(org.bukkit.configuration.file.FileConfiguration config) {
        candidateItems = new ArrayList<>();

        List<String> items = config.getStringList("candidate-items");
        if (items.size() < 25) {
            ArrayList<String> allMaterials = new ArrayList<>();
            for (Material item: Material.values()) {
                allMaterials.add(item.name());
            }
            System.out.println("Size: " + allMaterials.size());
            config.set("candidate-items", allMaterials.toArray(new String[0]));
            items = allMaterials;
        }
        for (String item: items) {
            if (item != null) {
                candidateItems.add(Material.getMaterial(item));
            }
        }
    }

    private final ArrayList<Material> candidateItems;
}
