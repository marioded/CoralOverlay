package tech.zmario.coraloverlay;

import tech.zmario.coraloverlay.manager.UserManager;
import tech.zmario.coraloverlay.windows.OverlayFrame;

import javax.swing.*;
import java.util.Scanner;
import java.util.logging.Logger;

public class CoralOverlay extends JFrame {

    public static final Logger LOGGER = Logger.getLogger("CoralOverlay");
    private final OverlayFrame overlayFrame;
    private final UserManager userManager;
    private final String playerName;
    private String disguisedName;

    private CoralOverlay(String playerName) {
        this.userManager = new UserManager();
        this.overlayFrame = OverlayFrame.create(this);
        this.playerName = playerName;

        start();
    }

    public static void create(String playerName) {
        new CoralOverlay(playerName);
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
        return disguisedName != null ? disguisedName : playerName;
    }

    public void setDisguisedName(String disguisedName) {
        this.disguisedName = disguisedName;
    }
}
