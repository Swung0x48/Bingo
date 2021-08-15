package info.bcrc.mc.bingo.impl.classic.service;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import info.bcrc.mc.bingo.Bingo;
import info.bcrc.mc.bingo.base.service.BingoGame;
import info.bcrc.mc.bingo.base.view.BingoCardView;
import info.bcrc.mc.bingo.impl.classic.model.BingoCardClassic;
import info.bcrc.mc.bingo.util.MessageSender;

public class BingoGameClassic extends BingoGame {
    Bingo plugin;

    public BingoGameClassic(Bingo plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public void setup() {
        super.setup();
    }

    @Override
    public BingoCardClassic createBingoCardForPlayer(Player player) {
        return new BingoCardClassic(plugin.getBingoRandomGenerator().getSelectedItems().toArray(ItemStack[]::new));
    }

    @Override
    public BingoCardView createBingoCardViewForPlayer(Player player, ItemStack[] items) {
        return new BingoCardView(plugin, player, items);
    }

    @Override
    public boolean found(Player player, ItemStack item) {
        int index = playerState.get(player).toggle(item);
        if (index != -1)
            playerView.get(player).toggle(index);

        return index != -1;
    }

    @Override
    public boolean playerFinished(Player player) {
        return getBingoCardByPlayer(player).finished();
    }
}
