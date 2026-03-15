package com.CrownHold.crown;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.*;

public class CrownGUI implements Listener {

    private final CrownManager crownManager;
    private static final String GUI_TITLE = ChatColor.GOLD + "👑 Crown Stats";

    public CrownGUI(CrownManager crownManager) {
        this.crownManager = crownManager;
    }

    public void openGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, GUI_TITLE);

        // Crown holder info
        ItemStack crown = new ItemStack(Material.GOLDEN_HELMET);
        ItemMeta crownMeta = crown.getItemMeta();
        String kingName = crownManager.getCrownHolder() != null
                ? Bukkit.getOfflinePlayer(crownManager.getCrownHolder()).getName()
                : "None";
        crownMeta.setDisplayName(ChatColor.GOLD + "👑 Current King");
        crownMeta.setLore(Arrays.asList(
                ChatColor.YELLOW + kingName,
                ChatColor.GRAY + "Reigns until next reset"
        ));
        crown.setItemMeta(crownMeta);
        inv.setItem(4, crown);

        // Current stat
        ItemStack statItem = new ItemStack(Material.PAPER);
        ItemMeta statMeta = statItem.getItemMeta();
        statMeta.setDisplayName(ChatColor.AQUA + "Current Stat: " + crownManager.getCurrentStat());
        statItem.setItemMeta(statMeta);
        inv.setItem(11, statItem);

        // Top players
        List<Map.Entry<UUID, Integer>> sorted = new ArrayList<>(crownManager.getStatMap().entrySet());
        sorted.sort((a, b) -> b.getValue() - a.getValue());

        int slot = 13;
        for (int i = 0; i < Math.min(5, sorted.size()); i++) {
            Map.Entry<UUID, Integer> entry = sorted.get(i);
            String name = Bukkit.getOfflinePlayer(entry.getKey()).getName();
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            ItemMeta meta = head.getItemMeta();
            meta.setDisplayName(ChatColor.YELLOW + "#" + (i + 1) + " " + name);
            meta.setLore(Arrays.asList(ChatColor.GOLD + "" + entry.getValue() + " " + crownManager.getCurrentStat()));
            head.setItemMeta(meta);
            inv.setItem(slot, head);
            slot++;
        }

        // Fill empty slots with glass
        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.setDisplayName(" ");
        filler.setItemMeta(fillerMeta);
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null) inv.setItem(i, filler);
        }

        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equals(GUI_TITLE)) {
            e.setCancelled(true); // Nobody can take items
        }
    }
}
