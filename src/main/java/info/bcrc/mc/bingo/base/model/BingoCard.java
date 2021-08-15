package info.bcrc.mc.bingo.base.model;

import org.bukkit.entity.Player;
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
    public Player player;
    public ItemStack[] items;
    public boolean[] checked;

    public BingoCard(Player player, ItemStack[] items) {
        this.player = player;
        this.items = items;
        this.checked = new boolean[items.length];
        Arrays.fill(checked, false);
    }

    public void toggle(int index) {
        checked[index] = !checked[index];
    }

    public int toggle(ItemStack item) {
        int index = Arrays.asList(items).indexOf(item);
        if (index != -1)
            toggle(index);
        return index;
    }

    public abstract boolean hasFinished();

    public abstract void onFinished();
}
