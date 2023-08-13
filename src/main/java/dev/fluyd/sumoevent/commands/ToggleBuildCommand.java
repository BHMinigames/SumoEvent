package dev.fluyd.sumoevent.commands;

import dev.fluyd.sumoevent.game.GameManager;
import dev.fluyd.sumoevent.utils.MessagesUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleBuildCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("sumoevent.togglebuild")) {
            if (GameManager.canBuild) {
                GameManager.canBuild = false;
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Building/Breaking blocks has been toggled &cOFF."));
            } else {
                GameManager.canBuild = true;
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Building/Breaking blocks has been toggled &aON."));
            }
        } else {
            MessagesUtils.sendNoPermissionError((Player) sender);
        }
        return true;
    }
}
