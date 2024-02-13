package tech.zmario.coraloverlay;

import tech.zmario.coraloverlay.utils.TextUtils;

import javax.swing.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Application {

    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$s] %5$s%6$s%n");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException ignored) {
        }
        Object input = JOptionPane.showInputDialog(null, "Inserisci il tuo nome di Minecraft:", "CoralOverlay",
                JOptionPane.PLAIN_MESSAGE, TextUtils.LOGO_RESIZED, null, null);
        String name;

        if (input == null || (name = input.toString()).isEmpty() || name.isBlank() || name.equals("uninitializedValue")) {
            JOptionPane.showMessageDialog(null, "Il nome non può essere vuoto. Chiudendo il programma.",
                    "CoralOverlay", JOptionPane.ERROR_MESSAGE, TextUtils.LOGO_RESIZED);
            System.exit(0);
            return;
        }

        ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setName("overlay-main-executor");
            thread.setDaemon(true);

            return thread;
        });

        CoralOverlay.LOGGER.info("Starting CoralOverlay for " + name);

        executor.submit(() -> CoralOverlay.create(name));

        Runtime.getRuntime().addShutdownHook(new Thread(executor::shutdown));
    }
}
