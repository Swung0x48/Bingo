package info.bcrc.mc.bingo.base.service;

import java.util.HashMap;
import java.util.Set;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import info.bcrc.mc.bingo.Bingo;
import info.bcrc.mc.bingo.base.model.BingoCard;
import info.bcrc.mc.bingo.base.view.BingoCardView;

public abstract class BingoGame {
    public enum GameState {
        UNINITIALIZED, SETUP, RUNNING, FINISHED
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

        if (gameState != GameState.FINISHED && gameState != GameState.UNINITIALIZED) {
            plugin.getLogger().info("Game is already running.");
            return;
        }

        plugin.getBingoRandomGenerator().generateNewList();
        this.gameState = GameState.SETUP;

        plugin.getServer().getOnlinePlayers().forEach(player -> {
            player.sendMessage("A bingo game has been set up.");
            player.sendMessage("Type /bingo join to join it.");
        });
    }

    public void join(Player player) {
        playerState.put(player, null);
        playerView.put(player, null);
        player.sendMessage("You've joined the bingo game.");
    }

    public void start() {
        plugin.getLogger().info("Starting Bingo game...");

        playerState.forEach((player, _unused) ->
            playerState.put(player, createBingoCardForPlayer(player)));
        playerView.forEach((player, _unused) -> {
            playerView.put(player,
                    createBingoCardViewForPlayer(player, playerState.get(player).items));
            initializePlayer(player);
            player.teleport(plugin.getBingoRandomGenerator().getLocation(player.getWorld()));
            player.sendMessage("Bingo game started!");
        });

        this.gameState = GameState.RUNNING;
    }

    public void stop() {
        throw new NotImplementedException();
    }

    public void finish() {
        this.gameState = GameState.FINISHED;
    }

    public abstract BingoCard createBingoCardForPlayer(Player player);

    public abstract BingoCardView createBingoCardViewForPlayer(Player player, ItemStack[] items);

    public BingoCard getBingoCardByPlayer(Player player) {
        return playerState.get(player);
    }

    public BingoCardView getBingoCardViewByPlayer(Player player) {
        return playerView.get(player);
    }

    public boolean cardViewBelongsToPlayer(Inventory inventory, Player player) {
        return inventory == getBingoCardViewByPlayer(player).getInventory();
    }

    public Set<Player> getPlayersInGame() {
        return playerState.keySet();
    }

    public boolean isPlayerInGame(Player player) {
        return playerState.containsKey(player);
    }

    public abstract void found(Player player, ItemStack item);

    public void openBingoCard(Player player) {
        BingoCardView view = playerView.get(player);
        if (view != null)
            view.openView();
    }

    private void initializePlayer(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
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