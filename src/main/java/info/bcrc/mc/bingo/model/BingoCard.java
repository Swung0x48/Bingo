package info.bcrc.mc.bingo.model;

import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * BingoCard
 * Model only.
 * DO NOT handle UI & interactions in this class!!!
 * (Plz do that in BingoCardView)
**/
public abstract class BingoCard
{
    ItemStack[] items;
    boolean[] checked;

    public BingoCard(ItemStack[] items) {
        this.items = items;
        this.checked = new boolean[items.length];
        Arrays.fill(checked, false);
    }

    public void toggle(int index) {
        checked[index] = !checked[index];
    }

    public abstract boolean finished();
}
