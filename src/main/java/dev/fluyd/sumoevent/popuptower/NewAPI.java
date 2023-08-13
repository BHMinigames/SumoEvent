package dev.fluyd.sumoevent.popuptower;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;

public final class NewAPI {
    public static BlockFace getBlockFace(final Block block) {
        final BlockData data = block.getBlockData();
        if (!(data instanceof Directional))
            return null;

        final Directional directional = (Directional) data;
        return directional.getFacing();
    }

    public static void handleRotation(final Block block, final Direction direction) {
        final BlockData data = block.getBlockData();
        if (!(data instanceof Directional))
            return;

        final Directional directional = (Directional) data;
        directional.setFacing(direction.getFace());

        block.setBlockData(directional);
    }

}