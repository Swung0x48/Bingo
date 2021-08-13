package info.bcrc.mc.bingo.base.service;

import info.bcrc.mc.bingo.base.model.BingoCard;
import info.bcrc.mc.bingo.base.view.BingoCardView;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class BingoGame {
    public enum GameState {
        UNINITIALIZED,
        SETUP,
        RUNNING,
        FINISHED
    }

    protected GameState gameState;
    protected HashMap<Player, BingoCard> playerState;
    protected HashMap<Player, BingoCardView> playerView;

    public BingoGame() {
        this.gameState = GameState.UNINITIALIZED;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setup() {
        if (gameState != GameState.FINISHED && gameState != GameState.UNINITIALIZED)
            return;

        this.gameState = GameState.SETUP;
    }

    public void join(Player player) {
        playerState.put(player, null);
        playerView.put(player, null);
    }

    public void start() {
        this.gameState = GameState.RUNNING;
        playerState.forEach((player, _unused) ->
                playerState.put(player, createBingoCardForPlayer(player)));
        playerView.forEach((player, _unused) ->
                playerView.put(player, createBingoCardViewForPlayer(player, playerState.get(player).items)));
    }

    public abstract BingoCard createBingoCardForPlayer(Player player);

    public abstract BingoCardView createBingoCardViewForPlayer(Player player, ItemStack[] items);

    public ArrayList<Player> getPlayersInGame() {
        throw new NotImplementedException();
    }

    public boolean isPlayerInGame(Player player) {
        throw new NotImplementedException();
    }

    public abstract void found(Player player, ItemStack item);

    public void openBingoCard(Player player) {
        throw new NotImplementedException();
    }

    public void stop() {
        throw new NotImplementedException();
    }

    public void finish() {
        this.gameState = GameState.FINISHED;
    }
}