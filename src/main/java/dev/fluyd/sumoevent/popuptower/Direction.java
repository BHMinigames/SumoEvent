package dev.fluyd.sumoevent.popuptower;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

@Getter
public enum Direction {
    NORTH(3, BlockFace.SOUTH, 0),
    EAST(4, BlockFace.WEST, 90),
    SOUTH(2, BlockFace.NORTH, 180),
    WEST(5, BlockFace.EAST, 270);

    private final byte data;
    private final BlockFace face;
    private final double angle;

    /**
     * @param data - Magic number
     * @param face - Opposite to actual direction cuz spigot is gay
     * @param angle - In degrees
     */
    Direction(final int data, final BlockFace face, final double angle) {
        this.data = (byte) data;
        this.face = face;
        this.angle = Math.toRadians(angle);
    }

    /**
     * Shifts the provided location based on the direction
     * @param loc
     * @param center
     * @return
     */
    public Location shiftLocation(final Location center, final Location loc) {
        final double cos = Math.cos(this.angle);
        final double sin = Math.sin(this.angle);

        final Vector r1 = new Vector(cos, 0, -sin);
        final Vector r2 = new Vector(sin, 0, cos);
        final Vector vector = loc.clone().subtract(center).toVector();

        final Vector rotated = new Vector(r1.dot(vector), 0, r2.dot(vector));
        final Location finalLocation = rotated.add(center.toVector()).toLocation(loc.getWorld());
        finalLocation.setY(loc.getY());

        return finalLocation;
    }

    /**
     * Returns a Direction from the provided integer
     * @param data
     * @return
     */
    public static Direction getDirection(final int data) {
        for (final Direction direction : Direction.values())
            if (direction.getData() == data)
                return direction;

        return null;
    }

    public static Direction getDirection(final BlockFace face) {
        for (final Direction direction : Direction.values())
            if (direction.getFace() == face)
                return direction;

        return null;
    }
}