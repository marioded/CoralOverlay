package tech.zmario.coraloverlay.threads;

import tech.zmario.coraloverlay.CoralOverlay;
import tech.zmario.coraloverlay.manager.UserManager;
import tech.zmario.coraloverlay.windows.OverlayFrame;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UpdateRunnable implements Runnable {

    private final CoralOverlay coralOverlay;
    private final OverlayFrame overlayFrame;

    public UpdateRunnable(CoralOverlay coralOverlay) {
        this.coralOverlay = coralOverlay;
        this.overlayFrame = coralOverlay.getOverlayFrame();

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setName("overlay-executor");
            thread.setDaemon(true);

            return thread;
        });

        executor.scheduleWithFixedDelay(this, 30, 30, TimeUnit.SECONDS);

        Runtime.getRuntime().addShutdownHook(new Thread(executor::shutdown));
    }

    @Override
    public void run() {
        System.out.println("Running UpdateRunnable");
        UserManager userManager = coralOverlay.getUserManager();
        List<String> playerNames = userManager.getPlayerNames();

        JPanel tagPanel = overlayFrame.getTagPanel();
        JPanel streakPanel = overlayFrame.getStreakPanel();
        JPanel fkdrPanel = overlayFrame.getFkdrPanel();
        JPanel wlrPanel = overlayFrame.getWlrPanel();
        JPanel finalsPanel = overlayFrame.getFinalsPanel();
        JPanel winsPanel = overlayFrame.getWinsPanel();

        try {
            for (int i = 1; i < playerNames.size(); i++) {
                String playerName = playerNames.get(i - 1);

                System.out.println("Adding " + playerName + " to playersPanel");

                int finalI = i;
                userManager.getUser(playerName).thenAccept(bedWarsUser -> userManager.getPrefix(playerName).thenAccept(prefix -> {
                    double fkdr = Math.round((double) Math.max(bedWarsUser.getFinalKills(), 1) / (double) Math.max(bedWarsUser.getFinalDeaths(), 1) * 100.0) / 100.0;
                    double wlr = Math.round((double) Math.max(bedWarsUser.getWins(), 1) / (double) Math.max(1, bedWarsUser.getPlayed()) * 100.0) / 100.0;

                    setText(tagPanel, "[" + bedWarsUser.getLevel() + "*]", finalI);
                    setText(streakPanel, bedWarsUser.getWinStreak() + "", finalI);
                    setText(fkdrPanel, fkdr + "", finalI);
                    setText(wlrPanel, wlr + "", finalI);
                    setText(finalsPanel, bedWarsUser.getFinalKills() + "", finalI);
                    setText(winsPanel, bedWarsUser.getWins() + "", finalI);
                }));


                Thread.sleep(2000L);
            }
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    private void setText(JPanel panel, String text, int index) {
        ((JLabel) panel.getComponent(index)).setText("");
        ((JLabel) panel.getComponent(index)).setText("<html>" + text + "</html>");
    }
}
