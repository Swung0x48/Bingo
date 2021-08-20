package info.bcrc.mc.bingo.impl.classic.service;

import info.bcrc.mc.bingo.base.event.BingoFinishedEvent;
import info.bcrc.mc.bingo.base.event.BingoFoundEvent;
import info.bcrc.mc.bingo.impl.classic.view.BingoCardViewClassic;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import info.bcrc.mc.bingo.Bingo;
import info.bcrc.mc.bingo.base.service.BingoGame;
import info.bcrc.mc.bingo.impl.classic.model.BingoCardClassic;

import java.util.UUID;

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
    public BingoCardClassic createBingoCardForPlayer(UUID uuid) {
        return new BingoCardClassic(uuid, plugin.getBingoRandomGenerator().getSelectedItems().toArray(ItemStack[]::new));
    }

    @Override
    public BingoCardViewClassic createBingoCardViewForPlayer(Player player, ItemStack[] items) {
        return new BingoCardViewClassic(plugin, player, items);
    }

    @Override
    public boolean playerThrows(Player player, ItemStack item) {
        boolean finished = hasPlayerFinished(player);
        int index = playerState.get(player.getUniqueId()).setFound(item);
        if (index != -1) {
            onFound(player, item);
            playerView.get(player.getUniqueId()).setFound(index);
            if (!finished && hasPlayerFinished(player)) {
                onPlayerFinished(player);
            }
        }

        return index != -1;
    }

    @Override
    public void onFound(Player player, ItemStack item) {
        Bukkit.getPluginManager().callEvent(new BingoFoundEvent(player, item));
    }

    @Override
    public boolean hasPlayerFinished(Player player) {
        return getBingoCardByPlayer(player).hasFinished();
    }

    @Override
    public void onPlayerFinished(Player player) {
        Bukkit.getPluginManager().callEvent(new BingoFinishedEvent(player));
    }
}
