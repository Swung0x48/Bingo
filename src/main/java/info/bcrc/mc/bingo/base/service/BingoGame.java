package info.bcrc.mc.bingo.base.service;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import info.bcrc.mc.bingo.util.MessageSender;

public abstract class BingoGame {
    public enum GameState {
        UNINITIALIZED, SETUP, RUNNING, FINISHED
    }

    protected Bingo plugin;
    protected GameState gameState;
    protected HashMap<UUID, BingoCard> playerState;
    protected HashMap<UUID, BingoCardView> playerView;

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
        plugin.getLogger().info(ChatColor.GOLD + "[Bingo] " + ChatColor.RESET + "Setting up Bingo game...");

        if (gameState != GameState.FINISHED && gameState != GameState.UNINITIALIZED) {
            plugin.getLogger().info(ChatColor.GOLD + "[Bingo] " + ChatColor.RESET + "Game is already running.");
            return;
        }

        plugin.getBingoRandomGenerator().generateNewList();
        this.gameState = GameState.SETUP;

        MessageSender.broadcastMessage(ChatColor.GOLD + "[Bingo] " + ChatColor.RESET + "A bingo game has been set up.");
        MessageSender.broadcastRawMessage("{\"text\": \"\", \"extra\": [{\"text\": \"[Bingo] \", \"color\": \"gold\"}, {\"text\": \"Type \"}, {\"text\": \"/bingo join\", \"color\": \"green\"}, {\"text\": \" or \"}, {\"text\": \"click here to join it directly\", \"underlined\": \"true\", \"clickEvent\": {\"action\": \"run_command\", \"value\": \"/bingo join\"}, \"hoverEvent\": {\"action\": \"show_text\", \"value\": \"click here to join it directly\"}}]}");
    }

    public void join(Player player) {
        if (!playerState.containsKey(player.getUniqueId()))
            playerState.put(player.getUniqueId(), null);
        if (!playerView.containsKey(player.getUniqueId()))
            playerView.put(player.getUniqueId(), null);
        player.sendMessage(ChatColor.GOLD + "[Bingo] " + ChatColor.RESET + "You've joined the bingo game.");
    }

    public void start() {
        plugin.getLogger().info(ChatColor.GOLD + "[Bingo] " + ChatColor.RESET + "Starting Bingo game...");

        playerState.forEach((uuid, _unused) ->
            playerState.put(uuid, createBingoCardForPlayer(uuid)));
        playerView.forEach((uuid, _unused) -> {
            playerView.put(uuid,
                    createBingoCardViewForPlayer(Bukkit.getPlayer(uuid), playerState.get(uuid).items));
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                initializePlayer(player);
                player.teleport(plugin.getBingoRandomGenerator().getLocation(player.getWorld()));
                player.sendMessage(ChatColor.GOLD + "[Bingo] " + ChatColor.RESET + "Bingo game started!");
            }
        });

        this.gameState = GameState.RUNNING;
    }

    public void reconnectPlayer(Player player) {

    }

    public void stop() {
        this.gameState = GameState.UNINITIALIZED;
    }

    public void finish() {
        this.gameState = GameState.FINISHED;
    }

    public abstract BingoCard createBingoCardForPlayer(UUID uuid);

    public abstract BingoCardView createBingoCardViewForPlayer(Player player, ItemStack[] items);

    public BingoCard getBingoCardByPlayer(Player player) {
        return playerState.get(player.getUniqueId());
    }

    public BingoCardView getBingoCardViewByPlayer(Player player) {
        return playerView.get(player.getUniqueId());
    }

    public boolean cardViewBelongsToPlayer(Inventory inventory, Player player) {
        return inventory == getBingoCardViewByPlayer(player).getInventory();
    }

    public Set<Player> getPlayersInGame() {
        return playerState.keySet().stream().map(Bukkit::getPlayer).collect(Collectors.toSet());
    }

    public boolean isPlayerInGame(Player player) {
        return playerState.containsKey(player.getUniqueId());
    }

    public abstract boolean playerThrows(Player player, ItemStack item);

    public abstract void onFound(Player player, ItemStack item);

    public abstract boolean hasPlayerFinished(Player player);

    public abstract void onPlayerFinished(Player player);

    public void openBingoCard(Player player) {
        BingoCardView view = playerView.get(player.getUniqueId());
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

        MessageSender.sendRawMessage(player, "{\"text\": \"\", \"extra\": [{\"text\": \"[Bingo] \", \"color\": \"gold\"}, {\"text\": \"Click with the [\"}, {\"translate\": \""
                + MessageSender.getItemTranslationKey(Material.NETHER_STAR) + "\", \"color\": \"yellow\", \"hoverEvent\": {\"action\": \"show_item\", \"value\": \"{\\\"id\\\": \\\"nether_star\\\", \\\"Count\\\": 1}\"}}, {\"text\": \"] to check the bingo map\"}]}");
    }
}