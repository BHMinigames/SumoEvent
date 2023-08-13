package dev.fluyd.sumoevent.customitem.items;

import dev.fluyd.sumoevent.SumoEvent;
import dev.fluyd.sumoevent.commands.GravityMapBreakCommand;
import dev.fluyd.sumoevent.commands.ToggleCooldownsCommand;
import dev.fluyd.sumoevent.customitem.AbstractCustomItem;
import dev.fluyd.sumoevent.game.GameManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GravityWand extends AbstractCustomItem {
    ArrayList<Player> coolDown = new ArrayList<>();
    private List<FallingBlock> fallingBlocks = new ArrayList<>();

    public GravityWand() {
        super(Material.BLAZE_ROD, "&6Gravity Wand", "&7&oShoots a trace. When it", "&7&ohits a block, it converts", "&7&oblocks in a 5-block sphere into", "&7&ofalling sand blocks and blows", "&7&othem apart.", "&7&oCooldown: &c&l5s");
    }

    @Override
    public void onUse(Player player) {
        if (coolDown.contains(player)) {
            player.sendMessage(ChatColor.RED + "You can't use this item yet!");
            return;
        }
        if (ToggleCooldownsCommand.cooldownsEnabled) {
            coolDown.add(player);
            Bukkit.getScheduler().runTaskLater(SumoEvent.INSTANCE, () -> coolDown.remove(player), 20 * 10);
        }
        traceAndHit(player);
    }

    @Override
    public void onPlace(Player player, Block block) {
    }

    private void traceAndHit(Player player) {
        Location start = player.getEyeLocation();
        Vector direction = start.getDirection();

        new BukkitRunnable() {
            double distance = 0;

            public void run() {
                if (distance > 100) {
                    this.cancel();
                    return;
                }

                Location checkLocation = start.clone().add(direction.clone().multiply(distance));
                player.getWorld().playSound(checkLocation, getCompatibleSound(CustomSound.EGG_POP), 1.0f, 1.0f);

                if (distance > 1) {
                    for (int i = 0; i < 10; i++) {
                        new ParticleBuilder(ParticleEffect.REDSTONE, checkLocation)
                                .setAmount(20)
                                .setSpeed(0)
                                .display();
                    }
                }

                if (checkLocation.getBlock().getType().isSolid()) {
                    player.getWorld().playSound(checkLocation, getCompatibleSound(CustomSound.SMOKE_DISIPATE), 1.0f, 1.0f);
                    new ParticleBuilder(ParticleEffect.FLAME, checkLocation)
                            .setAmount(100)
                            .setSpeed(.5F)
                            .display();

                    activateGravityEffect(checkLocation);

                    this.cancel();
                    return;
                }

                for (Entity entity : checkLocation.getWorld().getNearbyEntities(checkLocation, 0.5, 0.5, 0.5)) {
                    if (entity != player && entity instanceof LivingEntity) {
                        ((LivingEntity) entity).damage(5, player);
                        this.cancel();
                        return;
                    }
                }

                distance += 1;
            }
        }.runTaskTimer(SumoEvent.INSTANCE, 0L, 1L);
    }

    private void activateGravityEffect(Location loc) {
        int radius = 7;

        // Draw a particle ring as an outline
        for (double theta = 0; theta <= Math.PI; theta += Math.PI/40) { // Polar angle
            for (double phi = 0; phi <= 2 * Math.PI; phi += Math.PI/20) { // Azimuthal angle
                double dx = radius * Math.sin(theta) * Math.cos(phi);
                double dy = radius * Math.sin(theta) * Math.sin(phi);
                double dz = radius * Math.cos(theta);

                Location particleLoc = loc.clone().add(dx, dy, dz);
                new ParticleBuilder(ParticleEffect.REDSTONE, particleLoc)
                        .setAmount(1)
                        .setSpeed(0)
                        .display();
            }
        }

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block block = loc.clone().add(x, y, z).getBlock();
                    if (!GameManager.placedBlocks.contains(block.getLocation()) && !GravityMapBreakCommand.ALLOW_MAP_BREAKING) continue;
                    if (block.getType() != Material.CHEST) {
                        FallingBlock fallingBlock = loc.getWorld().spawnFallingBlock(block.getLocation(), block.getType(), block.getData());
                        block.setType(Material.AIR);
                        fallingBlock.setVelocity(new Vector((Math.random() - 0.5) * .5, Math.random() * .5, (Math.random() - 0.5) * .5));
                        fallingBlock.setDropItem(false);
                        fallingBlocks.add(fallingBlock);
                    }
                }
            }
        }

        // Push players in the radius
        for (Entity entity : loc.getWorld().getNearbyEntities(loc, radius, radius, radius)) {
            if (entity instanceof Player) {
                Vector direction = entity.getLocation().subtract(loc).toVector().normalize().multiply(1.5); // Change 1.5 to control the force of the push
                entity.setVelocity(direction);
            }
        }

        // Periodically check if the blocks have landed
        new BukkitRunnable() {
            @Override
            public void run() {
                int i = 0;

                Iterator<FallingBlock> iterator = fallingBlocks.iterator();
                while (iterator.hasNext()) {
                    FallingBlock fb = iterator.next();
                    if (fb.isDead() || fb.isOnGround()) {
                        fb.remove();
                        iterator.remove();

                        fb.getLocation().getBlock().setType(Material.AIR);

                        ++i;
                    }
                }

                if (i == 0)
                    this.cancel();
            }
        }.runTaskTimer(SumoEvent.INSTANCE, 20L, 20L);
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