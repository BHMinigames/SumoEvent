package dev.fluyd.sumoevent.listeners;

import dev.fluyd.sumoevent.SumoEvent;
import dev.fluyd.sumoevent.events.PlayerEnterWaterEvent;
import dev.fluyd.sumoevent.events.PlayerKillEvent;
import dev.fluyd.sumoevent.game.DeathCause;
import dev.fluyd.sumoevent.game.GameManager;
import dev.fluyd.sumoevent.utils.LootUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class PlayerListener implements Listener {
    public static HashMap<UUID, Integer> kills = new HashMap<>();

    @EventHandler
    public void playerEnterWaterEvent(PlayerEnterWaterEvent e) {
        if (GameManager.gameStarted && GameManager.alivePlayers.contains(e.getPlayer().getUniqueId()))
            GameManager.handleDeath(e.getPlayer(), null, DeathCause.FALL);
    }

    @EventHandler
    public void playerHungerChangeEvent(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void playerKillPlayer(PlayerKillEvent e) {
        Player player = e.getVictim();
        Location deathLocation = e.getKiller().getLocation();  // Get player's death location

        // get their inventory, and drop their items at their death location
        dropAndClearInventory(player, deathLocation);

        // Prevent death screen from showing
        player.spigot().respawn();
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(e.getKiller().getLocation());

        GameManager.handleDeath(e.getVictim(), e.getKiller(), DeathCause.PLAYER);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        e.setDeathMessage(null);

        e.getDrops().clear();

        e.setDroppedExp(0);
    }

    public void dropAndClearInventory(Player player, Location dropLocation) {
        PlayerInventory inventory = player.getInventory(); // Player's inventory

        // Loop through items and drop them
        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getType() != Material.AIR) { // Make sure the item slot isn't empty
                player.getWorld().dropItemNaturally(dropLocation, item);
            }
        }

        // Clear the inventory
        inventory.clear();
    }


    @EventHandler
    public void onDamageEvent(EntityDamageEvent e) {
        // Disable damage for non in-game players, whilst the game is currently running.
        if (GameManager.gameStarted && !GameManager.players.contains(e.getEntity().getUniqueId()))
            e.setCancelled(true);

        // If invincibility is enabled, and the cause of the damage was another player.
        if (GameManager.invincibility && e.getEntityType() == EntityType.PLAYER)
            e.setCancelled(true);

        // If NoFall is enabled, and the cause of the damage was fall damage.
        if (GameManager.noFall && e.getCause() == EntityDamageEvent.DamageCause.FALL)
            e.setCancelled(true);
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent e) {
        if (GameManager.gameStarting && GameManager.players.contains(e.getPlayer().getUniqueId()))
            e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerOpenChestEvent(PlayerInteractEvent e) {

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.CHEST && LootUtils.chestLocations.contains(e.getClickedBlock()) && e.getPlayer().getGameMode() != GameMode.SURVIVAL) {
            e.getPlayer().sendMessage(ChatColor.RED + "You are not allowed to do that!");
            e.setCancelled(true);
            return;
        }

        // If the player right-clicked a check that was placed by the LootUtil
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.CHEST && LootUtils.chestLocations.contains(e.getClickedBlock())) {
            if (LootUtils.openedChests.get(e.getClickedBlock()) != -1) {
                return;
            }
            LootUtils.openedChests.put(e.getClickedBlock(), 300);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Block blockBelow = e.getPlayer().getLocation().subtract(0, 1, 0).getBlock();

        // Launchpad functionality
        if (blockBelow.getType() == Material.SPONGE && !GameManager.launchpadCooldown.contains(e.getPlayer().getUniqueId()) && GameManager.alivePlayers.contains(e.getPlayer().getUniqueId())) {
            // Add velocity to the way the player is facing, if they are upward, focus the velocity upwards more than anywhere else, also if they are sprinting, apply more velocity
            e.getPlayer().setVelocity(e.getPlayer().getLocation().getDirection().multiply(1.5).setY(e.getPlayer().isSprinting() ? 3.6 : 3.3));

            e.getPlayer().playSound(e.getPlayer().getLocation(), getGhastFireballSound(), 1, 1);
            GameManager.launchpadCooldown.add(e.getPlayer().getUniqueId());
            SumoEvent.INSTANCE.getServer().getScheduler().scheduleSyncDelayedTask(SumoEvent.INSTANCE, () -> GameManager.launchpadCooldown.remove(e.getPlayer().getUniqueId()), 40L);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {}

    private Sound getGhastFireballSound() {
        try {
            return Sound.valueOf("GHAST_FIREBALL"); // 1.8
        } catch (IllegalArgumentException e) {
            return Sound.valueOf("ENTITY_GHAST_SHOOT"); // After 1.8
        }
    }

}
