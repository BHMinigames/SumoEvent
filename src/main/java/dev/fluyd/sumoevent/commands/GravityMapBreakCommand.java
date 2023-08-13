package dev.fluyd.sumoevent.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GravityMapBreakCommand implements CommandExecutor {
    public static boolean ALLOW_MAP_BREAKING = false;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (ALLOW_MAP_BREAKING) {
            sender.sendMessage(ChatColor.GRAY + "Map breaking is now toggled " + ChatColor.RED + "OFF" + ChatColor.GRAY + ".");
            ALLOW_MAP_BREAKING = false;
        } else {
            sender.sendMessage(ChatColor.GRAY + "Map breaking is now toggled " + ChatColor.GREEN + "ON" + ChatColor.GRAY + ".");
            ALLOW_MAP_BREAKING = true;
        }

        return false;
    }
}
