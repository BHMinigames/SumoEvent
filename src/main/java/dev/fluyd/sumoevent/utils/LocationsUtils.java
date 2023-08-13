package dev.fluyd.sumoevent.utils;

import dev.fluyd.sumoevent.SumoEvent;
import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

@UtilityClass
public class LocationsUtils {
    private static final File locationsPath = new File(SumoEvent.INSTANCE.getDataFolder(), "locations.yml");

    public void createLocationsConfig() {
        if (!locationsPath.getParentFile().exists()) {
            boolean madeDirectory = locationsPath.getParentFile().mkdirs();
            if (!madeDirectory) {
                System.out.println("Error while creating directory for locations.yml");
            }
        }
        if (!locationsPath.exists()) {
            try {
                FileWriter fileWriter = new FileWriter(locationsPath);
                YamlConfiguration locationsConfig = new YamlConfiguration();
                locationsConfig.createSection("locations");
                fileWriter.flush();
                fileWriter.write(locationsConfig.saveToString());
                fileWriter.close();
            } catch (Exception exception) {
                System.out.println("Error while creating config section for locations.yml: " + exception);
            }
        }
    }

    public YamlConfiguration getLocations() {
        return YamlConfiguration.loadConfiguration(locationsPath);
    }

    private void saveLocations(YamlConfiguration locations) {
        try {
            FileWriter filewriter = new FileWriter(locationsPath);
            filewriter.write(locations.saveToString());
            filewriter.close();
        } catch (Exception exception) {
            System.out.println("Error while saving locations.yml: " + exception);
        }
    }

    public ArrayList<Location> getLocationsList() {
        YamlConfiguration locationsConfig = getLocations();
        ArrayList<Location> locationsList = new ArrayList<>();
        ConfigurationSection locationsSection = locationsConfig.getConfigurationSection("locations");
        for (String location : locationsSection.getKeys(false)) {
            locationsList.add(ConfigUtils.getParsedLocation(location.replace('~', '.')));
        }
        if (!locationsList.isEmpty()) {
            return locationsList;
        }
        return null;
    }

    public void addLocation(Player p) {
        YamlConfiguration locations = getLocations();
        Location location = p.getLocation();
        try {
            locations.createSection("locations." + ConfigUtils.getUnparsedLocation(location).replace('.', '~'));
            saveLocations(locations);
            p.sendMessage(ChatColor.GREEN + "You have successfully added your current location as a place for a chest to spawn.");
        } catch (Exception exception) {
            System.out.println("Error while creating config section for locations.yml: " + exception);
            p.sendMessage(ChatColor.RED + "Adding your current location has failed...");
        }
    }

}
