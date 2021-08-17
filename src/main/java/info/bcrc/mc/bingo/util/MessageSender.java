package info.bcrc.mc.bingo.util;

import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import info.bcrc.mc.bingo.Bingo;

public class MessageSender {
    Bingo plugin;
    Server server;
    ConsoleCommandSender console;

    static Logger logger;

    public MessageSender(Bingo plugin) {
        this.plugin = plugin;
        server = plugin.getServer();
        console = server.getConsoleSender();
        logger = plugin.getLogger();
    }

    /**
     * Get the translation key in Minecraft language files.
     * @param material
     * @return "block.minecraft.<i>name</i>" or "item.minecraft.<i>name</i>"
     *  depending on whether the material is a type of block or item.
     */

    public static String getItemTranslationKey(Material material) {
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
}
