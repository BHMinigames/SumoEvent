package dev.fluyd.sumoevent.commands;

import dev.fluyd.sumoevent.SumoEvent;
import dev.fluyd.sumoevent.customitem.CustomItem;
import dev.fluyd.sumoevent.utils.MessagesUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomItemCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        if (!sender.hasPermission("sumoevent.customitem")) {
            MessagesUtils.sendNoPermissionError((Player) sender);
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Specify the custom item name!");
            return false;
        }

        String itemNameEnum = args[0];

        Optional<CustomItem> customItemOpt = SumoEvent.INSTANCE.customItemManager.getItem(itemNameEnum);

        int amount = 1; // Default value

        if (args.length > 1) {
            try {
                amount = Integer.parseInt(args[1]);
                if (amount <= 0) {
                    player.sendMessage(ChatColor.RED + "Please provide a valid positive amount!");
                    return true;
                }
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Invalid amount. Please provide a valid number!");
                return true;
            }
        }

        if (customItemOpt.isPresent()) {
            ItemStack customItemStack = customItemOpt.get().getItem().clone(); // Clone the original ItemStack
            customItemStack.setAmount(amount); // Set the desired amount
            player.getInventory().addItem(customItemStack);
            player.sendMessage(ChatColor.GREEN + "Gave you " + ChatColor.YELLOW + amount + ChatColor.GREEN + " of " + ChatColor.YELLOW + itemNameEnum + ChatColor.GREEN + "!");
        } else {
            player.sendMessage(ChatColor.RED + "No such item!");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return SumoEvent.INSTANCE.customItemManager.getAllItemNames()
                    .stream()
                    .filter(nameEnum -> nameEnum.startsWith(args[0].toUpperCase()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}