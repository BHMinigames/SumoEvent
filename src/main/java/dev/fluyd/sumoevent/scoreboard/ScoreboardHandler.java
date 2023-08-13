package dev.fluyd.sumoevent.scoreboard;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

@Getter
public class ScoreboardHandler {
    private final Scoreboard scoreboard;
    private final Objective objective;

    public ScoreboardHandler(String title) {
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = scoreboard.registerNewObjective("sumoevent", "dummy");
        this.objective.setDisplayName(title);
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void setLine(String entry, int score) {
        Score scoreObj = objective.getScore(entry);
        scoreObj.setScore(score);
    }
}