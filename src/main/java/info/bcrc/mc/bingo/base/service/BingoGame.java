package info.bcrc.mc.bingo.base.service;

import info.bcrc.mc.bingo.Bingo;
import info.bcrc.mc.bingo.base.model.BingoCard;
import info.bcrc.mc.bingo.base.view.BingoCardView;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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

    protected Bingo plugin;
    protected GameState gameState;
    protected HashMap<Player, BingoCard> playerState;
    protected HashMap<Player, BingoCardView> playerView;

    public BingoGame(Bingo plugin) {
        this.plugin = plugin;
        this.gameState = GameState.UNINITIALIZED;
        playerState = new HashMap<>();
        playerView = new HashMap<>();
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setup() {
        plugin.getLogger().info("Setting up Bingo game...");

        if (gameState != GameState.FINISHED && gameState != GameState.UNINITIALIZED)
        {
            plugin.getLogger().info("Game is already running.");
            return;
        }

        plugin.getBingoItemGenerator().generateNewList();
        this.gameState = GameState.SETUP;
    }

    public void join(Player player) {
        playerState.put(player, null);
        playerView.put(player, null);
    }

    public void start() {
        plugin.getLogger().info("Starting Bingo game...");

        playerState.forEach((player, _unused) -> {
            playerState.put(player, createBingoCardForPlayer(player));
            initializePlayer(player);
        });
        playerView.forEach((player, _unused) ->
                playerView.put(player, createBingoCardViewForPlayer(player, playerState.get(player).items)));

        this.gameState = GameState.RUNNING;
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
        BingoCardView view = playerView.get(player);
        if (view != null)
            view.openView();
    }

    public void stop() {
        throw new NotImplementedException();
    }

    public void finish() {
        this.gameState = GameState.FINISHED;
    }

    private void initializePlayer(Player player)
    {
        for (PotionEffect effect : player.getActivePotionEffects())
        {
            player.removePotionEffect(effect.getType());
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 999999, 255, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 255, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 255, false, false));

        player.getInventory().clear();

        ItemStack bingoGuide = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = bingoGuide.getItemMeta();
        if (meta != null)
            meta.setDisplayName("Bingo Card");
        bingoGuide.setItemMeta(meta);
        player.getInventory().setItem(8, bingoGuide);

        player.setGameMode(GameMode.ADVENTURE);

        player.sendMessage("Click with the nether_star to check the bingo map");
    }
}