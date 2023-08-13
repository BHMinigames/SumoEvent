package dev.fluyd.sumoevent.listeners;

import dev.fluyd.sumoevent.SumoEvent;
import dev.fluyd.sumoevent.game.GameManager;
import dev.fluyd.sumoevent.utils.MessagesUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class WorldListener implements Listener {
    private final Map<Location, Long> recentCancelledPlacements = new HashMap<>();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPlaceBlock(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();

        if (!GameManager.canBuild && !GameManager.players.contains(player.getUniqueId())) {
            cancelAndRemember(block.getLocation());
            e.setCancelled(true);
            MessagesUtils.sendMapEditError(player);
            return;
        }

        Material replacedType = e.getBlockReplacedState().getType();
        if (replacedType == Material.WATER || replacedType == Material.valueOf("STATIONARY_WATER")) {
            cancelAndRemember(block.getLocation());
            e.setCancelled(true);
            MessagesUtils.sendMapEditError(player);
            return;
        }

        // Check if player is placing block on top of a recent cancelled block location
        if (recentCancelledPlacements.containsKey(block.getRelative(BlockFace.DOWN).getLocation()) &&
                System.currentTimeMillis() - recentCancelledPlacements.get(block.getRelative(BlockFace.DOWN).getLocation()) < 1000) { // 1000ms = 1 second
            e.setCancelled(true);
            MessagesUtils.sendMapEditError(player);
            return;
        }

        GameManager.placedBlocks.add(block.getLocation());
    }

    private void cancelAndRemember(Location location) {
        recentCancelledPlacements.put(location, System.currentTimeMillis());
    }

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent e) {
        if (!GameManager.canBuild && !GameManager.placedBlocks.contains(e.getBlock().getLocation()) ) {
            Block block = e.getBlock();

            e.setCancelled(true);

            if (block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST) {
                Chest chest = (Chest) block.getState();

                // Drop the contents of the chest
                for (ItemStack item : chest.getInventory().getContents()) {
                    if (item != null) {
                        Item droppedItem = block.getWorld().dropItemNaturally(block.getLocation().add(0.5, 1, 0.5), item);

                        // Apply velocity to the item in a way to make it look like it exploded
                        double x = Math.random() * 0.5 - 0.25; // Random value between -0.25 and 0.25
                        double y = Math.random() * 0.5;        // Random value between 0 and 0.5 to give it an upward boost
                        double z = Math.random() * 0.5 - 0.25; // Random value between -0.25 and 0.25

                        droppedItem.setVelocity(new Vector(x, y, z));
                    }
                }

                // Clear the chest's inventory
                chest.getInventory().clear();

                // Manually set the block type to AIR to "break" it without dropping the block itself
                block.setType(Material.AIR);
                return;
            }

            // Send a manual block change to the player to prevent ghost missing blocks.
            SumoEvent.INSTANCE.getServer().getScheduler().runTaskLaterAsynchronously(SumoEvent.INSTANCE, () -> {
                e.getPlayer().sendBlockChange(e.getBlock().getLocation(), e.getBlock().getType(), e.getBlock().getData());
            }, 20L);

            MessagesUtils.sendMapEditError(e.getPlayer());
        } else if (GameManager.players.contains(e.getPlayer().getUniqueId()) && GameManager.placedBlocks.contains(e.getBlock().getLocation())) {
            GameManager.placedBlocks.remove(e.getBlock().getLocation());
        }
    }
}
