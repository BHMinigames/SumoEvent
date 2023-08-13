package dev.fluyd.sumoevent.customitem.items;

import dev.fluyd.sumoevent.customitem.AbstractCustomItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class BridgeEgg extends AbstractCustomItem implements Listener {
    private final JavaPlugin plugin;

    public BridgeEgg(JavaPlugin plugin) {
        super(Material.EGG, "&bBridge Egg", "&7&oThrow to create a magical bridge.");
        this.plugin = plugin;
    }

    @Override
    public void onUse(Player player) {
        player.launchProjectile(Egg.class); // Launch the egg
    }

    @Override
    public void onPlace(Player player, Block block) {

    }

    @EventHandler
    public void onEggLand(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Egg) {
            bridgeEgg((Egg) event.getEntity());
        }
    }

    private void bridgeEgg(Egg egg) {
        int i = 1;
        while (i < 300) {
            final int count = i; // For use within lambda
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                Location location = egg.getLocation().clone().subtract(0, count * 0.1, 0); // Adjust based on desired path of the bridge
                setRandomWool(location);
            }, (long) (i * 0.1));
            i++;
        }
    }

    private void setRandomWool(Location location) {
        location.getBlock().setType(Material.getMaterial("WOOL"));
        byte dataValue = (byte) new Random().nextInt(16); // Random wool color
//        location.getBlock().setData(dataValue);
//        location.getWorld().playSound(location, Sound.CHICKEN_EGG_POP, 10.0f, 0.8f);
    }
}