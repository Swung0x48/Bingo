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
import info.bcrc.mc.bingo.base.service.BingoGame.GameState;
import info.bcrc.mc.bingo.impl.classic.service.BingoGameClassic;

public class BingoCommandExecutor implements CommandExecutor, TabCompleter {

    Bingo plugin;

    public BingoCommandExecutor(Bingo plugin) {
        this.plugin = plugin;
    }

    private boolean isPlayer(CommandSender sender) {
        if (sender instanceof Player)
            return true;
        else {
            sender.sendMessage(ChatColor.GOLD + "[Bingo] " + ChatColor.RESET + "This command can only be issued by player.");
            return false;
        }
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
                    if (!isPlayer(sender))
                        return true;

                    if (plugin.getBingoGame() == null) {
                        sender.sendMessage(ChatColor.GOLD + "[Bingo] " + ChatColor.RESET + "Error: No bingo game has been set up.");
                        sender.sendMessage(ChatColor.GOLD + "[Bingo] " + ChatColor.RESET + "Use /bingo setup to set one up.");
                        return true;
                    }

                    plugin.bingoGame.join((Player) sender);
                    return true;
                case "start":
                    if (plugin.bingoGame.getGameState() != BingoGame.GameState.SETUP)
                        sender.sendMessage(ChatColor.GOLD + "[Bingo] " + ChatColor.RESET + "Error: The bingo has not been set up");
                    else
                        plugin.bingoGame.start();

                    return true;
                case "status":
                    plugin.getLogger().info(plugin.bingoGame.getGameState().name());
                    return true;
                case "stop":
                    if (plugin.bingoGame.getGameState() != GameState.UNINITIALIZED) {
                        sender.sendMessage(ChatColor.GOLD + "[Bingo] " + ChatColor.RESET + "Stopped the current running bingo.");
                        plugin.bingoGame.stop();
                    }
                    else
                        sender.sendMessage(ChatColor.GOLD + "[Bingo] " + ChatColor.RESET + "Error: No bingo game is running currently.");
                    return true;
                case "quit":
                    if (!isPlayer(sender))
                        return true;
                    
                    if (plugin.bingoGame == null || plugin.bingoGame.getGameState() == GameState.UNINITIALIZED) {
                        sender.sendMessage(ChatColor.GOLD + "[Bingo] " + ChatColor.RESET + "Error: No bingo game has been set up.");
                        return true;
                    }
                    
                    if (args.length > 1 && args[1].equalsIgnoreCase("confirm"))
                        plugin.bingoGame.playerQuit((Player) sender);
                    else {
                        sender.sendMessage(ChatColor.GOLD + "[Bingo] " + ChatColor.RESET + "Are you sure to quit this game?");
                        sender.sendMessage(ChatColor.GOLD + "[Bingo] " + ChatColor.RESET + "Type " + ChatColor.GREEN + "/bingo quit confirm" + ChatColor.RESET + " to quit it.");
                    }
                    return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("bingo")) {
            if (args.length <= 1)
                return Arrays.asList("setup", "join", "quit", "start", "status", "stop");
            if (args.length > 1 && args[0].equalsIgnoreCase("quit"))
                return Arrays.asList("confirm");
        }
        return null;
    }
}
