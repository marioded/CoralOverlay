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
            LOGO_RESIZED = new ImageIcon(new ImageIcon(CoralOverlay.class.getResource("/coral.png"))
                    .getImage().getScaledInstance(110, 70, Image.SCALE_SMOOTH));

            MINECRAFT_FONT = Font.createFont(Font.TRUETYPE_FONT, CoralOverlay.class.getResourceAsStream("/minecraftia.ttf"));

            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(MINECRAFT_FONT);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private TextUtils() {
    }

    public static JLabel createLabel(String text, int x, int y, int size, Color color) {
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

    public static Color colorizeLevel(int level) {
        if (level < 200) {
            return Color.WHITE;
        } else if (level < 300) {
            return Color.YELLOW;
        } else if (level < 400) {
            return Color.ORANGE;
        } else if (level < 500) {
            return Color.RED;
        } else if (level < 600) {
            return Color.PINK;
        } else if (level < 700) {
            return Color.MAGENTA;
        } else if (level < 800) {
            return Color.CYAN;
        } else if (level < 900) {
            return Color.BLUE;
        } else if (level < 1000) {
            return Color.GREEN;
        }

        return Color.GRAY;
    }

    public static int extractMaxPlayers(String[] args) {
        try {
            Matcher matcher = MAX_PLAYERS_PATTERN.matcher(args[args.length - 1]);

            if (matcher.find()) return Integer.parseInt(matcher.group(1));
        } catch (Exception ignored) {
        }

        return 8;
    }
}
