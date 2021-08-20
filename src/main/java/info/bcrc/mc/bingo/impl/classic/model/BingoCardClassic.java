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

        if (!hasFinished && hasFinishedInternal(index)) {
            hasFinished = hasFinishedInternal(index);
            onFinished();
        }
        return true;
    }

    @Override
    public boolean hasFinished() {
        return hasFinished;
    }

    @Override
    public void onFinished()
    {
    }

    private boolean hasFinishedInternal(int index) {
        int absoluteX = indexToX(index);
        int absoluteY = indexToY(index);

        boolean won = true;
        for (int i = 0; i < 5; ++i) {
            if (!checked[xyToIndex(getAbsoluteX(i), absoluteY)]) {
                won = false;
                break;
            }
        }
        if (won) return true;

        won = true;
        for (int j = 0; j < 5; ++j) {
            if (!checked[xyToIndex(absoluteX, j)]) {
                won = false;
                break;
            }
        }
        if (won) return true;

        if (atMainDiagonal(absoluteX, absoluteY)) {
            won = true;
            for (int i = 0; i < 5; ++i) {
                if (!checked[xyToIndex(getAbsoluteX(i), i)]) {
                    won = false;
                    break;
                }
            }
            if (won) return true;
        }

        if (atAntiDiagonal(absoluteX, absoluteY)) {
            won = true;
            for (int i = 0; i < 5; ++i) {
                if (!checked[xyToIndex(getAbsoluteX(i), 4 - i)]) {
                    won = false;
                    break;
                }
            }
            if (won) return true;
        }

        return false;
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

    private boolean hasFinished = false;
    private final static int X_INIT = 2;
}
