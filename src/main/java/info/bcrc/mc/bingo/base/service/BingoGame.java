package info.bcrc.mc.bingo.base.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import info.bcrc.mc.bingo.Bingo;
import info.bcrc.mc.bingo.base.event.BingoPlayerQuitEvent;
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
        plugin.getLogger().info(MessageSender.bingoPrefix + "Setting up Bingo game...");

        if (gameState != GameState.FINISHED && gameState != GameState.UNINITIALIZED) {
            plugin.getLogger().info(MessageSender.bingoPrefix + "Game is already running.");
            return;
        }

        plugin.getBingoPreGameListener().register();

        ArrayList<ItemStack> list = plugin.getBingoRandomGenerator().generateNewList();
        plugin.getLogger().info("Item generated: ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); ++i) {
            if (i % 9 == 0) {
                plugin.getLogger().info(sb.toString());
                sb.setLength(0);
            }
            sb.append(list.get(i).getType().name()).append(' ');
        }
        this.gameState = GameState.SETUP;

        plugin.getMessageSender().broadcastMessage(MessageSender.bingoPrefix + "A bingo game has been set up.");
        plugin.getMessageSender().broadcastRawMessage("{\"text\": \"\", \"extra\": [" + MessageSender.bingoJsonPrefix
                + ", {\"text\": \"Type \"}, {\"text\": \"/bingo join\", \"color\": \"green\"}, {\"text\": \" or \"}, {\"text\": \"click here to join it directly\", \"underlined\": \"true\", \"clickEvent\": {\"action\": \"run_command\", \"value\": \"/bingo join\"}, \"hoverEvent\": {\"action\": \"show_text\", \"value\": \"click here to join it directly\"}}]}");
    }

    public void join(Player player) {
        if (!playerState.containsKey(player.getUniqueId()))
            playerState.put(player.getUniqueId(), null);
        if (!playerView.containsKey(player.getUniqueId()))
            playerView.put(player.getUniqueId(), null);

        initializePlayer(player, false);
        plugin.getMessageSender().broadcastMessage(ChatColor.GOLD + "[Bingo] " + ChatColor.AQUA + player.getName()
                + ChatColor.RESET + " has joined the bingo game.");
    }

    public void start() {
        plugin.getLogger().info(MessageSender.bingoPrefix + "Starting Bingo game...");
        plugin.getBingoScoreboard().init();

        playerState.forEach((uuid, _unused) -> playerState.put(uuid, createBingoCardForPlayer(uuid)));
        playerView.forEach((uuid, _unused) -> {
            playerView.put(uuid, createBingoCardViewForPlayer(Bukkit.getPlayer(uuid), playerState.get(uuid).items));
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.sendMessage(MessageSender.bingoPrefix + "Bingo game started!");
            }
        });

        plugin.getServer().getWorlds().forEach(world -> world.setTime(0));

        plugin.getBingoPreGameListener().unregister();
        plugin.getBingoListener().register();

        this.gameState = GameState.RUNNING;
    }

    public void stop() {
        plugin.getBingoListener().unregister();

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

    public boolean isActiveBingoCardView(Inventory inventory) {
        for (Map.Entry<UUID, BingoCardView> entry : playerView.entrySet()) {
            if (entry.getValue().getInventory() == inventory)
                return true;
        }

        return false;
    }

    public abstract boolean playerThrows(Player player, ItemStack item);

    public abstract boolean hasPlayerFinished(Player player);

    public abstract void onPlayerFinished(Player player);

    public void openBingoCard(Player player) {
        BingoCardView view = playerView.get(player.getUniqueId());
        if (view != null)
            view.openView();
    }

    public boolean playerQuit(Player player) {
        if (!isPlayerInGame(player))
            return false;
        try {
            playerState.remove(player.getUniqueId());
            playerView.remove(player.getUniqueId());
        } catch (NullPointerException e) {
            plugin.getLogger().warning("Exception when playerQuit");
            e.printStackTrace();
        }
        Bukkit.getPluginManager().callEvent(new BingoPlayerQuitEvent(player));
        return true;
    }

    public void initializePlayer(Player player, boolean fromRespawn) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 999999, 255, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 255, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 255, false, false));

        player.getInventory().clear();

        ItemStack bingoGuide = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = bingoGuide.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RESET + "Bingo Card");
            meta.setLore(List.of(ChatColor.RESET + "Click to view the bingo card"));
        }
        bingoGuide.setItemMeta(meta);
        player.getInventory().setItem(8, bingoGuide);

        player.setGameMode(GameMode.SURVIVAL);

        if (!fromRespawn) {
            Server server = plugin.getServer();

            if (plugin.getBingoConfig().getDifferentLocationPerPlayer())
                plugin.getBingoRandomGenerator().generateRandomNonLiquidLocation(player.getWorld());

            Location randomLocation = plugin.getBingoRandomGenerator().getLocation(player.getWorld());
            player.teleport(randomLocation);
            server.dispatchCommand(server.getConsoleSender(), "spawnpoint " + player.getName() + " "
                    + plugin.getMessageSender().getLocationString(randomLocation));

            server.dispatchCommand(server.getConsoleSender(), "advancement revoke " + player.getName() + " everything");
            
            plugin.getMessageSender().sendRawMessage(player, "{\"text\": \"\", \"extra\": ["
                    + MessageSender.bingoJsonPrefix + ", {\"text\": \"Click with the [\"}, {\"translate\": \""
                    + plugin.getMessageSender().getItemTranslationKey(Material.NETHER_STAR)
                    + "\", \"color\": \"yellow\", \"hoverEvent\": {\"action\": \"show_item\", \"value\": \"{\\\"id\\\": \\\"nether_star\\\", \\\"Count\\\": 1}\"}}, {\"text\": \"] to check the bingo map\"}]}");
        }
    }
}