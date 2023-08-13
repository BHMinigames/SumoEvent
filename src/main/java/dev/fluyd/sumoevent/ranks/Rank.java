package dev.fluyd.sumoevent.ranks;

import lombok.Getter;

@Getter
public enum Rank {
    NONE("&7 "),
    VIP("&a[VIP] "),
    VIP_PLUS("&a[VIP&6+&a] "),
    MVP("&b[MVP] "),
    MVP_PLUS("&b[MVP&c+&b] "),
    MVP_PLUS_PLUS("&6[MVP&c++&6] "),
    GAME_MASTER("&2[GM] "),
    ADMIN("&c[ADMIN] "),
    OWNER("&c[OWNER] ");

    private final String chatDisplay;

    Rank(String chatDisplay) {
        this.chatDisplay = chatDisplay;
    }
}
