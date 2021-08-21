package info.bcrc.mc.bingo.base.model;

import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.UUID;

/**
 * BingoCard
 * Model only.
 * DO NOT handle UI & interactions in this class!!!
 * (Plz do that in BingoCardView)
**/
public abstract class BingoCard
{
    public UUID Uuid;
    public ItemStack[] items;
    public boolean[] checked;

    public BingoCard(UUID Uuid, ItemStack[] items) {
        this.Uuid = Uuid;
        this.items = items;
        this.checked = new boolean[items.length];
        Arrays.fill(checked, false);
    }

    public boolean setFound(int index) {
        if (checked[index])
            return false;
        checked[index] = true;
        return true;
    }

    public int setFound(ItemStack item) {
        boolean finished = hasFinished();
        int index = Arrays.asList(items).indexOf(item);
        if (index == -1 || !setFound(index))
            return -1;

        onFound(index);
        if (!finished && hasFinished())
            onFinished();
        return index;
    }

    public abstract boolean hasFinished();

    public abstract void onFound(int index);

    public abstract void onFinished();
}
