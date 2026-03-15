package com.CrownHold.crown;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import java.util.Map;
import java.util.UUID;

public class CrownCommand implements CommandExecutor {

    private final CrownManager crownManager;
    private final CrownGUI crownGUI;

    public CrownCommand(CrownManager crownManager, CrownGUI crownGUI) {
        this.crownManager = crownManager;
        this.crownGUI = crownGUI;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0 || args[0].equalsIgnoreCase("menu")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can open the menu.");
                return true;
            }
            crownGUI.openGUI((Player) sender);
            return true;
        }

        if (!sender.hasPermission("crown.admin")) {
            sender.sendMessage(ChatColor.RED + "No permission.");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "setstat":
                if (args.length < 2) { sender.sendMessage(ChatColor.RED + "Specify a stat."); return true; }
                crownManager.setCurrentStat(args[1].toLowerCase());
                sender.sendMessage(ChatColor.GREEN + "Stat set to: " + args[1]);
                break;
            case "top":
                crownManager.getStatMap().entrySet().stream()
                        .sorted(Map.Entry.<UUID, Integer>comparingByValue().reversed())
                        .limit(5)
                        .forEach(e -> {
                            String name = Bukkit.getOfflinePlayer(e.getKey()).getName();
                            sender.sendMessage(ChatColor.YELLOW + name + ": " + e.getValue());
                        });
                break;
            case "reset":
                crownManager.resetCycle();
                sender.sendMessage(ChatColor.GREEN + "Cycle reset manually.");
                break;
            default:
                sender.sendMessage(ChatColor.GOLD + "/crown menu");
                sender.sendMessage(ChatColor.GOLD + "/crown setstat <kills|blocks|xp|playerkills>");
                sender.sendMessage(ChatColor.GOLD + "/crown top");
                sender.sendMessage(ChatColor.GOLD + "/crown reset");
        }
        return true;
    }
}
