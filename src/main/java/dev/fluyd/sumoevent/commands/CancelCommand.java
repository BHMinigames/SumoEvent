package dev.fluyd.sumoevent.commands;

import dev.fluyd.sumoevent.game.GameManager;
import dev.fluyd.sumoevent.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CancelCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("sumoevent.cancel")) {
            if (GameManager.gameStarted && !GameManager.inCage) {
                GameManager.stopGame(false);
                Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.RED + "An admin has cancelled the game!"));
            } else {
                sender.sendMessage(ChatColor.RED + "A game is not currently running!");
            }
        } else {
            MessagesUtils.sendNoPermissionError((Player) sender);
        }
        return true;
    }
}
