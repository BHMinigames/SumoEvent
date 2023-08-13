package dev.fluyd.sumoevent.commands;

import dev.fluyd.sumoevent.game.GameManager;
import dev.fluyd.sumoevent.utils.MessagesUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleNoFallCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("sumoevent.togglenofall")) {
            if (GameManager.noFall) {
                GameManager.noFall = false;
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7NoFall has been toggled &cOFF."));
            } else {
                GameManager.noFall = true;
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7NoFall has been toggled &aON."));
            }
        } else {
            MessagesUtils.sendNoPermissionError((Player) sender);
        }
        return true;
    }
}
