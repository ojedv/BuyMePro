package interfaces;

import javax.swing.*;

public interface IPanelSwitcher {
    /**
     * Cierra el panel actual
     * @param panel El panel que se desea cerrar
     */
    void closePanel(JPanel panel);

    /**
     * Abre un nuevo panel en el JFrame
     * @param panel El panel que se desea abrir
     */
    void openPanel(JPanel panel);
}