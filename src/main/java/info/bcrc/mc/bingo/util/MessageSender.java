package info.bcrc.mc.bingo.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.SoundCategory;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import info.bcrc.mc.bingo.Bingo;

public class MessageSender {
    Bingo plugin;
    Server server;
    ConsoleCommandSender console;

    public MessageSender(Bingo plugin) {
        this.plugin = plugin;
        server = plugin.getServer();
        console = server.getConsoleSender();
    }

    public String getLocationString(Location location) {
        return (Integer.toString((int) Math.floor(location.getX())) + " " + Integer.toString((int) Math.floor(location.getY())) + " " + Integer.toString((int) Math.floor(location.getZ())));
    }

    /**
     * Get the translation key in Minecraft language files.
     * @param material
     * @return "block.minecraft.<i>name</i>" or "item.minecraft.<i>name</i>"
     *  depending on whether the material is a type of block or item.
     */

    public String getItemTranslationKey(Material material) {
        if (material.isBlock())
            return "block.minecraft." + material.getKey().getKey();
        return "item.minecraft." + material.getKey().getKey();
    }

    /**
     * Send a raw JSON message by using <i>/tellraw</i>.
     * @param player
     * @param json raw JSON message
     */
    public void sendRawMessage(Player player, String json) {
        server.dispatchCommand(console, "tellraw " + player.getName() + " " + json);
    }

    /**
     * Broadcast a message to players <i>but not the console</i>.
     * @param message
     */
    public void broadcastMessage(String message) {
        server.getOnlinePlayers().forEach(player -> {
            player.sendMessage(message);
        });
    }

    /**
     * Broadcast a raw JSON message to players by using <i>/tellraw</i>.
     * @param json raw JSON message
     */
    public void broadcastRawMessage(String json) {
        server.dispatchCommand(console, "tellraw @a " + json);
    }

    /**
     * Broadcast a message to <i>current Bingo players</i>.
     * @param message
     */
    public void broadcastBingoMessage(String message) {
        plugin.getBingoGame().getPlayersInGame().forEach(player -> {
            player.sendMessage(message);
        });
    }

    /**
     * Broadcast a raw JSON message to <i>current Bingo players</i> by using <i>/tellraw</i>.
     * @param json raw JSON message
     */
    public void broadcastRawBingoMessage(String json) {
        plugin.getBingoGame().getPlayersInGame().forEach(player -> {
            sendRawMessage(player, json);
        });
    }

    /**
     * Show a title to <i>current Bingo players</i>.
     */
    public void broadcastBingoTitle(String title, String subtitle) {
        plugin.getBingoGame().getPlayersInGame().forEach(player -> {
            player.sendTitle(title, subtitle, 10, 70, 20);
        });
    }

    /**
     * Play a sound to <i>specified players</i>.
     */
    public void playBingoSound(Player player, String sound) {
        player.playSound(player.getLocation(), sound, SoundCategory.MASTER, 1.0f, 1.0f);
    }

    /**
     * Play a sound to <i>current Bingo players</i>.
     */
    public void broadcastBingoSound(String sound) {
        plugin.getBingoGame().getPlayersInGame().forEach(player -> {
            playBingoSound(player, sound);
        });
    }
}
