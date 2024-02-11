package tech.zmario.coraloverlay.windows;

import tech.zmario.coraloverlay.utils.TextUtils;

import javax.swing.*;
import java.awt.*;

public class NameInputPanel extends JOptionPane {

    private NameInputPanel() {
        setName("Inserisci il tuo nome");
        setIcon(TextUtils.LOGO_RESIZED);
        setMessage("Inserisci il tuo nome");
        setWantsInput(true);

        setBackground(new Color(0, 0, 0, 0));

        JDialog dialog = createDialog("CoralOverlay");

        dialog.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 150, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 100);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        dialog.setIconImage(TextUtils.LOGO_RESIZED.getImage());

        selectInitialValue();
        setVisible(true);

        dialog.show();
        dialog.dispose();
    }

    public String getName() {
        return (String) getInputValue();
    }

    public static NameInputPanel create() {
        return new NameInputPanel();
    }
}
