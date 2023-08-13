package dev.fluyd.sumoevent.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerEnterWaterEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();

    public PlayerEnterWaterEvent(Player who) {
        super(who);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
