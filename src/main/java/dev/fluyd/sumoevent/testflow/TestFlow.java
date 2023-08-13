package dev.fluyd.sumoevent.testflow;

import org.bukkit.entity.Player;

public interface TestFlow {
    String getName();
    void run(Player sender, String[] args);
}
