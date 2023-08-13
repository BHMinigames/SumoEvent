package dev.fluyd.sumoevent.customitem.items;

import dev.fluyd.sumoevent.SumoEvent;
import dev.fluyd.sumoevent.customitem.AbstractCustomItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import dev.fluyd.sumoevent.popuptower.Direction;
import dev.fluyd.sumoevent.popuptower.NewAPI;
import dev.fluyd.sumoevent.popuptower.TowerBuilder;

public class PopupTower extends AbstractCustomItem {
    public TowerBuilder builder = new TowerBuilder();

    public PopupTower() {
        super(Material.CHEST, "&cPopup Tower", "&7&oPlace this to create a magical tower.");
    }

    @Override
    public void onUse(Player player) {

    }

    @Override
    public void onPlace(Player player, Block block) {
        final Location location = block.getLocation();
        this.loadChunks(location);

        final Direction direction = SumoEvent.isNewApi() ? Direction.getDirection(NewAPI.getBlockFace(block)) : Direction.getDirection(block.getData());
        this.builder.build(location, player.getUniqueId(), direction);
    }

    private void loadChunks(final Location location) {
        if (!location.getChunk().isLoaded())
            location.getChunk().load();
    }
}
