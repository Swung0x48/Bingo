package info.bcrc.mc.bingo.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    public Location generateRandomLocation(World world) {
        double x = ThreadLocalRandom.current().nextDouble(minCoordinate, maxCoordinate);
        double z = ThreadLocalRandom.current().nextDouble(minCoordinate, maxCoordinate);

        return new Location(world, x, world.getHighestBlockYAt((int) Math.floor(x), (int) Math.floor(z)), z);
    }

    public Location generateRandomNonLiquidLocation(World world) {
        Location location = generateRandomLocation(world);

        if (!world.getBlockAt(location).isLiquid())
            return location.add(0, 1, 0);
        return generateRandomNonLiquidLocation(world);
    }

    public Location getLocation(World world) {
        if (location == null)
            return generateRandomNonLiquidLocation(world);

        return location;
    }

    private static ArrayList<Material> candidateItems;
    private ArrayList<ItemStack> selectedItems;
    private Location location;
    private double minCoordinate;
    private double maxCoordinate;
}
