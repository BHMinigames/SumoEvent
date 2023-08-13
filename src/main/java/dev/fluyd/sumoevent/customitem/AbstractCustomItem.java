package dev.fluyd.sumoevent.customitem;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCustomItem implements CustomItem {
    protected final Material item;
    protected final String itemName;
    protected List<String> itemLore = new ArrayList<>();

    public AbstractCustomItem(Material item, String itemName, String... loreLines) {
        this.item = item;
        this.itemName = ChatColor.translateAlternateColorCodes('&', itemName);
        for (String loreLine : loreLines) {
            itemLore.add(ChatColor.translateAlternateColorCodes('&', loreLine));
        }
    }

    @Override
    public ItemStack getItem() {
        ItemStack itemStack = new ItemStack(item);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(itemName);
        itemMeta.setLore(itemLore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public String getRawName() {
        return ChatColor.stripColor(itemName);
    }

    @Override
    public abstract void onUse(Player player);

    @Override
    public abstract void onPlace(Player player, Block block);
}
