package dev.fluyd.sumoevent.listeners;

import dev.fluyd.sumoevent.SumoEvent;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class CustomItemListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && event.getAction().toString().contains("RIGHT")) {

            // Chest check
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.CHEST) return;

            SumoEvent.INSTANCE.customItemManager.getItem(item.getItemMeta().getDisplayName())
                    .ifPresent(customItem -> customItem.onUse(event.getPlayer()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = event.getItemInHand();

        if (itemInHand != null && itemInHand.hasItemMeta() && itemInHand.getItemMeta().hasDisplayName()) {
            SumoEvent.INSTANCE.customItemManager.getItem(itemInHand.getItemMeta().getDisplayName())
                    .ifPresent(customItem -> {
                        customItem.onPlace(player, event.getBlockPlaced());
                        if (customItem.getRawName().equals("Popup Tower")) removePopupTowerItem(player, itemInHand);
                        event.setCancelled(true);
                    });
        }
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
