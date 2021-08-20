package info.bcrc.mc.bingo.base.view;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import info.bcrc.mc.bingo.Bingo;

public class BingoScoreboard {
    private Bingo plugin;
    private Scoreboard scoreboard;
    private Objective bingoObjective;
    private Objective itemsToWinObjective;

    public BingoScoreboard(Bingo plugin) {
        this.plugin = plugin;
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    }

    public void init() {
        if ((bingoObjective = scoreboard.getObjective("bingo_scoreboard")) != null)
            bingoObjective.unregister();
        bingoObjective = scoreboard.registerNewObjective("bingo_scoreboard", "dummy", ChatColor.GOLD + "[Bingo] " + ChatColor.RESET + "Items Found");

        if ((itemsToWinObjective = scoreboard.getObjective("items_to_win")) != null)
            itemsToWinObjective.unregister();
        itemsToWinObjective = scoreboard.registerNewObjective("items_to_win", "dummy", ChatColor.GOLD + "[Bingo] " + ChatColor.RESET + "Items to Win");
        
        for (Player player : plugin.getBingoGame().getPlayersInGame()) {
            bingoObjective.getScore(player.getName() + " (5 to win)").setScore(0);
            itemsToWinObjective.getScore(player.getName()).setScore(5);
            player.setScoreboard(scoreboard);
        }

        bingoObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void updateEntryName(String oldName, String newName) {
        int score = bingoObjective.getScore(oldName).getScore();
        bingoObjective.getScoreboard().resetScores(oldName);
        bingoObjective.getScore(newName).setScore(score);
    }

    public void increaseFoundItems(Player player, int itemsToWin) {
        plugin.getLogger().info(player.getName() + ": " + Integer.toString(itemsToWin) + " to win");

        String oldName = player.getName() + " (" + Integer.toString(itemsToWinObjective.getScore(player.getName()).getScore()) + " to win)";
        Score score = bingoObjective.getScore(oldName);
        score.setScore(score.getScore() + 1);
        if (itemsToWin == 0)
            updateEntryName(oldName, player.getName() + " (" + ChatColor.GREEN + "won" + ChatColor.RESET + ")");
        else
            updateEntryName(oldName, player.getName() + " (" + Integer.toString(itemsToWin) + " to win)");
    }

    public void setItemsToWin(Player player, int itemsToWin) {
        itemsToWinObjective.getScore(player.getName()).setScore(itemsToWin);
    }
}
