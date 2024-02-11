package tech.zmario.coraloverlay.threads;

import tech.zmario.coraloverlay.CoralOverlay;
import tech.zmario.coraloverlay.manager.UserManager;
import tech.zmario.coraloverlay.utils.ClientUtils;
import tech.zmario.coraloverlay.utils.TextUtils;
import tech.zmario.coraloverlay.windows.OverlayFrame;

import javax.swing.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LogsReaderRunnable implements Runnable {

    private final CoralOverlay coralOverlay;
    private final OverlayFrame overlayFrame;

    public LogsReaderRunnable(CoralOverlay coralOverlay) {
        this.coralOverlay = coralOverlay;
        this.overlayFrame = coralOverlay.getOverlayFrame();

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r);

            thread.setName("overlay-logsreader-executor");
            thread.setDaemon(true);

            return thread;
        });

        executor.scheduleWithFixedDelay(this, 30, 30, TimeUnit.MILLISECONDS);
        Runtime.getRuntime().addShutdownHook(new Thread(executor::shutdown));
    }

    @Override
    public void run() {
        Path logsFilePath = ClientUtils.detectClient();

        if (logsFilePath == null || !logsFilePath.toFile().exists()) {
            JOptionPane.showMessageDialog(null, "Non è stato trovato il file di log di Minecraft. " +
                            "Assicurati di averlo installato e di averlo avviato almeno una volta.", "CoralOverlay", JOptionPane.ERROR_MESSAGE,
                    TextUtils.LOGO_RESIZED);
            System.exit(0);
            return;
        }
        UserManager userManager = coralOverlay.getUserManager();
        List<String> playerNames = userManager.getPlayerNames();

        try {
            List<String> lines = Files.readAllLines(logsFilePath, StandardCharsets.ISO_8859_1);

            Collections.reverse(lines);

            lines = lines.subList(0, Math.min(1, lines.size()));

            for (String line : lines) {
                if (line == null) continue;
                String[] args = line.split(" ");
                args = Arrays.copyOfRange(args, 3, args.length);

                if (args.length == 0) continue;

                if (args[0].equals("[CHAT]")) args = Arrays.copyOfRange(args, 1, args.length);

                if (line.contains("è uscito (")) {
                    String playerName = args[0];

                    if (!playerNames.contains(playerName)) continue;

                    overlayFrame.removePlayer(playerName);
                    playerNames.remove(playerName);
                } else if (line.contains("è entrato (")) {
                    handlePlayerJoin(args, playerNames);
                } else if (line.contains("è entrato nella lobby")) {
                    overlayFrame.clearAll();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handlePlayerJoin(String[] args, List<String> playerNames) {
        String playerName = args[0];

        if (playerNames.contains(playerName)) return;
        int maxPlayers = TextUtils.extractMaxPlayers(args);

        overlayFrame.setMaxPlayers(maxPlayers);

        playerNames.add(playerName);

        if (playerName.equalsIgnoreCase(coralOverlay.getPlayerName())) overlayFrame.clearAll();
        else {
            overlayFrame.addPlayer(playerName);
        }
    }
}
