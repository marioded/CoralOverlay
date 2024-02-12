package tech.zmario.coraloverlay;

import tech.zmario.coraloverlay.utils.TextUtils;

import javax.swing.*;

public class Application {

    public static void main(String[] args) {
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

        System.out.println("Logged in as " + name + ".");
        CoralOverlay.create(name);
    }
}
