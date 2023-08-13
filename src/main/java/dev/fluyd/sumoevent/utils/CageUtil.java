package dev.fluyd.sumoevent.utils;

import dev.fluyd.sumoevent.game.GameManager;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;

@UtilityClass
public class CageUtil {
    private ArrayList<Location> createCage(Location location) {
        ArrayList<Location> blocks = new ArrayList<>();
        Location loc1 = new Location(location.getWorld(), location.getX() + 2.0, location.getY() - 1.0, location.getZ() + 2.0);
        Location loc2 = new Location(location.getWorld(), location.getX() - 2.0, location.getY() + 3.0, location.getZ() - 2.0);
        ArrayList<Location> area1 = ConfigUtils.getBlocksInArea(loc1, loc2);
        Location loc3 = new Location(location.getWorld(), location.getX() + 1.0, location.getY() + 0.0, location.getZ() + 1.0);
        Location loc4 = new Location(location.getWorld(), location.getX() - 1.0, location.getY() + 2.0, location.getZ() - 1.0);
        ArrayList<Location> area2 = ConfigUtils.getBlocksInArea(loc3, loc4);
        for (Location areaLoc : area1) {
            if (blocks.contains(areaLoc)) continue;
            blocks.add(areaLoc);
        }
        for (Location areaLoc : area2) {
            if (!blocks.contains(areaLoc)) continue;
            blocks.remove(areaLoc);
        }
        return blocks;
    }

    private Material compatStainedGlass() {
        try {
            return Material.valueOf("STAINED_GLASS");
        } catch (IllegalArgumentException e) {
            return Material.valueOf("WHITE_STAINED_GLASS");
        }
    }

    public void setCage(boolean placed) {
        GameManager.inCage = placed;
        Location location = ConfigUtils.getParsedLocation(ConfigUtils.getGameStartLocation());

        for (Location loc : createCage(location)) {
            Block block = loc.getBlock();
            block.setType(placed ? compatStainedGlass() : Material.AIR);
            block.getState().update();
        }
    }
}
