package dev.fluyd.sumoevent.game;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class KitManager {
    public static void giveKit(Player p) {
        p.getInventory().setItem(0, createItem(getMaterialCompat("WOODEN_SWORD", "WOOD_SWORD"), null, 0, 1));
        p.getInventory().setItem(1, createItem(Material.FISHING_ROD, null, 0, 1));
        p.getInventory().setItem(2, getRandomStainedClay());
        p.getInventory().setItem(3, getRandomStainedClay());
        p.getInventory().setItem(4, createItem(Material.BOW, null, 0, 1));
        p.getInventory().setItem(5, createItem(getMaterialCompat("GOLDEN_PICKAXE", "GOLD_PICKAXE"), Enchantment.DIG_SPEED, 5, 1));
        p.getInventory().setItem(6, createItem(Material.SHEARS, Enchantment.DIG_SPEED, 5, 1));
        p.getInventory().setItem(8, createItem(Material.GOLDEN_APPLE, null, 0, 8));
        p.getInventory().setItem(9, createItem(Material.ARROW, null, 0, 64));

        p.getInventory().setHelmet(createItem(Material.IRON_HELMET, Enchantment.PROTECTION_ENVIRONMENTAL, 1, 1));
        p.getInventory().setChestplate(createItem(Material.IRON_CHESTPLATE, Enchantment.PROTECTION_ENVIRONMENTAL, 1, 1));
        p.getInventory().setLeggings(createItem(Material.IRON_LEGGINGS, Enchantment.PROTECTION_ENVIRONMENTAL, 1, 1));
        p.getInventory().setBoots(createItem(Material.IRON_BOOTS, Enchantment.PROTECTION_ENVIRONMENTAL, 1, 1));

        p.updateInventory();
    }

    public static Material getMaterialCompat(String modernName, String legacyName) {
        try {
            return Material.valueOf(modernName);
        } catch (IllegalArgumentException e) {
            try {
                return Material.valueOf(legacyName);
            } catch (IllegalArgumentException e2) {
                return null;
            }
        }
    }

    public static ItemStack getRandomStainedClay() {
        Material clayMaterial = getMaterialCompat("TERRACOTTA", "STAINED_CLAY");
        if (clayMaterial == null) return null;

        DyeColor randomColor = DyeColor.values()[new Random().nextInt(DyeColor.values().length)];
        short data = randomColor.getDyeData();

        return new ItemStack(clayMaterial, 64, data);
    }

    public static ItemStack createItem(Material material, Enchantment enchant, int enchantLevel, int amount) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();

        if (enchant != null) {
            meta.addEnchant(enchant, enchantLevel, true);
        }

        // Checking if the unbreakable boolean in GameManager is true
        meta.spigot().setUnbreakable(GameManager.unbreakableItems);

        item.setItemMeta(meta);
        return item;
    }
}
