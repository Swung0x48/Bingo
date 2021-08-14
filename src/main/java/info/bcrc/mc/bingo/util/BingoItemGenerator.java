package info.bcrc.mc.bingo.util;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BingoItemGenerator
{
    public BingoItemGenerator(BingoConfig config) {
        candidateItems = config.getCandidateItems();
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

    private static ArrayList<Material> candidateItems;
    private ArrayList<ItemStack> selectedItems;
}
