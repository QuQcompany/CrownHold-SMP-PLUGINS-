package com.CrownHold.crown;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.*;

public class CrownManager {

    private final JavaPlugin plugin;
    private UUID crownHolder = null;
    private String currentStat = "kills";
    private final Map<UUID, Integer> statMap = new HashMap<>();
    private final TablistManager tablistManager;
    private final ScoreboardManager scoreboardManager;

    public CrownManager(JavaPlugin plugin, TablistManager tablistManager, ScoreboardManager scoreboardManager) {
        this.plugin = plugin;
        this.tablistManager = tablistManager;
        this.scoreboardManager = scoreboardManager;
        startCycle();
    }

    private void startCycle() {
        long ticks = 20L * 60;
        Bukkit.getScheduler().runTaskTimer(plugin, this::resetCycle, ticks, ticks);
        // Update sidebar every 5 seconds
        Bukkit.getScheduler().runTaskTimer(plugin, scoreboardManager::updateSidebar, 20L * 5, 20L * 5);
    }

    public void resetCycle() {
        UUID winner = getTopPlayer();

        if (winner != null) {
            UUID previousKing = crownHolder;
            crownHolder = winner;
            Player p = Bukkit.getPlayer(winner);
            String newName = p != null ? p.getName() : Bukkit.getOfflinePlayer(winner).getName();

            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(ChatColor.GOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
            Bukkit.broadcastMessage(ChatColor.YELLOW + "        👑  NEW KING CROWNED  👑");
            Bukkit.broadcastMessage(ChatColor.WHITE + "         " + ChatColor.GOLD + newName + ChatColor.WHITE + " is the new King!");

            if (previousKing != null && !previousKing.equals(winner)) {
                String oldName = Bukkit.getOfflinePlayer(previousKing).getName();
                Bukkit.broadcastMessage(ChatColor.RED + "        👎 " + newName + " dethroned " + oldName + "!");
            }

            Bukkit.broadcastMessage(ChatColor.GRAY + "        Stat: " + ChatColor.AQUA + currentStat);
            Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "        Made by " + ChatColor.GOLD + "QuQ Company");
            Bukkit.broadcastMessage(ChatColor.GOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
            Bukkit.broadcastMessage("");

            tablistManager.setCrown(crownHolder);
            for (Player online : Bukkit.getOnlinePlayers()) {
                tablistManager.applyToPlayer(online);
            }

            if (p != null) p.sendMessage(ChatColor.GOLD + "👑 You have been crowned King!");
        }

        statMap.clear();
    }

    private UUID getTopPlayer() {
        return statMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public void addStat(UUID uuid, int amount) {
        statMap.merge(uuid, amount, Integer::sum);
    }

    public UUID getCrownHolder() { return crownHolder; }
    public String getCurrentStat() { return currentStat; }
    public void setCurrentStat(String stat) { this.currentStat = stat; }
    public Map<UUID, Integer> getStatMap() { return statMap; }
}
