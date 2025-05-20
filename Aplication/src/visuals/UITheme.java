package visuals;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Clase utilitaria para mantener un tema consistente en toda la aplicación
 * Colores: Rojo y blanco (estilo minimalista)
 */
public class UITheme {
    // Colores principales
    public static final Color PRIMARY_RED = new Color(227, 30, 36);      // Rojo principal
    public static final Color SECONDARY_RED = new Color(190, 30, 45);    // Rojo secundario (más oscuro)
    public static final Color LIGHT_RED = new Color(255, 200, 200);      // Rojo claro
    public static final Color WHITE = Color.WHITE;                       // Blanco
    public static final Color LIGHT_GRAY = new Color(240, 240, 240);     // Gris claro
    public static final Color TEXT_DARK = new Color(50, 50, 50);         // Texto oscuro

    // Fuentes
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    // Bordes
    public static Border RED_LINE_BORDER = BorderFactory.createLineBorder(PRIMARY_RED, 2);
    public static Border EMPTY_BORDER = BorderFactory.createEmptyBorder(20, 20, 20, 20);
    public static Border RED_TITLED_BORDER(String title) {
        return BorderFactory.createTitledBorder(
                RED_LINE_BORDER, title, TitledBorder.LEFT, TitledBorder.TOP, SUBTITLE_FONT, PRIMARY_RED);
    }

    /**
     * Aplica el estilo a un botón principal (acción principal)
     * @param button El botón a estilizar
     */
    public static void applyPrimaryButtonStyle(JButton button) {
        button.setBackground(PRIMARY_RED);
        button.setForeground(WHITE);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
    }

    /**
     * Aplica el estilo a un botón secundario (acción secundaria)
     * @param button El botón a estilizar
     */
    public static void applySecondaryButtonStyle(JButton button) {
        button.setBackground(WHITE);
        button.setForeground(PRIMARY_RED);
        button.setFont(BUTTON_FONT);
        button.setBorder(BorderFactory.createLineBorder(PRIMARY_RED, 1));
        button.setFocusPainted(false);
        button.setOpaque(true);
    }

    /**
     * Aplica el estilo a un panel de contenido
     * @param panel El panel a estilizar
     */
    public static void applyPanelStyle(JPanel panel) {
        panel.setBackground(WHITE);
        panel.setBorder(EMPTY_BORDER);
    }

    /**
     * Aplica el estilo a un campo de texto
     * @param textField El campo de texto a estilizar
     */
    public static void applyTextFieldStyle(JTextField textField) {
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 7, 5, 7)));
        textField.setFont(REGULAR_FONT);
    }

    /**
     * Aplica el estilo a un campo de contraseña
     * @param passwordField El campo de contraseña a estilizar
     */
    public static void applyPasswordFieldStyle(JPasswordField passwordField) {
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 7, 5, 7)));
        passwordField.setFont(REGULAR_FONT);
    }

    /**
     * Aplica el estilo a una etiqueta de título
     * @param label La etiqueta a estilizar
     */
    public static void applyTitleLabelStyle(JLabel label) {
        label.setFont(TITLE_FONT);
        label.setForeground(PRIMARY_RED);
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }

    /**
     * Aplica el estilo a una etiqueta de subtítulo
     * @param label La etiqueta a estilizar
     */
    public static void applySubtitleLabelStyle(JLabel label) {
        label.setFont(SUBTITLE_FONT);
        label.setForeground(TEXT_DARK);
    }

    /**
     * Aplica el estilo a una etiqueta de formulario
     * @param label La etiqueta a estilizar
     */
    public static void applyFormLabelStyle(JLabel label) {
        label.setFont(REGULAR_FONT);
        label.setForeground(TEXT_DARK);
    }

    /**
     * Crea un panel de título con estilo consistente
     * @param title El texto del título
     * @return Un panel conteniendo el título estilizado
     */
    public static JPanel createTitlePanel(String title) {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(WHITE);

        JLabel titleLabel = new JLabel(title);
        applyTitleLabelStyle(titleLabel);

        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        return titlePanel;
    }

    /**
     * Configura una lista con el estilo de la aplicación
     * @param list La lista a estilizar
     */
    public static void applyListStyle(JList<?> list) {
        list.setFont(REGULAR_FONT);
        list.setSelectionBackground(LIGHT_RED);
        list.setSelectionForeground(TEXT_DARK);
        list.setBackground(WHITE);
    }
}