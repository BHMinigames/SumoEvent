package dev.fluyd.sumoevent.customitem;

import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomItemManager {
    private final Map<String, CustomItem> CUSTOM_ITEMS = new HashMap<>();

    public void registerItem(CustomItem item) {
        System.out.println(item.getRawName());
        CUSTOM_ITEMS.put(item.getRawName().toUpperCase().replace(" ", "_"), item);
    }

    public List<String> getAllItemNames() {
        return CUSTOM_ITEMS.keySet().stream()
                .map(name -> name.toUpperCase().replace(" ", "_"))
                .collect(Collectors.toList());
    }

    public Optional<CustomItem> getItem(String name) {
        return Optional.ofNullable(CUSTOM_ITEMS.get(ChatColor.stripColor(name).toUpperCase().replace(" ", "_")));
    }
}