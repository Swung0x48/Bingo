package info.bcrc.mc.bingo.base.service;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public abstract class BingoGame {
    public enum GameState {
        UNINITIALIZED,
        SETUP,
        RUNNING,
        FINISHED
    }

    protected GameState gameState;

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
        throw new NotImplementedException();
    }

    public void start() {
        this.gameState = GameState.RUNNING;
    }

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