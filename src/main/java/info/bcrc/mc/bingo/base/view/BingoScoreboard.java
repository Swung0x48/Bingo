package info.bcrc.mc.bingo.base.view;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import info.bcrc.mc.bingo.Bingo;

public class BingoScoreboard {
    private Bingo plugin;
    private Objective bingoObjective;

    public BingoScoreboard(Bingo plugin) {
        this.plugin = plugin;
    }

    public void init() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();
        if ((bingoObjective = board.getObjective("bingo_scoreboard")) == null)
            bingoObjective = board.registerNewObjective("bingo_scoreboard", "dummy", ChatColor.GOLD + "[Bingo] " + ChatColor.RESET + "Items Found");
        
        for (Player player : plugin.getBingoGame().getPlayersInGame())
            bingoObjective.getScore(player.getName()).setScore(0);

        bingoObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void increaseScore(Player player) {
        Score score = bingoObjective.getScore(player.getName());
        score.setScore(score.getScore() + 1);
    }
}
