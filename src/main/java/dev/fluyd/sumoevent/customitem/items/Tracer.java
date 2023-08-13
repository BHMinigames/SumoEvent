package dev.fluyd.sumoevent.customitem.items;

import dev.fluyd.sumoevent.SumoEvent;
import dev.fluyd.sumoevent.commands.ToggleCooldownsCommand;
import dev.fluyd.sumoevent.customitem.AbstractCustomItem;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.ArrayList;

public class Tracer extends AbstractCustomItem {
    ArrayList<Player> coolDown = new ArrayList<>();

    public Tracer() {
        super(Material.DIAMOND_HOE, "&bTracer", "&7&oShoots a tracer that", "&7&ohits the first entity", "&7&oit comes in contact with", "&7&ofor &c&l5 &7&odamage", "&7&oCooldown: &c&l5s");
    }

    @Override
    public void onUse(Player player) {
        if (coolDown.contains(player)) {
            player.sendMessage(ChatColor.RED + "You can't use this item yet!");
            return;
        }
        if (ToggleCooldownsCommand.cooldownsEnabled) {
            coolDown.add(player);
            Bukkit.getScheduler().runTaskLater(SumoEvent.INSTANCE, () -> coolDown.remove(player), 25L);
        }
        traceAndHit(player);
    }

    @Override
    public void onPlace(Player player, Block block) {

    }

    private void traceAndHit(Player player) {
        // Starting location
        Location start = player.getEyeLocation();
        // Direction in which player is looking
        Vector direction = start.getDirection();

        // Use a BukkitRunnable for the delay
        new BukkitRunnable() {
            double distance = 0;

            public void run() {
                if (distance > 100) {
                    this.cancel();
                    return;
                }

                Location checkLocation = start.clone().add(direction.clone().multiply(distance));

                // Play the EGG_POP sound for every block
                player.getWorld().playSound(checkLocation, getCompatibleSound(CustomSound.EGG_POP), 1.0f, 1.0f);

                if (distance > 1) {
                    for (int i = 0; i < 10; i++) {  // Spawning the particle multiple times for visibility
                        new ParticleBuilder(ParticleEffect.REDSTONE, checkLocation)
                                .setAmount(20)
                                .setSpeed(0)
                                .display();
                    }
                }

                // Check for block collision
                if (checkLocation.getBlock().getType().isSolid()) {
                    player.getWorld().playSound(checkLocation, getCompatibleSound(CustomSound.SMOKE_DISIPATE), 1.0f, 1.0f);
                    new ParticleBuilder(ParticleEffect.FLAME, checkLocation)
                            .setAmount(100)
                            .setSpeed(.5F)
                            .display();
                    this.cancel();
                    return;
                }

                // Check for entity collision
                for (Entity entity : checkLocation.getWorld().getNearbyEntities(checkLocation, 0.5, 0.5, 0.5)) {
                    if (entity != player && entity instanceof Player) {  // Assuming only players are targets
                        Player target = (Player) entity;

                        double headHeight = target.getLocation().getY() + 1.62;
                        boolean headshot = Math.abs(checkLocation.getY() - headHeight) < 0.3;  // You can adjust this threshold

                        if (headshot) {
                            target.damage(20, player);
                            player.sendMessage(ChatColor.GREEN + "Headshot on " + ChatColor.YELLOW + target.getName() + ChatColor.GREEN + "! " + ChatColor.RED + ((int) Math.floor(target.getHealth() / 2)) + "❤");
                        } else {
                            target.damage(5, player);
                        }

                        player.playSound(player.getLocation(), Sound.valueOf("NOTE_PLING"), 1.0f, 2.0f);
                        this.cancel();
                        return;
                    }
                }

                distance += 1;
            }
        }.runTaskTimer(SumoEvent.INSTANCE, 0L, 0L);
    }

    public enum CustomSound {
        EGG_POP,
        SMOKE_DISIPATE;
    }

    public Sound getCompatibleSound(CustomSound customSound) {
        String version = Bukkit.getServer().getBukkitVersion().split("-")[0];
        boolean neverVersion = version.startsWith("1.9") || version.startsWith("1.10") || version.startsWith("1.11") || version.startsWith("1.12");

        if (customSound == CustomSound.EGG_POP) {
            if (version.startsWith("1.8")) {
                return Sound.valueOf("CHICKEN_EGG_POP");
            } else if (neverVersion) {
                // Adjust this to the appropriate sound for these versions.
                // This is just a placeholder and may need adjustment:
                return Sound.valueOf("ENTITY_CHICKEN_EGG");
            }
            // Continue for other versions as necessary...
            else {
                // Default for newer versions (e.g., 1.13 onwards). Adjust as necessary:
                return Sound.valueOf("ENTITY_CHICKEN_EGG");
            }
        } else if (customSound == CustomSound.SMOKE_DISIPATE) {
            if (version.startsWith("1.8")) {
                return Sound.valueOf("FIZZ");
            } else if (neverVersion) {
                // Adjust this to the appropriate sound for these versions.
                // This is just a placeholder and may need adjustment:
                return Sound.valueOf("BLOCK_FIRE_EXTINGUISH");
            }
            // Continue for other versions as necessary...
            else {
                // Default for newer versions (e.g., 1.13 onwards). Adjust as necessary:
                return Sound.valueOf("BLOCK_FIRE_EXTINGUISH");
            }
        }
        // Return null or throw an exception if the CustomSound doesn't match any criteria:
        return null;
    }
}
