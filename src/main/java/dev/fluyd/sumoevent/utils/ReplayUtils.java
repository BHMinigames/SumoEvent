package dev.fluyd.sumoevent.utils;

import dev.fluyd.sumoevent.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

public class ReplayUtils {
    private String replayName;
    public void startReplay(List<UUID> playerUUIDs) {
        // Generate a random 6 character string
        Random random = new Random();
        StringBuilder replayName = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            replayName.append(random.nextInt(10));
        }

        this.replayName = replayName.toString();

        // For each player in players, construct a new List<Player> from their uuid;s
        List<Player> players = playerUUIDs.stream().map(Bukkit::getPlayer).collect(Collectors.toList());
    }

    public String stopReplay() {

        GameManager.players.forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);

            if (player != null) {
                player.sendMessage(ChatColor.GREEN + "Recording complete!");
            }
        });

        return replayName;
    }
}
