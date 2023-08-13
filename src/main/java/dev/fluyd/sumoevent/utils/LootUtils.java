package dev.fluyd.sumoevent.utils;

import dev.fluyd.sumoevent.SumoEvent;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

@UtilityClass
public class LootUtils {
    private File lootFile;
    public ArrayList<Block> chestLocations = new ArrayList<>();
    public HashMap<Block, Integer> openedChests = new HashMap<>();
    public HashMap<Block, Integer> regenCounter = new HashMap<>();
    public HashMap<Block, ArmorStand> chestNametags = new HashMap<>();
    public BukkitTask chestNametagTask;

    public void createLootFile(boolean reset) {
        lootFile = new File(SumoEvent.INSTANCE.getDataFolder(), "loot.yml");
        if (reset) {
            lootFile.delete();
        }
        if (!lootFile.exists()) {
            lootFile.getParentFile().mkdirs();
            SumoEvent.INSTANCE.saveResource("loot.yml", false);
        }
    }

    private YamlConfiguration getLoot() {
        return YamlConfiguration.loadConfiguration(lootFile);
    }

    public ItemStack getRandomLoot() {
        YamlConfiguration lootConfig = getLoot();
        ArrayList<ItemStack> lootList = new ArrayList<>();
        ConfigurationSection lootSection = lootConfig.getConfigurationSection("loot");
        for (String item : lootSection.getKeys(false)) {
            lootList.add(getParsedItemStack(item));
        }
        if (lootList.isEmpty()) {
            return null;
        }
        Random random = new Random();

        if (random.nextDouble() >= 0.98) {
            return SumoEvent.INSTANCE.customItemManager.getItem("POPUP_TOWER").get().getItem();
        }
        if (random.nextDouble() >= 0.98) {
            return SumoEvent.INSTANCE.customItemManager.getItem("TRACER").get().getItem();
        }
        if (random.nextDouble() >= 0.987) {
            return SumoEvent.INSTANCE.customItemManager.getItem("MAGIC_PLATFORM").get().getItem();
        }
        if (random.nextDouble() >= 0.991) {
            return SumoEvent.INSTANCE.customItemManager.getItem("GRAVITY_WAND").get().getItem();
        }

        return lootList.get(random.nextInt(lootList.size()));
    }

    public void handleChestNametags() {
        chestNametagTask = Bukkit.getScheduler().runTaskTimerAsynchronously(SumoEvent.INSTANCE, () -> {
            new HashMap<>(openedChests).forEach((block, openedTime) -> {
                if (block.getType() != Material.CHEST) {
                    openedChests.remove(block);
                    if (chestNametags.containsKey(block)) {
                        chestNametags.get(block).setCustomName(ChatColor.RED + "Broken");
                    }
                    return;
                }

                if (openedTime == -1) {
                    if (chestNametags.containsKey(block)) {
                        chestNametags.get(block).remove();
                        chestNametags.remove(block);
                    }

                    Bukkit.getScheduler().runTask(SumoEvent.INSTANCE, () -> {
                        ArmorStand nameTag = (ArmorStand) block.getWorld().spawnEntity(block.getLocation().add(0.5, 1, 0.5), EntityType.ARMOR_STAND);

                        nameTag.setVisible(false);  // Make ArmorStand invisible
                        nameTag.setGravity(false);  // Ensure it doesn't fall
                        nameTag.setMarker(true);  // This makes the ArmorStand non-interactable (can't push it, etc.)
                        if (regenCounter.containsKey(block)) {
                            nameTag.setCustomName(ChatColor.GREEN + "Unopened " + ChatColor.YELLOW + regenCounter.get(block) + ChatColor.YELLOW + "x");
                        } else {
                            nameTag.setCustomName(ChatColor.GREEN + "Unopened");
                        }
                        nameTag.setCustomNameVisible(true);

                        chestNametags.put(block, nameTag);
                    });
                } else {
                    // Decrease the opened time by 1
                    int newOpenedTime = openedTime - 1;
                    openedChests.put(block, newOpenedTime);

                    if (newOpenedTime <= 0) {
                        openedChests.put(block, -1);
                        if (chestNametags.containsKey(block)) {
                            ArmorStand nameTag = chestNametags.get(block);
                            nameTag.remove();
                            chestNametags.remove(block);
                            // Update the loot
                            Random random = new Random();
                            Chest chest = (Chest) block.getState();
                            chest.getInventory().clear();
                            int r2 = random.nextInt(7) + 6;
                            for (int x = 0; x < r2; x++) {
                                int r = random.nextInt(27);
                                chest.getInventory().setItem(r, getRandomLoot());
                                chest.update();
                            }

                            // Increase the regen counter
                            if (regenCounter.containsKey(block)) {
                                regenCounter.put(block, regenCounter.get(block) + 1);
                            } else {
                                regenCounter.put(block, 1);
                            }
                        }
                    } else {
                        if (chestNametags.containsKey(block)) {
                            String formattedTime = secondsToMMSS(newOpenedTime);
                            chestNametags.get(block).setCustomName(ChatColor.GREEN + formattedTime);
                        }
                    }
                }
            });
        }, 0L, 20L);
    }

