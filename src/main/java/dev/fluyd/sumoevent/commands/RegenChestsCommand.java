package dev.fluyd.sumoevent.commands;

import dev.fluyd.sumoevent.game.GameManager;
import dev.fluyd.sumoevent.utils.LootUtils;
import dev.fluyd.sumoevent.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RegenChestsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("sumoevent.regenchests")) {
            MessagesUtils.sendNoPermissionError((Player) sender);
            return false;
        }

        if (!GameManager.gameStarted) {
            sender.sendMessage(ChatColor.RED + "You can only use this command while a game is running!");
            return false;
        }

        LootUtils.openedChests.forEach((block, timeRemaining) -> {
            LootUtils.openedChests.put(block, 0);
        });

        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendMessage(ChatColor.GREEN + "All chests have been regenerated!");
        });
        return false;
    }
}
