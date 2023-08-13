package dev.fluyd.sumoevent.commands;

import dev.fluyd.sumoevent.game.GameManager;
import dev.fluyd.sumoevent.utils.MessagesUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleInvincibilityCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("sumoevent.toggleinvincibility")) {
            if (!GameManager.invincibility) {
                GameManager.invincibility = true;
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Invincibility has been toggled &aON."));
            } else {
                GameManager.invincibility = false;
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Invincibility has been toggled &cOFF."));
            }
        } else {
            MessagesUtils.sendNoPermissionError((Player) sender);
        }

        return true;
    }
}
