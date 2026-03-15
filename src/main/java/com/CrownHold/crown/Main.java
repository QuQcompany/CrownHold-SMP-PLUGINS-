package com.CrownHold.crown;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    private TablistManager tablistManager;
    private CrownManager crownManager;
    private ScoreboardManager scoreboardManager;
    private CrownGUI crownGUI;

    @Override
    public void onEnable() {
        tablistManager = new TablistManager();
        scoreboardManager = new ScoreboardManager(null); // temp
        crownManager = new CrownManager(this, tablistManager, scoreboardManager);
        scoreboardManager = new ScoreboardManager(crownManager);
        crownGUI = new CrownGUI(crownManager);

        // Restart with correct reference
        tablistManager = new TablistManager();
        crownManager = new CrownManager(this, tablistManager, scoreboardManager);

        getServer().getPluginManager().registerEvents(new StatTracker(crownManager), this);
        getServer().getPluginManager().registerEvents(new ChatListener(crownManager), this);
        getServer().getPluginManager().registerEvents(crownGUI, this);
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("crown").setExecutor(new CrownCommand(crownManager, crownGUI));

        getLogger().info(net.md_5.bungee.api.ChatColor.GOLD + "Crown by QuQ Company enabled!");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        tablistManager.applyToPlayer(e.getPlayer());
        scoreboardManager.updateSidebar();
    }
}
