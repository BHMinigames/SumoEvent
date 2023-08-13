package dev.fluyd.sumoevent.testflow.flows;

import dev.fluyd.sumoevent.game.GameManager;
import dev.fluyd.sumoevent.testflow.TestFlow;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class DeleteBlocks implements TestFlow {

    @Override
    public String getName() {
        return "DELETE_BLOCKS";
    }

    @Override
    public void run(Player sender, String[] args) {
        GameManager.placedBlocks.forEach(block -> block.getBlock().setType(Material.AIR));

        sender.sendMessage(ChatColor.GREEN + "Deleted " + ChatColor.YELLOW + GameManager.placedBlocks.size() + ChatColor.GREEN + " blocks.");

        GameManager.placedBlocks.clear();
    }
}
