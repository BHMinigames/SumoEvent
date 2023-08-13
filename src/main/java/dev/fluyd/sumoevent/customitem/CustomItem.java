package dev.fluyd.sumoevent.customitem;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface CustomItem {
    ItemStack getItem();
    String getRawName();
    void onUse(Player player);
    void onPlace(Player player, Block block);

}
