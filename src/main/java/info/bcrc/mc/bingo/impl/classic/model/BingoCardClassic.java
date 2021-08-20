package info.bcrc.mc.bingo.impl.classic.model;

import info.bcrc.mc.bingo.base.model.BingoCard;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class BingoCardClassic extends BingoCard
{
    public BingoCardClassic(UUID Uuid, ItemStack[] items)
    {
        super(Uuid, items);
    }

    @Override
    public boolean setFound(int index) {
        if (!super.setFound(index))
            return false;

        if (!hasFinished() && itemsToWinInternal(index) == 0) {
            itemsToWin = itemsToWinInternal(index);
            onFinished();
        }
        return true;
    }

    @Override
    public boolean hasFinished() {
        return itemsToWin == 0;
    }

    public int getItemsToWin() {
        return itemsToWin;
    }

    @Override
    public void onFinished()
    {
    }

    private int itemsToWinInternal(int index) {
        int absoluteX = indexToX(index);
        int absoluteY = indexToY(index);
        int remaining = 5;

        int localRemaining = 0;
        for (int i = 0; i < 5; ++i) {
            if (!checked[xyToIndex(getAbsoluteX(i), absoluteY)]) {
                ++localRemaining;
            }
        }
        remaining = Math.min(remaining, localRemaining);

        localRemaining = 0;
        for (int j = 0; j < 5; ++j) {
            if (!checked[xyToIndex(absoluteX, j)]) {
                ++localRemaining;
            }
        }
        remaining = Math.min(remaining, localRemaining);

        if (atMainDiagonal(absoluteX, absoluteY)) {
            localRemaining = 0;
            for (int i = 0; i < 5; ++i) {
                if (!checked[xyToIndex(getAbsoluteX(i), i)]) {
                    ++localRemaining;
                }
            }
            remaining = Math.min(remaining, localRemaining);
        }

        if (atAntiDiagonal(absoluteX, absoluteY)) {
            localRemaining = 0;
            for (int i = 0; i < 5; ++i) {
                if (!checked[xyToIndex(getAbsoluteX(i), 4 - i)]) {
                    ++localRemaining;
                }
            }
            remaining = Math.min(remaining, localRemaining);
        }

        return remaining;
    }
    private static int indexToX(int index) {
        return index % 9;
    }

    private static int indexToY(int index) {
        return index / 9;
    }

    private static int xyToIndex(int x, int y) {
        return x + y * 9;
    }

    private static int getEffectiveX(int absoluteX) {
        return absoluteX - X_INIT;
    }

    private static int getAbsoluteX(int effectiveX) {
        return effectiveX + X_INIT;
    }

    private static boolean atMainDiagonal(int x, int y) {
        return getEffectiveX(x) == y;
    }

    private static boolean atAntiDiagonal(int x, int y) {
        return getEffectiveX(x) + y == 4;
    }

    private int itemsToWin = 5;
    private final static int X_INIT = 2;
}
