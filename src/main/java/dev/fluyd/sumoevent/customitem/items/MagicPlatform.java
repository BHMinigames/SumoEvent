package dev.fluyd.sumoevent.customitem.items;

import dev.fluyd.sumoevent.SumoEvent;
import dev.fluyd.sumoevent.customitem.AbstractCustomItem;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class MagicPlatform extends AbstractCustomItem {
    public MagicPlatform() {
        super(Material.BEDROCK, "&cM&6a&eg&ai&3c &5P&cl&6a&et&af&3o&9r&5m", "&7&oPlace to create a magical platform.");
    }

    @Override
    public void onUse(Player player) {
        removePopupTowerItem(player, player.getItemInHand());
        Location center = player.getLocation().clone().subtract(0, 2, 0);
        int radius = 3; // Or whatever radius you want
        int height = 1; // Since you want a flat cylinder

        Map<Location, Material> originalBlocks = new HashMap<>(); // Store the original block types

        // Fill with bedrock and store the original block types
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                // Check if inside circle using pythagoras
                if (x * x + z * z <= radius * radius) {
                    for (int y = 0; y < height; y++) {
                        Location loc = center.clone().add(x, y, z);
                        if (loc.getBlock().getType() != Material.AIR) continue;
                        originalBlocks.put(loc, loc.getBlock().getType());
                        loc.getBlock().setType(Material.BEDROCK);
                    }
                }
            }
        }

        player.teleport(player.getLocation().clone().add(0, 1, 0));

        // Schedule a task to revert the blocks back after 3 seconds (60 ticks)
        Bukkit.getScheduler().scheduleSyncDelayedTask(SumoEvent.INSTANCE, () -> {
            for (Location loc : originalBlocks.keySet()) {
                loc.getBlock().setType(originalBlocks.get(loc));
            }
        }, 60L);
    }

    @Override
    public void onPlace(Player player, Block block) {

    }

    private void removePopupTowerItem(final Player p, final ItemStack item) {
        if (p.getGameMode() != GameMode.SURVIVAL)
            return;

        if (item.getAmount() == 1)
            p.setItemInHand(null);
        else item.setAmount(item.getAmount() - 1);

        p.updateInventory();
    }
}
