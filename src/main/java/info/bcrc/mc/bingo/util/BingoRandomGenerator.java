package info.bcrc.mc.bingo.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

public class BingoRandomGenerator {
    public BingoRandomGenerator(BingoConfig config, BingoItemManager itemManager) {
        candidateItems = itemManager.getCandidateItems();
        minCoordinate = config.getMinCoordinate();
        maxCoordinate = config.getMaxCoordinate();
    }

    public ArrayList<ItemStack> getSelectedItems() {
        if (selectedItems == null) {
            selectedItems = new ArrayList<>();
            generateNewList();
        }

        return selectedItems;
    }

    public ArrayList<ItemStack> generateNewList() {
        Collections.shuffle(candidateItems);
        selectedItems = new ArrayList<>();

        for (int i = 0; i < 25;) {
            for (int j = 0; j < 2; ++j)
                selectedItems.add(new ItemStack(Material.AIR));

            for (int j = 0; j < 5; j++) {
                selectedItems.add(new ItemStack(candidateItems.get(i++)));
            }

            for (int j = 0; j < 2; ++j)
                selectedItems.add(new ItemStack(Material.AIR));
        }

        return selectedItems;
    }

    public Location getRandomLocation(World world) {
        int x = ThreadLocalRandom.current().nextInt(minCoordinate, maxCoordinate);
        int z = ThreadLocalRandom.current().nextInt(minCoordinate, maxCoordinate);

        return new Location(world, x, world.getHighestBlockYAt(x, z), z);
    }

    public void generateRandomNonLiquidLocation(World world) {
        for (int i = 0; i < 20; ++i) {
            location = getRandomLocation(world);

            if (!world.getBlockAt(location).isLiquid())
                break;
        }
        location = location.add(0.5, 1, 0.5);
    }

    public Location getLocation(World world) {
        if (location == null)
            generateRandomNonLiquidLocation(world);

        return location;
    }

    public void clearLocationCache() {
        location = null;
    }

    private static ArrayList<Material> candidateItems;
    private ArrayList<ItemStack> selectedItems;
    private Location location;
    private int minCoordinate;
    private int maxCoordinate;
}
