package tech.zmario.coraloverlay;

import tech.zmario.coraloverlay.manager.UserManager;
import tech.zmario.coraloverlay.windows.OverlayFrame;

import javax.swing.*;
import java.util.Scanner;

public class CoralOverlay extends JFrame {

    private final String playerName;

    private final OverlayFrame overlayFrame;
    private final UserManager userManager;

    private CoralOverlay(String playerName) {
        this.userManager = new UserManager();
        this.overlayFrame = OverlayFrame.create(this);
        this.playerName = playerName;

        start();
    }

    protected void start() {
        overlayFrame.start();

        new Thread(() -> {
            try (Scanner scanner = new Scanner(System.in)) {
                String line;

                while ((line = scanner.nextLine()) != null) {
                    String[] args = line.split(" ");

                    for (String arg : args) overlayFrame.addPlayer(arg);
                }
            }
        }).start();
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public OverlayFrame getOverlayFrame() {
        return overlayFrame;
    }

    public String getPlayerName() {
        return playerName;
    }

    public static CoralOverlay create(String playerName) {
        return new CoralOverlay(playerName);
    }
}
