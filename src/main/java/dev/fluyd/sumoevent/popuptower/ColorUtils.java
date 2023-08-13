package dev.fluyd.sumoevent.popuptower;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public final class ColorUtils {
    public static ItemStack convertToWool(final UUID uuid, final int yLevel) {
        return WoolColors.getWoolColorFromUUID(uuid, yLevel).getWool();
    }
}