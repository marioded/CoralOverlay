package tech.zmario.coraloverlay.enums;

import java.awt.*;

public enum RankColor {

    USER(new Color(0xE3E3E3)),
    VIP(new Color(0x55FF55)),
    LEGEND(new Color(0x4A6BFF)),
    CHAMPION(new Color(0xFFAA00)),
    FAMOUS(new Color(0xFF55FF)),
    YOUTUBE(new Color(0xFFAA00)),
    STRAMER(new Color(0xAA00AA)),
    BUILDER(new Color(0xFFFF55)),
    HELPER(new Color(0x00AA00)),
    MOD(new Color(0x00AAAA)),
    SRMOD(new Color(0x00AAAA)),
    JRDEV(new Color(0x55FFFF)),
    DEV(new Color(0x55FFFF)),
    SRDEV(new Color(0x55FFFF)),
    ADMIN(new Color(0xFF5555)),
    SRADMIN(new Color(0xFF5555)),
    OWNER(new Color(0xAA0000)),
    DISGUISED(new Color(0xFF5555));

    private final Color color;

    RankColor(Color color) {
        this.color = color;
    }

    public static RankColor getRankColor(String prefix) {
        for (RankColor rankColor : values()) {
            if (prefix.contains(rankColor.name())) return rankColor;
        }

        return USER;
    }

    public Color getColor() {
        return color;
    }
}
