package info.bcrc.mc.bingo.impl.classic.model;

import info.bcrc.mc.bingo.base.model.BingoCard;
import org.bukkit.inventory.ItemStack;

public class BingoCardClassic extends BingoCard
{
    public BingoCardClassic(ItemStack[] items)
    {
        super(items);
    }

    @Override
    public boolean win() {
        return false;
    }

    static class Cell {
        int x;
        int y;
    }

    private static Cell oneDimToXy(int index) {
        return new Cell();
    }
}
