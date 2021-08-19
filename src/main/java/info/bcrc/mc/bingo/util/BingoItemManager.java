package info.bcrc.mc.bingo.util;

import org.bukkit.Material;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class BingoItemManager
{
    public BingoItemManager() {
        candidateItems = new ArrayList<>();
        Collections.addAll(candidateItems, Material.values());
    }

    public BingoItemManager(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        candidateItems = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
//            System.out.println(line);
            Material mat = Material.getMaterial(line.toUpperCase());
            if (mat != null)
            {
//                System.out.println("mat:" + mat.name());
                candidateItems.add(mat);
            }
//            System.out.println("sz: " + candidateItems.size());
        }
    }

    public ArrayList<Material> getCandidateItems() {
        return candidateItems;
    }

    private final ArrayList<Material> candidateItems;
}
