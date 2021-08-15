package info.bcrc.mc.bingo.util;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import info.bcrc.mc.bingo.Bingo;

public class MessageSender {
    static Server server;
    static ConsoleCommandSender console;

    public MessageSender(Bingo plugin) {
        server = plugin.getServer();
        console = server.getConsoleSender();
    }

    public static String getItemTranslationKey(Material material) {
        if (material.isBlock())
            return "block.minecraft." + material.getKey().getKey();
        return "item.minecraft." + material.getKey().getKey();
    }

    public static void sendRawMessage(Player player, String json) {
        server.dispatchCommand(console, "tellraw " + player.getName() + " " + json);
    }
}