package com.CrownHold.crown;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final CrownManager crownManager;

    public ChatListener(CrownManager crownManager) {
        this.crownManager = crownManager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (e.getPlayer().getUniqueId().equals(crownManager.getCrownHolder())) {
            e.setFormat(ChatColor.GOLD + "👑 " + ChatColor.YELLOW + e.getPlayer().getName()
                + ChatColor.WHITE + ": " + e.getMessage());
        }
    }
}
