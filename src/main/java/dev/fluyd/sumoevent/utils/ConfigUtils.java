package dev.fluyd.sumoevent.utils;

import dev.fluyd.sumoevent.SumoEvent;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;

@UtilityClass
public class ConfigUtils {
    public String getGameStartLocation() {
        return SumoEvent.INSTANCE.getConfig().getString("gameStartLocation");
    }
    public Location getParsedLocation(String location) {
        try {
            String[] splitLocation = location.split(",");
            World world = Bukkit.getWorld(splitLocation[0]);
            double x = Double.parseDouble(splitLocation[1]);
            double y = Double.parseDouble(splitLocation[2]);
            double z = Double.parseDouble(splitLocation[3]);
            float yaw = Float.parseFloat(splitLocation[4]);
            float pitch = Float.parseFloat(splitLocation[5]);
            return new Location(world, x, y, z, yaw, pitch);
        }
        catch (Exception exception) {
            System.out.println("Something went wrong when parsing the locations from the config: ");
            exception.printStackTrace();
            return null;
        }
    }

    public String getLobbyLocation() {
        return SumoEvent.INSTANCE.getConfig().getString("lobbyLocation");
    }

    public FileConfiguration getConfig() {
        return SumoEvent.INSTANCE.getConfig();
    }

    public String getUnparsedLocation(Location location) {
        World world = location.getWorld();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        float yaw = location.getYaw();
        float pitch = location.getPitch();
        return world.getName() + "," + x + "," + y + "," + z + "," + yaw + "," + pitch;
    }

    public Boolean getMode() {
        try {
            return SumoEvent.INSTANCE.getConfig().getBoolean("mode");
        }
        catch (Exception exception) {
            System.out.println("Something went wrong when parsing a boolean!");
            exception.printStackTrace();
            return null;
        }
    }

    public ArrayList<Location> getBlocksInArea(Location start, Location end) {
        int topBlockX = Math.max(start.getBlockX(), end.getBlockX());
        int bottomBlockX = Math.min(start.getBlockX(), end.getBlockX());
        int topBlockY = Math.max(start.getBlockY(), end.getBlockY());
        int bottomBlockY = Math.min(start.getBlockY(), end.getBlockY());
        int topBlockZ = Math.max(start.getBlockZ(), end.getBlockZ());
        int bottomBlockZ = Math.min(start.getBlockZ(), end.getBlockZ());
        ArrayList<Location> locs = new ArrayList<>();
        int x = bottomBlockX;
        while (x <= topBlockX) {
            int z = bottomBlockZ;
            while (z <= topBlockZ) {
                int y = bottomBlockY;
                while (y <= topBlockY) {
                    Block block = start.getWorld().getBlockAt(x, y, z);
                    locs.add(block.getLocation());
                    ++y;
                }
                ++z;
            }
            ++x;
        }
        return locs;
    }
}
