package tech.zmario.coraloverlay.threads;

import tech.zmario.coraloverlay.CoralOverlay;
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
import java.util.stream.Collectors;

public class LogsReaderRunnable implements Runnable {

    private final CoralOverlay coralOverlay;
    private final OverlayFrame overlayFrame;

    private final Path logsFilePath = ClientUtils.detectClient();

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

        if (logsFilePath == null || !logsFilePath.toFile().exists()) {
            JOptionPane.showMessageDialog(null, "Non è stato trovato il file di log di Minecraft. " +
                            "Assicurati di averlo installato e di averlo avviato almeno una volta.",
                    "CoralOverlay", JOptionPane.ERROR_MESSAGE, TextUtils.LOGO_RESIZED);
            System.exit(0);
        }
    }

    @Override
    public void run() {
        if (logsFilePath == null || !logsFilePath.toFile().exists()) return;

        try {
            List<String> lines = Files.readAllLines(logsFilePath, StandardCharsets.ISO_8859_1)
                    .stream().filter(line -> line != null && !line.isEmpty() && !line.isBlank())
                    .collect(Collectors.toList());

            Collections.reverse(lines);
            lines = lines.subList(0, Math.min(lines.size(), 3));

            for (String line : lines) {
                if (line.isEmpty() || line.isBlank()) continue;
                String[] args = line.split(" ");

                if (args.length < 3) continue;

                args = Arrays.copyOfRange(args, 3, args.length);

                if (args.length == 0) continue;

                if (args[0].equals("[CHAT]")) args = Arrays.copyOfRange(args, 1, args.length);

                line = String.join(" ", args);

                if (line.isEmpty() || line.isBlank()) continue;
                String owner = coralOverlay.getPlayerName();

                if (line.contains("è uscito (") || line.endsWith("FINAL KILL!")) {
                    String playerName = args[0];

                    overlayFrame.removePlayer(playerName);
                } else if (line.contains("è entrato (")) {
                    handlePlayerJoin(args);
                } else if (line.startsWith("(Da " + coralOverlay.getPlayerName() + ")")) {
                    String[] playerNamesArray = Arrays.copyOfRange(args, 2, args.length);

                    for (String playerName : playerNamesArray) {
                        overlayFrame.addPlayer(playerName);
                    }
                } else if (line.endsWith(" è caduto nel vuoto.")) {
                    String playerName = args[0];

                    overlayFrame.addPlayer(playerName);
                } else if (line.contains(", " + owner) && !line.contains(":")) {
                    String[] split = line.split(", ");

                    for (String playerName : split) {
                        if (playerName.contains(owner)) continue;

                        overlayFrame.addPlayer(playerName);
                    }
                } else if (line.contains("è entrato nella lobby")) overlayFrame.clearAll();
                else continue;

                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handlePlayerJoin(String[] args) {
        String playerName = args[0];

        int maxPlayers = TextUtils.extractMaxPlayers(args);

        overlayFrame.setMaxPlayers(maxPlayers);

        if (playerName.equalsIgnoreCase(coralOverlay.getPlayerName())) {
            overlayFrame.clearAll();
            return;
        }

        overlayFrame.addPlayer(playerName);
    }
}
