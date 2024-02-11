package tech.zmario.coraloverlay;

import tech.zmario.coraloverlay.utils.TextUtils;
import tech.zmario.coraloverlay.windows.NameInputPanel;

import javax.swing.*;

public class Application {

    public static void main(String[] args) {
        String name = NameInputPanel.create().getName();

        if (name.isEmpty() || name.isBlank()) {
            JOptionPane.showMessageDialog(null, "Il nome non può essere vuoto.",
                    "CoralOverlay", JOptionPane.ERROR_MESSAGE, TextUtils.LOGO_RESIZED);
            System.exit(0);
            return;
        }

        CoralOverlay.create(name);
    }
}
