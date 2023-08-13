package dev.fluyd.sumoevent.popuptower;

import dev.fluyd.sumoevent.SumoEvent;
import dev.fluyd.sumoevent.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class TowerBuilder {
    /**
     * This builds the north tower
     * The tower is later rotated to the desired direction
     * @param center
     * @param direction
     */
    public void build(final Location center, final UUID uuid, final Direction direction) {
        final List<Location> towerBlocks = new ArrayList<>();
        final List<Location> towerLadders = new ArrayList<>();

        for (int i = -1; i < 2; ++i) {
            towerBlocks.add(this.modifyLocation(center, -2D, i, -1D));
            towerBlocks.add(this.modifyLocation(center, -1D, i, -2D));
            towerBlocks.add(this.modifyLocation(center, -2D, i, 0D));
            towerBlocks.add(this.modifyLocation(center, 0D, i, -2D));
            towerBlocks.add(this.modifyLocation(center, -1D, i, 1D));

            towerLadders.add(this.modifyLocation(center, 0D, i, -1D));

            towerBlocks.add(this.modifyLocation(center, 1D, i, -2D));
            towerBlocks.add(this.modifyLocation(center, 2D, i, -1D));
            towerBlocks.add(this.modifyLocation(center, 1D, i, 1D));
            towerBlocks.add(this.modifyLocation(center, 2D, i, 0D));
        }

        for (int i = -1; i < 1; ++i) {
            final double yIncrement = 3D + i;

            towerBlocks.add(this.modifyLocation(center, -2D, yIncrement, -1D));
            towerBlocks.add(this.modifyLocation(center, -2D, yIncrement, 0D));
            towerBlocks.add(this.modifyLocation(center, -1D, yIncrement, -2D));
            towerBlocks.add(this.modifyLocation(center, -1D, yIncrement, 1D));
            towerBlocks.add(this.modifyLocation(center, 0D, yIncrement, -2D));

            towerLadders.add(this.modifyLocation(center, 0D, yIncrement, -1D));

            towerBlocks.add(this.modifyLocation(center, 0D, yIncrement, 1D));
            towerBlocks.add(this.modifyLocation(center, 1D, yIncrement, 1D));
            towerBlocks.add(this.modifyLocation(center, 1D, yIncrement, -2D));
            towerBlocks.add(this.modifyLocation(center, 2D, yIncrement, 0D));
            towerBlocks.add(this.modifyLocation(center, 2D, yIncrement, -1D));
        }

        final double layer4 = 4D;
        towerBlocks.add(this.modifyLocation(center, -3D, layer4, -2D));
        towerBlocks.add(this.modifyLocation(center, -2D, layer4, -3D));
        towerBlocks.add(this.modifyLocation(center, -3D, layer4, 1D));
        towerBlocks.add(this.modifyLocation(center, -2D, layer4, -2D));
        towerBlocks.add(this.modifyLocation(center, -2D, layer4, -1D));
        towerBlocks.add(this.modifyLocation(center, -2D, layer4, 0D));
        towerBlocks.add(this.modifyLocation(center, -2D, layer4, 1D));
        towerBlocks.add(this.modifyLocation(center, -1D, layer4, -2D));
        towerBlocks.add(this.modifyLocation(center, -2D, layer4, 2D));
        towerBlocks.add(this.modifyLocation(center, -1D, layer4, -1D));
        towerBlocks.add(this.modifyLocation(center, -1D, layer4, 0D));
        towerBlocks.add(this.modifyLocation(center, -1D, layer4, 1D));
        towerBlocks.add(this.modifyLocation(center, 0D, layer4, -3D));
        towerBlocks.add(this.modifyLocation(center, 0D, layer4, -2D));

        towerLadders.add(this.modifyLocation(center, 0D, layer4, -1D));

        towerBlocks.add(this.modifyLocation(center, 0D, layer4, 1D));
        towerBlocks.add(this.modifyLocation(center, 0D, layer4, 0D));
        towerBlocks.add(this.modifyLocation(center, 1D, layer4, -2D));
        towerBlocks.add(this.modifyLocation(center, 0D, layer4, 2D));
        towerBlocks.add(this.modifyLocation(center, 1D, layer4, -1D));
        towerBlocks.add(this.modifyLocation(center, 1D, layer4, 0D));
        towerBlocks.add(this.modifyLocation(center, 1D, layer4, 1D));
        towerBlocks.add(this.modifyLocation(center, 2D, layer4, -3D));
        towerBlocks.add(this.modifyLocation(center, 2D, layer4, -2D));
        towerBlocks.add(this.modifyLocation(center, 2D, layer4, -1D));
        towerBlocks.add(this.modifyLocation(center, 2D, layer4, 0D));
        towerBlocks.add(this.modifyLocation(center, 2D, layer4, 1D));
        towerBlocks.add(this.modifyLocation(center, 2D, layer4, 2D));
        towerBlocks.add(this.modifyLocation(center, 3D, layer4, -2D));
        towerBlocks.add(this.modifyLocation(center, 3D, layer4, 1D));

        final double layer5 = 5D;
        towerBlocks.add(this.modifyLocation(center, -3D, layer5, -2D));
        towerBlocks.add(this.modifyLocation(center, -3D, layer5, -1D));
        towerBlocks.add(this.modifyLocation(center, -3D, layer5, 0D));
        towerBlocks.add(this.modifyLocation(center, -3D, layer5, 1D));
        towerBlocks.add(this.modifyLocation(center, -2D, layer5, -3D));
        towerBlocks.add(this.modifyLocation(center, -1D, layer5, -3D));
        towerBlocks.add(this.modifyLocation(center, -2D, layer5, 2D));
        towerBlocks.add(this.modifyLocation(center, -1D, layer5, 2D));
        towerBlocks.add(this.modifyLocation(center, 0D, layer5, -3D));
        towerBlocks.add(this.modifyLocation(center, 0D, layer5, 2D));
        towerBlocks.add(this.modifyLocation(center, 1D, layer5, -3D));
        towerBlocks.add(this.modifyLocation(center, 1D, layer5, 2D));
        towerBlocks.add(this.modifyLocation(center, 2D, layer5, -3D));
        towerBlocks.add(this.modifyLocation(center, 3D, layer5, -2D));
        towerBlocks.add(this.modifyLocation(center, 2D, layer5, 2D));
        towerBlocks.add(this.modifyLocation(center, 3D, layer5, -1D));
        towerBlocks.add(this.modifyLocation(center, 3D, layer5, 0D));
        towerBlocks.add(this.modifyLocation(center, 3D, layer5, 1D));

        final double layer6 = 6D;
        towerBlocks.add(this.modifyLocation(center, -3D, layer6, -2D));
        towerBlocks.add(this.modifyLocation(center, -2D, layer6, -3D));
        towerBlocks.add(this.modifyLocation(center, -3D, layer6, 1D));
        towerBlocks.add(this.modifyLocation(center, -0D, layer6, -3D));
        towerBlocks.add(this.modifyLocation(center, -2D, layer6, 2D));
        towerBlocks.add(this.modifyLocation(center, 2D, layer6, -3D));
        towerBlocks.add(this.modifyLocation(center, 0D, layer6, 2D));
        towerBlocks.add(this.modifyLocation(center, 3D, layer6, -2D));
        towerBlocks.add(this.modifyLocation(center, 2D, layer6, 2D));
        towerBlocks.add(this.modifyLocation(center, 3D, layer6, 1D));

        this.placeTower(center, towerBlocks, towerLadders, uuid, direction);
    }

    private void placeTower(final Location center, final List<Location> towerBlocks, final List<Location> towerLadders, final UUID uuid, final Direction direction) {
        final BukkitScheduler scheduler = Bukkit.getScheduler();
        final long initialDelay = 1L;

        scheduler.runTaskLater(SumoEvent.INSTANCE, () -> {
            double count = 0.0D;
            long delay = 0L;

            for (final Location loc : towerBlocks) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(SumoEvent.INSTANCE, () -> placeWool(center, direction.shiftLocation(center, loc), uuid), delay);

                ++count;
                if (count % 2.0D == 0.0D)
                    ++delay;
            }

            delay = 0L;

            for (final Location loc : towerLadders) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(SumoEvent.INSTANCE, () -> placeBlock(direction.shiftLocation(center, loc), Material.LADDER, direction), delay);
                if (delay == 20L)
                    delay += 11L;
                else
                    delay += 5L;
            }
        }, initialDelay);
    }

    private Location modifyLocation(final Location location, final double x, final double y, final double z) {
        final Location newLocation = location.clone();
        return newLocation.add(x, y, z);
    }

    private void placeWool(final Location center, final Location loc, final UUID uuid) {
        final Block block = loc.getBlock();
        if (!this.canPlace(block))
            return;

        this.pushUp(loc);

        final int blockY = block.getY();
        final int centerY = (int) center.getY();
        final int diff = Math.abs(blockY - centerY);

        final ItemStack item = ColorUtils.convertToWool(uuid, diff);
        block.setType(item.getType());
        if (!SumoEvent.isNewApi())
            this.setData(block, item.getData().getData());

        GameManager.placedBlocks.add(block.getLocation());

        this.pop(loc);
    }

    private void pushUp(final Location loc) {
        for (final Entity e : loc.getWorld().getEntities()) {
            final Location entityLoc = e.getLocation();

            if (!(e instanceof Player) && !(e instanceof Monster) && !(e instanceof Creature))
                continue;

            if (loc.distance(entityLoc) > 1.3D)
                continue;

            final Vector vector = e.getVelocity();
            e.setVelocity(new Vector(vector.getX(), 0.5D, vector.getZ()));
        }
    }

    private void placeBlock(final Location loc, final Material m, final Direction d) {
        final Block block = loc.getBlock();

        if (!this.canPlace(block))
            return;

        block.setType(m);

        if (m == Material.LADDER)
            this.handleRotation(block, d);

        GameManager.placedBlocks.add(block.getLocation());

        this.pop(loc);
    }

    private void pop(final Location loc) {
        loc.getWorld().playSound(loc, this.getPopSound(), 10.0f, 0.8f);
    }

    private boolean canPlace(final Block block) {
        if (GameManager.gameStarting)
            return false;
        final Material type = block.getType();
        return type == Material.AIR; // Removed isPassable check as it breaks compatibility
    }

    private Sound getPopSound() {
        if (SumoEvent.isNewApi())
            return Sound.ENTITY_CHICKEN_EGG;

        return Sound.valueOf("CHICKEN_EGG_POP");
    }

    private void handleRotation(final Block block, final Direction direction) {
        if (SumoEvent.isNewApi()) {
            NewAPI.handleRotation(block, direction);
            return;
        }

        this.setData(block, direction.getData());
    }

    /**
     * Method to set data for old versions
     * @param block
     * @param data
     */
    private void setData(final Block block, final byte data) {
        try {
            final Class<?> blockClass = block.getClass();
            final Method method = blockClass.getDeclaredMethod("setData", byte.class);

            method.invoke(block, data);
        } catch (final Exception ignored) {}
    }
}