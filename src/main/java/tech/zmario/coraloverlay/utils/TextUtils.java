package tech.zmario.coraloverlay.utils;

import tech.zmario.coraloverlay.CoralOverlay;
import tech.zmario.coraloverlay.enums.RankColor;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {

    public static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)&[0-9A-FK-ORX]");
    public static final Font MINECRAFT_FONT;
    public static final ImageIcon LOGO_RESIZED;

    private static final Pattern MAX_PLAYERS_PATTERN = Pattern.compile("/(\\d+)");

    static {
        try {
            LOGO_RESIZED = new ImageIcon(new ImageIcon(CoralOverlay.class.getResource("/coral.png")).getImage()
                    .getScaledInstance(110, 70, Image.SCALE_SMOOTH));
            MINECRAFT_FONT = Font.createFont(Font.TRUETYPE_FONT, CoralOverlay.class.getResourceAsStream("/minecraftia.ttf"));

            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(MINECRAFT_FONT);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private TextUtils() {
    }

    public static JLabel label(String text, int x, int y, int size, Color color) {
        JLabel label = new JLabel();

        label.setFont(MINECRAFT_FONT.deriveFont(Font.PLAIN, size - 5));
        label.setText("<html>" + text + "</html>");
        label.setForeground(color);
        label.setLocation(x, y);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);

        return label;
    }

    public static RankColor colorizeRank(String prefix) {
        prefix = STRIP_COLOR_PATTERN.matcher(prefix).replaceAll("");

        if (prefix.isEmpty()) prefix = "USER";

        if (prefix.equals("disguised")) return RankColor.DISGUISED;

        return RankColor.getRankColor(prefix);
    }

    public static Color colorizeLevel(int level) { // else if chains!
        if (level < 100) {
            return Color.WHITE;
        } else if (level < 200) {
            return Color.GREEN;
        } else if (level < 300) {
            return Color.YELLOW;
        } else if (level < 400) {
            return Color.ORANGE;
        } else if (level < 500) {
            return Color.PINK;
        } else if (level < 600) {
            return Color.MAGENTA;
        } else if (level < 700) {
            return Color.CYAN;
        } else if (level < 800) {
            return Color.BLUE;
        }

        return Color.RED;
    }

    public static int extractMaxPlayers(String[] args) {
        try {
            Matcher matcher = MAX_PLAYERS_PATTERN.matcher(args[args.length - 1]);

            if (matcher.find()) return Integer.parseInt(matcher.group(1));
        } catch (Exception ignored) {
        }

        return 8;
    }

    public static Color getFKDRColor(double fkdr) {
        if (fkdr < 1.5) {
            return Color.WHITE;
        } else if (fkdr < 2.5) {
            return Color.GREEN;
        } else if (fkdr < 5) {
            return Color.YELLOW;
        } else if (fkdr < 7.0) {
            return Color.ORANGE;
        } else {
            return Color.RED;
        }
    }

    public static Color getWLRColor(double wlr) {
        if (wlr < 0.3) {
            return Color.WHITE;
        } else if (wlr < 0.5) {
            return Color.GREEN;
        } else if (wlr < 0.9) {
            return Color.YELLOW;
        } else if (wlr < 1.5) {
            return Color.ORANGE;
        } else {
            return Color.RED;
        }
    }
}
