package info.bcrc.mc.bingo.controller;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import info.bcrc.mc.bingo.Bingo;
import info.bcrc.mc.bingo.base.service.BingoGame.GameState;
import info.bcrc.mc.bingo.impl.classic.service.BingoGameClassic;
import info.bcrc.mc.bingo.util.MessageSender;

public class BingoCommandExecutor implements CommandExecutor, TabCompleter {

    Bingo plugin;

    public BingoCommandExecutor(Bingo plugin) {
        this.plugin = plugin;
    }

    private boolean isPlayer(CommandSender sender) {
        if (sender instanceof Player)
            return true;
        else {
            sender.sendMessage(MessageSender.bingoPrefix + "This command can only be issued by player.");
            return false;
        }
    }

    private void bingoUp(Player player) {
        Location currentLocation = player.getLocation();
        double x = Math.floor(currentLocation.getX());
        double y = Math.floor(currentLocation.getY());
        double z = Math.floor(currentLocation.getZ());

        int highestY = player.getWorld().getHighestBlockYAt((int) x, (int) z);
        int maxY = player.getWorld().getMaxHeight();

        if (y < 80) {
            if (highestY < 120) {
                player.getWorld().getBlockAt((int) x, 120, (int) z).setType(Material.DIRT);
                player.teleport(new Location(player.getWorld(), x + 0.5, 121, z + 0.5));
            }
            else
                player.teleport(new Location(player.getWorld(), x + 0.5, highestY + 1, z + 0.5));
        }
        else if (y >= 80 && y < maxY - 40) {
            if (highestY < y + 40) {
                player.getWorld().getBlockAt((int) x, (int) y + 40, (int) z).setType(Material.DIRT);
                player.teleport(new Location(player.getWorld(), x + 0.5, y + 41, z + 0.5));
            }
            else
                player.teleport(new Location(player.getWorld(), x + 0.5, highestY + 1, z + 0.5));
        }
        else {
            if (highestY != maxY)
                player.getWorld().getBlockAt((int) x, maxY - 1, (int) z).setType(Material.DIRT);
            
            player.teleport(new Location(player.getWorld(), x + 0.5, maxY, z + 0.5));
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("bingo") && args.length > 0) {
            String bingoAction = args[0].toLowerCase();
            switch (bingoAction) {
                case "setup":
                    if (plugin.bingoGame != null)
                        plugin.getMessageSender().broadcastMessage(MessageSender.bingoPrefix + ChatColor.YELLOW + "Warning: Any existing Bingo games has been stopped.");
                    plugin.bingoGame = new BingoGameClassic(plugin);
                    plugin.bingoGame.setup();
                    return true;
                case "join":
                    if (!isPlayer(sender))
                        return true;

                    if (plugin.getBingoGame() == null) {
                        sender.sendMessage(MessageSender.bingoErrorPrefix + "No bingo game has been set up.");
                        sender.sendMessage(MessageSender.bingoPrefix + "Use /bingo setup to set one up.");
                        return true;
                    }

                    if (plugin.bingoGame.isPlayerInGame((Player) sender)) {
                        sender.sendMessage(MessageSender.bingoErrorPrefix + "You've already joined the game.");
                        return true;
                    }

                    plugin.bingoGame.join((Player) sender);
                    return true;
                case "start":
                    if (plugin.bingoGame.getGameState() != GameState.SETUP)
                        sender.sendMessage(MessageSender.bingoErrorPrefix + "The bingo has not been set up");
                    else
                        plugin.bingoGame.start();

                    return true;
                case "status":
                    plugin.getLogger().info(plugin.bingoGame.getGameState().name());
                    return true;
                case "stop":
                    if (plugin.bingoGame.getGameState() != GameState.UNINITIALIZED) {
                        sender.sendMessage(MessageSender.bingoPrefix + "Stopped the current running bingo.");
                        plugin.bingoGame.stop();
                    }
                    else
                        sender.sendMessage(MessageSender.bingoErrorPrefix + "No bingo game is running currently.");
                    return true;
                case "quit":
                    if (!isPlayer(sender))
                        return true;

                    if (plugin.bingoGame == null || plugin.bingoGame.getGameState() == GameState.UNINITIALIZED) {
                        sender.sendMessage(MessageSender.bingoErrorPrefix + "No bingo game has been set up.");
                        return true;
                    }

                    if (args.length > 1 && args[1].equalsIgnoreCase("confirm"))
                        plugin.bingoGame.playerQuit((Player) sender);
                    else {
                        sender.sendMessage(MessageSender.bingoPrefix + "Are you sure to quit this game?");
                        sender.sendMessage(MessageSender.bingoPrefix + "Type " + ChatColor.GREEN + "/bingo quit confirm" + ChatColor.RESET + " to quit it.");
                    }
                    return true;
                case "viewcard":
                    if (!isPlayer(sender))
                        return true;

                    if (plugin.bingoGame.getGameState() == GameState.RUNNING)
                        plugin.getBingoGame().openBingoCard((Player) sender);
                    else
                        sender.sendMessage(MessageSender.bingoErrorPrefix + "You can only view the card during a running bingo game!");
                    return true;
                case "up":
                    if (!isPlayer(sender))
                        return true;

                    bingoUp((Player) sender);
                    return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("bingo")) {
            if (args.length <= 1)
                return Arrays.asList("setup", "join", "quit", "start", "status", "viewcard", "up", "stop");
            if (args.length > 1 && args[0].equalsIgnoreCase("quit"))
                return Arrays.asList("confirm");
        }
        return null;
    }
}
