package info.bcrc.mc.bingo;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
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
            List<Material> allMaterials = Arrays.stream(Material.values()).toList();
            config.set("candidate-items", allMaterials.toArray(new Material[0]));
        }
        for (String item: items) {
            if (item != null) {
                candidateItems.add(Material.getMaterial(item));
            }
        }
    }

    private final ArrayList<Material> candidateItems;
}
