package info.bcrc.mc.bingo.util;

import org.bukkit.Material;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Logger;

public class BingoItemManager
{
    public BingoItemManager(Logger logger) {
        candidateItems = new ArrayList<>();
        Collections.addAll(candidateItems, Material.values());
        logger.info(MessageFormat.format("Loaded {0} items.", candidateItems.size()));
    }

    public BingoItemManager(InputStream stream, Logger logger) {
        Scanner scanner = new Scanner(stream);
        candidateItems = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Material mat = Material.getMaterial(line.toUpperCase());
            if (mat != null) {
                candidateItems.add(mat);
            }
        }
        logger.info(MessageFormat.format("Loaded {0} items.", candidateItems.size()));
    }

    public ArrayList<Material> getCandidateItems() {
        return candidateItems;
    }

    private final ArrayList<Material> candidateItems;
}
