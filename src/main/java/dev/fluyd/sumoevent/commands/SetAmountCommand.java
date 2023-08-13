package dev.fluyd.sumoevent.commands;

import dev.fluyd.sumoevent.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SetAmountCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("sumoevent.setamount")) {
            if (args.length == 1) {
                try {
                    Player player = Bukkit.getPlayer(sender.getName());
                    ItemStack item = player.getItemInHand();
                    short amount = Short.parseShort(args[0]);
                    if (item != null && item.getType() != Material.AIR) {
                        item.setAmount(amount);
                        player.setItemInHand(item);
                        sender.sendMessage(ChatColor.GREEN + "Amount set to " + ChatColor.YELLOW + amount + ChatColor.GREEN + ".");
                    }
                    sender.sendMessage(ChatColor.RED + "You have to be holding an item.");
                } catch (Exception exception) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSomething went wrong when setting the amount of the item in your hand: &7" + exception));
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <value>");
            }
        } else {
            MessagesUtils.sendNoPermissionError((Player) sender);
        }
        return false;
    }
}
