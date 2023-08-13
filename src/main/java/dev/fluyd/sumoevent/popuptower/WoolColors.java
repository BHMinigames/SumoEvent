package dev.fluyd.sumoevent.popuptower;

import dev.fluyd.sumoevent.SumoEvent;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.net.HttpURLConnection;
import java.util.UUID;

@Getter
public enum WoolColors {
    /**
     * Hypixel colors
     */
    RED("Red", (byte) 14, Material.getMaterial("RED_WOOL"), ChatColor.RED),
    BLUE("Blue", (byte) 11, Material.getMaterial("BLUE_WOOL"), ChatColor.BLUE),
    GREEN("Green", (byte) 5, Material.getMaterial("LIME_WOOL"), ChatColor.GREEN),
    YELLOW("Yellow", (byte) 4, Material.getMaterial("YELLOW_WOOL"), ChatColor.YELLOW),
    AQUA("Aqua", (byte) 9, Material.getMaterial("CYAN_WOOL"), ChatColor.AQUA),
    WHITE("White", (byte) 0, Material.getMaterial("WHITE_WOOL"), ChatColor.WHITE),
    PINK("Pink", (byte) 6, Material.getMaterial("PINK_WOOL"), ChatColor.LIGHT_PURPLE),
    GRAY("Gray", (byte) 7, Material.getMaterial("GRAY_WOOL"), ChatColor.GRAY),

    /**
     * Other colors
     */
    ORANGE("Orange", (byte) 1, Material.getMaterial("ORANGE_WOOL"), ChatColor.GOLD),
    MAGENTA("Magenta", (byte) 2, Material.getMaterial("MAGENTA_WOOL"), ChatColor.LIGHT_PURPLE),
    LIGHT_BLUE("Light Blue", (byte) 3, Material.getMaterial("LIGHT_BLUE_WOOL"), ChatColor.BLUE),
    LIGHT_GRAY("Light Gray", (byte) 8, Material.getMaterial("LIGHT_GRAY"), ChatColor.GRAY);
//    PURPLE("Purple", (byte) 10, Material.getMaterial("PURPLE_WOOL"), ChatColor.DARK_PURPLE),
//    BROWN("Brown", (byte) 12, Material.getMaterial("BROWN_WOOL"), ChatColor.BLACK),
//    BLACK("Black", (byte) 15, Material.getMaterial("BLACK_WOOL"), ChatColor.BLACK);

    private final String name;
    private final byte legacyColorId;
    private final Material modernMaterial;
    private final ChatColor color;

    private final ItemStack wool;

    WoolColors(final String name, final byte legacyColorId, final Material modernMaterial, final ChatColor color) {
        this.name = name;
        this.legacyColorId = legacyColorId;
        this.modernMaterial = modernMaterial;
        this.color = color;

        this.wool = SumoEvent.isNewApi() ? new ItemStack(this.modernMaterial, 1) : new ItemStack(Material.getMaterial("WOOL"), 1, this.legacyColorId);
    }

    /**
     * Gets the wool color from the hash of the specified UUID
     * @param uuid
     * @param yLevel
     * @return
     */
    public static WoolColors getWoolColorFromUUID(final UUID uuid, final int yLevel) {
        final int versionModifier = HttpURLConnection.HTTP_NOT_FOUND;  // Adjust this number as desired

        long combinedHash = uuid.getMostSignificantBits() ^ uuid.getLeastSignificantBits();
        int mixedHash = (int) (combinedHash + yLevel + versionModifier);
        int ordinal = Math.abs(mixedHash) % WoolColors.values().length;
        return WoolColors.values()[ordinal];
    }

    /**
     * Gets the WoolColors enum type by its name
     * @param name
     * @return
     */
    public static final WoolColors getWoolColor(final String name) {
        for (final WoolColors woolColor : WoolColors.values())
            if (woolColor.getName().equals(name))
                return woolColor;

        return null;
    }
}