package dev.fluyd.sumoevent.commands;

import dev.fluyd.sumoevent.game.GameManager;
import dev.fluyd.sumoevent.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class StartCommand implements CommandExecutor {
    ArrayList<Player> validPlayers = new ArrayList<Player>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("sumoevent.start")) {
            if (!GameManager.gameStarted && !GameManager.inCage) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getGameMode() == GameMode.SPECTATOR || player.isDead()) continue;
                    validPlayers.add(player);
                }

                if (Bukkit.getOnlinePlayers().size() <= 1) {
                    sender.sendMessage(ChatColor.RED + "You need at least 2 players to start the game!");
                    return false;
                }

                if (validPlayers.size() <= 1) {
                    sender.sendMessage(ChatColor.RED + "Some players have not respawned yet!");
                    validPlayers.clear();
                    return false;
                }
                GameManager.startGame();
            } else {
                sender.sendMessage(ChatColor.RED + "A game is already running! Use " + ChatColor.YELLOW + "/cancel" + ChatColor.RED + " to cancel it.");
            }
        } else {
            MessagesUtils.sendNoPermissionError((Player) sender);
        }

        return true;
    }
}
