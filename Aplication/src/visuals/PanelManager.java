package visuals;


import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase que gestiona los diferentes paneles de la aplicación
 * Permite cambiar entre paneles sin necesidad de crear nuevas ventanas
 */
public class PanelManager {

    private JFrame mainFrame;
    private Map<String, JPanel> panels;
    private String currentPanel;

    /**
     * Constructor del gestor de paneles
     * @param mainFrame JFrame principal donde se mostrarán los paneles
     */
    public PanelManager(JFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.panels = new HashMap<>();
    }

    /**
     * Registra un panel en el gestor
     * @param name Nombre clave para identificar el panel
     * @param panel Panel a registrar
     */
    public void registerPanel(String name, JPanel panel) {
        panels.put(name, panel);
    }

    /**
     * Muestra un panel específico y oculta el resto
     * @param name Nombre del panel a mostrar
     */
    public void showPanel(String name) {
        if (!panels.containsKey(name)) {
            System.err.println("Panel no encontrado: " + name);
            return;
        }

        // Eliminar el panel actual si existe
        if (currentPanel != null) {
            mainFrame.getContentPane().remove(panels.get(currentPanel));
        }

        // Añadir el nuevo panel
        JPanel targetPanel = panels.get(name);
        mainFrame.getContentPane().add(targetPanel);
        currentPanel = name;

        // Actualizar la interfaz
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    /**
     * Devuelve el JFrame principal
     * @return JFrame principal
     */
    public JFrame getMainFrame() {
        return mainFrame;
    }
}
