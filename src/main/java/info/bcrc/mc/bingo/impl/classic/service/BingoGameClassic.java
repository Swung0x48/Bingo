package info.bcrc.mc.bingo.impl.classic.service;

import info.bcrc.mc.bingo.Bingo;
import info.bcrc.mc.bingo.base.model.BingoCard;
import info.bcrc.mc.bingo.base.service.BingoGame;
import info.bcrc.mc.bingo.base.view.BingoCardView;
import info.bcrc.mc.bingo.impl.classic.model.BingoCardClassic;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BingoGameClassic extends BingoGame
{
    Bingo plugin;

    public BingoGameClassic(Bingo plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public BingoCardClassic createBingoCardForPlayer(Player player)
    {
        return new BingoCardClassic(plugin.getBingoItemGenerator()
                .getSelectedItems().toArray(ItemStack[]::new));
    }

    @Override
    public BingoCardView createBingoCardViewForPlayer(Player player, ItemStack[] items)
    {
        return new BingoCardView(plugin, player, items);
    }

    @Override
    public void found(Player player, ItemStack item)
    {
        playerState.get(player).toggle(item);
        playerView.get(player).toggle(item);
    }
}
