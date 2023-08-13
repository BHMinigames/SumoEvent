package dev.fluyd.sumoevent.ranks;

import dev.fluyd.sumoevent.utils.LeaderboardUtils;
import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.UUID;

@UtilityClass
public class RankManager {
    public String getRankString(UUID uuid) {
        Rank rank = getRank(uuid);
        return Rank.valueOf(rank.name().toUpperCase()).getChatDisplay();
    }
    public Rank getRank(UUID uuid) {
        if (LeaderboardUtils.playerExists(uuid)) {
            try {
                YamlConfiguration leaderboard = LeaderboardUtils.getLeaderboard();
                return Rank.valueOf(leaderboard.getConfigurationSection("players").getConfigurationSection(uuid.toString()).get("rank").toString().toUpperCase());
            }
            catch (Exception exception) {
                System.out.println("Error while getting the rank for the player with the UUID of " + uuid.toString() + ".");
                LeaderboardUtils.createLeaderboardConfig();
                return Rank.NONE;
            }
        }
        LeaderboardUtils.createPlayerSection(uuid);
        return Rank.NONE;
    }

    public Rank getRank(String rank) {
        if (rankExists(rank)) {
            return Rank.valueOf(rank);
        }
        return Rank.NONE;
    }

    public String getRankList() {
        StringBuilder stringBuilder = new StringBuilder();
        Rank[] rankArray = Rank.values();
        int n = rankArray.length;
        int n2 = 0;
        while (n2 < n) {
            Rank rank = rankArray[n2];
            stringBuilder.append(rank).append(", ");
            ++n2;
        }
        return ChatColor.GREEN + "List of all ranks: " + ChatColor.GRAY + stringBuilder.substring(0, stringBuilder.length() - 2) + ".";
    }

    public void setRank(UUID uuid, Rank rank) {
        if (LeaderboardUtils.playerExists(uuid)) {
            try {
                YamlConfiguration leaderboard = LeaderboardUtils.getLeaderboard();
                leaderboard.getConfigurationSection("players").getConfigurationSection(uuid.toString()).set("rank", rank.name());
                LeaderboardUtils.saveLeaderboard(leaderboard);
            }
            catch (Exception exception) {
                System.out.println("Error while setting the rank for the player with the UUID of " + uuid.toString() + ".");
                LeaderboardUtils.createLeaderboardConfig();
            }
        } else {
            LeaderboardUtils.createPlayerSection(uuid);
        }
    }

    public boolean rankExists(String rank) {
        Rank[] rankArray = Rank.values();
        int n = rankArray.length;
        int n2 = 0;
        while (n2 < n) {
            Rank r = rankArray[n2];
            if (r.name().equals(rank)) {
                return true;
            }
            ++n2;
        }
        return false;
    }
}
