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
    public boolean hasFinished()
    {
        return false;
    }
}
