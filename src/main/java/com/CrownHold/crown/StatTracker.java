package com.CrownHold.crown;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class StatTracker implements Listener {

    private final CrownManager crownManager;

    public StatTracker(CrownManager crownManager) {
        this.crownManager = crownManager;
    }

    @EventHandler
    public void onKill(EntityDeathEvent e) {
        if (!crownManager.getCurrentStat().equals("kills")) return;
        if (e.getEntity().getKiller() == null) return;
        crownManager.addStat(e.getEntity().getKiller().getUniqueId(), 1);
    }

    @EventHandler
    public void onMine(BlockBreakEvent e) {
        if (!crownManager.getCurrentStat().equals("blocks")) return;
        crownManager.addStat(e.getPlayer().getUniqueId(), 1);
    }

    @EventHandler
    public void onXP(PlayerExpChangeEvent e) {
        if (!crownManager.getCurrentStat().equals("xp")) return;
        if (e.getAmount() <= 0) return;
        crownManager.addStat(e.getPlayer().getUniqueId(), e.getAmount());
    }

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent e) {
        if (!crownManager.getCurrentStat().equals("playerkills")) return;
        if (e.getEntity().getKiller() == null) return;
        crownManager.addStat(e.getEntity().getKiller().getUniqueId(), 1);
    }
}