    private String secondsToMMSS(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;

        if (minutes > 0) {
            return String.format("%02d:%02d", minutes, remainingSeconds);
        } else {
            return String.format("%02d", remainingSeconds);
        }
    }


    public void placeChests() {
        if (LocationsUtils.getLocations().getConfigurationSection("locations").getKeys(false).isEmpty()) {
            System.out.println("There were no locations found so no chests were placed for DeathMatch.");
            return;
        }
        try {
            for (Location location : LocationsUtils.getLocationsList()) {
                if (!chestLocations.contains(location.getBlock())) {
                    Random random = new Random();
                    Block block = location.getBlock();
                    block.setType(Material.CHEST);
                    Chest chest = (Chest) block.getState();
                    chest.setRawData((byte) getRandomDirection());
                    int r2 = random.nextInt(7) + 6;
                    for (int x = 0; x < r2; x++) {
                        int r = random.nextInt(27);
                        chest.getInventory().setItem(r, getRandomLoot());
                        chest.update();
                    }
                    chestLocations.add(location.getBlock());
                    openedChests.put(location.getBlock(), -1);
                }
            }
        } catch (Exception exception) {
            System.out.println("Something went wrong when parsing locations.yml: " + exception + "\nResetting locations.yml!");
        }
    }

    private int getRandomDirection() {
        Random random = new Random();
        int r = random.nextInt(4);
        switch (r) {
            case 1:
                return 2;
            case 2:
                return 4;
            case 3:
                return 5;
            default:
                return 3;
        }
    }

    public void removeAllChests() {
        for (Block block : chestLocations) {
            block.setType(Material.AIR);
        }
    }

    public ItemStack getParsedItemStack(String itemStackString) {
        String[] itemStackArray = itemStackString.split(",");
        ItemStack itemStack;

        try {
            switch (itemStackArray.length) {
                // If the loot is just a material
                case 1:
                case 2:
                    itemStack = handleSpecialItems(itemStackArray);
                    if (itemStack != null) {
                        return itemStack;
                    }
                    int amount = itemStackArray.length == 2 ? Integer.parseInt(itemStackArray[1]) : 1;
                    itemStack = new ItemStack(Material.getMaterial(itemStackArray[0].toUpperCase()), amount);
                    break;
                // If the loot is a material and a damage value
                case 3:
                case 4:
                case 5:
                    short damageValue = (short) Integer.parseInt(itemStackArray[2]);
                    itemStack = new ItemStack(Material.getMaterial(itemStackArray[0].toUpperCase()), Integer.parseInt(itemStackArray[1]), damageValue);

                    ItemMeta itemMeta = itemStack.getItemMeta();
                    if (itemStackArray.length >= 4) {
                        itemMeta.addEnchant(Enchantment.getByName(itemStackArray[3].toUpperCase()), itemStackArray.length == 5 ? Integer.parseInt(itemStackArray[4]) : 1, true);
                    }
                    itemStack.setItemMeta(itemMeta);
                    break;

                // If the loot is a material, a damage value, an enchantment, and has an item name
                case 6:
                    damageValue = (short) Integer.parseInt(itemStackArray[2]);
                    itemStack = new ItemStack(Material.getMaterial(itemStackArray[0].toUpperCase()), Integer.parseInt(itemStackArray[1]), damageValue);
                    itemMeta = itemStack.getItemMeta();

                    itemMeta.addEnchant(Enchantment.getByName(itemStackArray[3].toUpperCase()), Integer.parseInt(itemStackArray[4]), true);
                    itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemStackArray[5]));

                    itemStack.setItemMeta(itemMeta);
                    break;

                default:
                    System.out.println("Something went wrong when parsing loot.yml: Too many arguments were provided in " + itemStackString + "\nResetting loot.yml");
                    createLootFile(true);
                    return null;
            }

            return itemStack;
        } catch (Exception e) {
            System.out.println("Something went wrong when parsing loot.yml: " + e + "\nResetting loot.yml!");
            createLootFile(true);
            return null;
        }
    }

    private ItemStack handleSpecialItems(String[] itemStackArray) {
        Material material = null;
        String displayName = null;

        switch (itemStackArray[0].toUpperCase()) {
            case "BRIDGE_EGG":
                material = Material.EGG;
                displayName = "&bBridge Egg";
                break;

            case "POPUPTOWER":
                material = Material.TRAPPED_CHEST;
                displayName = ChatColor.AQUA + "Compact Pop-Up Tower";
                break;
        }

        if (material != null) {
            int amount = itemStackArray.length == 2 ? Integer.parseInt(itemStackArray[1]) : 1;
            ItemStack itemStack = new ItemStack(material, amount);

            if (displayName != null) {
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
                itemStack.setItemMeta(itemMeta);
            }
            return itemStack;
        }
        return null;
    }

}
