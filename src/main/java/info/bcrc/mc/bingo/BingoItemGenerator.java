package info.bcrc.mc.bingo;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BingoItemGenerator
{
    public BingoItemGenerator(BingoConfig config) {
        candidateItems = config.getCandidateItems();
        selectedItems = new ArrayList<>();
    }

    public ArrayList<ItemStack> generateNewList() {
        Collections.shuffle(candidateItems);
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

    static int xyTo1D(int x, int y, int xOffset, int yOffset) {
        int actualX = x + xOffset;
        int actualY = y + yOffset;
        return actualY * 5 + actualX;
    }

    static int xyTo1D(int x, int y) {
        return xyTo1D(x, y, 2, 0);
    }

    private static ArrayList<Material> candidateItems;
    private ArrayList<ItemStack> selectedItems;
}
