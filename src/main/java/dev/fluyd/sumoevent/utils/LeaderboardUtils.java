package dev.fluyd.sumoevent.utils;

import dev.fluyd.sumoevent.SumoEvent;
import dev.fluyd.sumoevent.ranks.Rank;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

@UtilityClass
public class LeaderboardUtils {
    public static HashMap<UUID, Objects> lastDamager = new HashMap();
    private static final File leaderboardPath = new File(SumoEvent.INSTANCE.getDataFolder(), "leaderboard.yml");

    public void createLeaderboardConfig() {
        if (!leaderboardPath.getParentFile().exists()) {
            leaderboardPath.getParentFile().mkdirs();
        }

        if (!leaderboardPath.exists()) {
            try {
                FileWriter fileWriter = new FileWriter(leaderboardPath);
                YamlConfiguration leaderboardConfig = new YamlConfiguration();
                leaderboardConfig.createSection("players");
                fileWriter.flush();
                fileWriter.write(leaderboardConfig.saveToString());
                fileWriter.close();
            } catch (Exception var3) {
                System.out.println("Error while creating config section for leaderboard.yml: " + var3);
            }
        }

    }

    public YamlConfiguration getLeaderboard() {
        return YamlConfiguration.loadConfiguration(leaderboardPath);
    }

    public void saveLeaderboard(YamlConfiguration leaderboard) {
        try {
            FileWriter filewriter = new FileWriter(leaderboardPath);
            filewriter.write(leaderboard.saveToString());
            filewriter.close();
        } catch (Exception var3) {
            System.out.println("Error while saving leaderboard.yml: " + var3);
        }

    }

    public void createPlayerSection(UUID uuid) {
        YamlConfiguration leaderboard = getLeaderboard();

        try {
            leaderboard.getConfigurationSection("players").createSection(uuid.toString());
            leaderboard.getConfigurationSection("players").getConfigurationSection(uuid.toString()).set("current-username", Bukkit.getPlayer(uuid).getName());
            leaderboard.getConfigurationSection("players").getConfigurationSection(uuid.toString()).set("gamesplayed", 0);
            leaderboard.getConfigurationSection("players").getConfigurationSection(uuid.toString()).set("wins", 0);
            leaderboard.getConfigurationSection("players").getConfigurationSection(uuid.toString()).set("losses", 0);
            leaderboard.getConfigurationSection("players").getConfigurationSection(uuid.toString()).set("kills", 0);
            leaderboard.getConfigurationSection("players").getConfigurationSection(uuid.toString()).set("deaths", 0);
            leaderboard.getConfigurationSection("players").getConfigurationSection(uuid.toString()).set("winstreak", 0);
            leaderboard.getConfigurationSection("players").getConfigurationSection(uuid.toString()).set("bestwinstreak", 0);
            leaderboard.getConfigurationSection("players").getConfigurationSection(uuid.toString()).set("experience", 1000);
            leaderboard.getConfigurationSection("players").getConfigurationSection(uuid.toString()).set("level", 1.0);
            leaderboard.getConfigurationSection("players").getConfigurationSection(uuid.toString()).set("rank", Rank.NONE.name());
            saveLeaderboard(leaderboard);
        } catch (Exception var4) {
            System.out.println("Failed creating a config section for " + Bukkit.getOfflinePlayer(uuid).getName() + " in leaderboard.yml.");
        }

    }

    public ConfigurationSection getPlayerSection(UUID uuid, Boolean catchCreate) {
        if (playerExists(uuid)) {
            try {
                YamlConfiguration leaderboard = getLeaderboard();
                return leaderboard.getConfigurationSection("players").getConfigurationSection(uuid.toString());
            } catch (Exception var4) {
                System.out.println("Error while getting the config section for the player with the uuid " + uuid.toString() + ".");
                createLeaderboardConfig();
            }
        } else if (catchCreate) {
            createPlayerSection(uuid);
        }

        return null;
    }

    public Boolean playerExists(UUID uuid) {
        YamlConfiguration leaderboard = getLeaderboard();
        return leaderboard.getConfigurationSection("players").isConfigurationSection(uuid.toString());
    }

    public double calculateLevel(int experience) {
        return (double)experience / 1000.0;
    }

    public void saveLevel(UUID uuid) {
        try {
            YamlConfiguration leaderboard = getLeaderboard();
            double currentLevel = calculateLevel(leaderboard.getConfigurationSection("players").getConfigurationSection(uuid.toString()).getInt("experience"));
            double pastLevel = leaderboard.getConfigurationSection("players").getConfigurationSection(uuid.toString()).getDouble("level");
            if ((int)currentLevel > (int)pastLevel) {
                Bukkit.getPlayer(uuid).getWorld().playSound(Bukkit.getPlayer(uuid).getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                Bukkit.getPlayer(uuid).sendMessage(ChatColor.GREEN + "You just leveled up to level " + (int)currentLevel + "!");
            }

            leaderboard.getConfigurationSection("players").getConfigurationSection(uuid.toString()).set("level", currentLevel);
            saveLeaderboard(leaderboard);
        } catch (Exception var7) {
            System.out.println("Error while saving level.");
            createLeaderboardConfig();
        }

    }

    public double getLevel(UUID uuid) {
        try {
            return getLeaderboard().getConfigurationSection("players").getConfigurationSection(uuid.toString()).getDouble("level");
        } catch (Exception var3) {
            System.out.println("Error while getting level.");
            createLeaderboardConfig();
            return 0.0;
        }
    }
}
