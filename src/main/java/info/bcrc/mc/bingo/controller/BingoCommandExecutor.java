package info.bcrc.mc.bingo.controller;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import info.bcrc.mc.bingo.Bingo;
import info.bcrc.mc.bingo.base.service.BingoGame;
import info.bcrc.mc.bingo.impl.classic.service.BingoGameClassic;

public class BingoCommandExecutor implements CommandExecutor, TabCompleter {

    Bingo plugin;

    public BingoCommandExecutor(Bingo plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("bingo") && args.length > 0) {
            String bingoAction = args[0].toLowerCase();
            switch (bingoAction) {
                case "setup":
                    plugin.bingoGame = new BingoGameClassic(plugin);
                    plugin.bingoGame.setup();
                    return true;
                case "join":
                    Player p = null;
                    if (sender instanceof Player)
                        p = (Player) sender;
                    if (p == null) {
                        sender.sendMessage(ChatColor.GOLD + "[Bingo] " + ChatColor.RESET + "This command can only be issued by player.");
                        return true;
                    }

                    if (plugin.getBingoGame() == null) {
                        sender.sendMessage(ChatColor.GOLD + "[Bingo] " + ChatColor.RESET + "No bingo game has been set up.");
                        sender.sendMessage(ChatColor.GOLD + "[Bingo] " + ChatColor.RESET + "Use /bingo setup to set one up.");
                        return true;
                    }

                    plugin.bingoGame.join(p);
                    return true;
                case "start":
                    if (plugin.bingoGame.getGameState() != BingoGame.GameState.SETUP)
                        sender.sendMessage(ChatColor.GOLD + "[Bingo] " + ChatColor.RESET + "Error: The bingo has not been set up");

                    plugin.bingoGame.start();

                    return true;
                case "status":
                    plugin.getLogger().info(plugin.bingoGame.getGameState().name());
                    return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("bingo")) {
            if (args.length <= 1) {
                return Arrays.asList("setup", "join", "start", "status");
            }
        }
        return null;
    }
}
