package dev.fluyd.sumoevent.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ToggleCooldownsCommand implements CommandExecutor {
    public static boolean cooldownsEnabled = true;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (cooldownsEnabled) {
            sender.sendMessage(ChatColor.GREEN + "Cooldowns are now " + ChatColor.RED + "disabled" + ChatColor.GREEN + ".");
            cooldownsEnabled = false;
        } else {
            sender.sendMessage(ChatColor.GREEN + "Cooldowns are now " + ChatColor.GREEN + "enabled" + ChatColor.GREEN + ".");
            cooldownsEnabled = true;
        }
        return true;
    }
}
