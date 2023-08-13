package dev.fluyd.sumoevent.listeners;

import dev.fluyd.sumoevent.SumoEvent;
import dev.fluyd.sumoevent.events.PlayerEnterWaterEvent;
import dev.fluyd.sumoevent.utils.MessagesUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

public class WaterListener implements Listener, Runnable {
    // Make this event happend before all other of my events
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerMove(PlayerMoveEvent e) {
        if (e.getPlayer().getLocation().getBlock().isLiquid())
            SumoEvent.INSTANCE.getServer().getPluginManager().callEvent(new PlayerEnterWaterEvent(e.getPlayer()));
    }

    @EventHandler
    public void onPlayerTeleportEvent(PlayerTeleportEvent e) {
        Player p = e.getPlayer();

        if (e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            if (e.getTo().getBlock().isLiquid()) {
                e.setCancelled(true);
                MessagesUtils.sendOutOfBoundsError(p);
                // Give them the item back
                p.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 1));
            }
        }
    }

    @Override
    public void run() {
        for (Player p : SumoEvent.INSTANCE.getServer().getOnlinePlayers()) {
            if (p.getLocation().getBlock().isLiquid()) {
                SumoEvent.INSTANCE.getServer().getPluginManager().callEvent(new PlayerEnterWaterEvent(p));
            }
        }
    }
}
