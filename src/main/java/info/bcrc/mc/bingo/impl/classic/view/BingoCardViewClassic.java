package info.bcrc.mc.bingo.impl.classic.view;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import info.bcrc.mc.bingo.Bingo;
import info.bcrc.mc.bingo.base.view.BingoCardView;

public class BingoCardViewClassic extends BingoCardView {
    public BingoCardViewClassic(Bingo plugin, Player player, ItemStack[] items) {
        super(plugin, player, items);
    }
}
