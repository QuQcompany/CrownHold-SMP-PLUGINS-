package com.CrownHold.crown;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import java.util.*;
import java.util.UUID;

public class TablistManager {

    private final Scoreboard scoreboard;
    private Team crownTeam;
    private Team defaultTeam;

    public TablistManager() {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        setupTeams();
    }

    private void setupTeams() {
        crownTeam = scoreboard.registerNewTeam("aaa_crown"); // 'aaa' sorts to top of tab
        crownTeam.setPrefix(ChatColor.GOLD + "👑 " + ChatColor.YELLOW);
        crownTeam.setColor(ChatColor.GOLD);

        defaultTeam = scoreboard.registerNewTeam("zzz_default"); // 'zzz' sorts to bottom
        defaultTeam.setPrefix(ChatColor.GRAY.toString());
    }

    public void setCrown(UUID newKing) {
        // Remove everyone from crown team first
        new HashSet<>(crownTeam.getEntries()).forEach(crownTeam::removeEntry);

        Player king = Bukkit.getPlayer(newKing);
        if (king != null) {
            // Make sure not in default team
            defaultTeam.removeEntry(king.getName());
            crownTeam.addEntry(king.getName());
            king.setScoreboard(scoreboard);
        }
    }

    public void applyToPlayer(Player player) {
        player.setScoreboard(scoreboard);
        if (!crownTeam.getEntries().contains(player.getName())) {
            defaultTeam.addEntry(player.getName());
        }
    }

    public Scoreboard getScoreboard() { return scoreboard; }
}
