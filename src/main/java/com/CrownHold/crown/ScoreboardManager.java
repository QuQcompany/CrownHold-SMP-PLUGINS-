package com.CrownHold.crown;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import java.util.*;

public class ScoreboardManager {

    private final CrownManager crownManager;
    private final org.bukkit.scoreboard.Scoreboard scoreboard;
    private Objective objective;

    public ScoreboardManager(CrownManager crownManager) {
        this.crownManager = crownManager;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        setupObjective();
    }

    private void setupObjective() {
        objective = scoreboard.registerNewObjective("crown_stats", "dummy",
                ChatColor.GOLD + "👑 " + ChatColor.YELLOW + "Crown Stats");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void updateSidebar() {
        // Clear old scores
        for (String entry : new HashSet<>(scoreboard.getEntries())) {
            scoreboard.resetScores(entry);
        }

        Map<UUID, Integer> stats = crownManager.getStatMap();
        String currentKing = crownManager.getCrownHolder() != null
                ? Bukkit.getOfflinePlayer(crownManager.getCrownHolder()).getName()
                : "None";

        // Header info
        objective.getScore(ChatColor.GRAY + "▬▬▬▬▬▬▬▬▬▬▬▬▬").setScore(15);
        objective.getScore(ChatColor.GOLD + "King: " + ChatColor.YELLOW + currentKing).setScore(14);
        objective.getScore(ChatColor.GRAY + "Stat: " + ChatColor.AQUA + crownManager.getCurrentStat()).setScore(13);
        objective.getScore(ChatColor.GRAY + " ").setScore(12);
        objective.getScore(ChatColor.YELLOW + "Top Players:").setScore(11);

        // Top 5 players
        List<Map.Entry<UUID, Integer>> sorted = new ArrayList<>(stats.entrySet());
        sorted.sort((a, b) -> b.getValue() - a.getValue());

        int slot = 10;
        int rank = 1;
        for (Map.Entry<UUID, Integer> entry : sorted) {
            if (rank > 5) break;
            String name = Bukkit.getOfflinePlayer(entry.getKey()).getName();
            objective.getScore(ChatColor.WHITE + "" + rank + ". " + name + " " + ChatColor.GOLD + entry.getValue()).setScore(slot);
            slot--;
            rank++;
        }

        objective.getScore(ChatColor.GRAY + "  ").setScore(slot - 1);
        objective.getScore(ChatColor.DARK_GRAY + "QuQ Company").setScore(slot - 2);

        // Apply to all players
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(scoreboard);
        }
    }

    public org.bukkit.scoreboard.Scoreboard getScoreboard() { return scoreboard; }
}
