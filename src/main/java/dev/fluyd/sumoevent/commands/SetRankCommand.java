package dev.fluyd.sumoevent.commands;

import dev.fluyd.sumoevent.ranks.Rank;
import dev.fluyd.sumoevent.ranks.RankManager;
import dev.fluyd.sumoevent.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SetRankCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("sumoevent.setrank")) {
                if (args.length == 1) {
                    if (RankManager.rankExists(args[0].toUpperCase())) {
                        Rank rank = RankManager.getRank(args[0].toUpperCase());
                        RankManager.setRank(Bukkit.getPlayer(sender.getName()).getUniqueId(), rank);
                        Bukkit.getPlayer(sender.getName()).setPlayerListName(ChatColor.translateAlternateColorCodes('&', RankManager.getRankString(Bukkit.getPlayer(sender.getName()).getUniqueId())) + sender.getName());
                        sender.sendMessage(ChatColor.GREEN + "You have set your rank to " + ChatColor.YELLOW + args[0] + ChatColor.GREEN + "!");
                    } else {
                        sender.sendMessage(RankManager.getRankList());
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <rank>");
                }
            } else {
                MessagesUtils.sendNoPermissionError((Player) sender);
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Sorry, but this command can only be executed by a player.");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            // If the user has typed the start of a command, filter out the matching ones
            return Arrays.stream(Rank.values())
                    .map(Rank::name)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();  // Return an empty list if no matches
    }
}
