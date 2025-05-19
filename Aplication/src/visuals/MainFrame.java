package visuals;
import interfaces.IPanelSwitcher;

import javax.swing.*;

public class MainFrame extends JFrame implements IPanelSwitcher {
    // Panel actual
    private JPanel currentPanel;
    private IPanelSwitcher IPanelSwitcher = new IPanelSwitcher() {;
        @Override
        public void closePanel(JPanel panel) {
            MainFrame.this.closePanel(panel);
        }

        @Override
        public void openPanel(JPanel panel) {
            MainFrame.this.openPanel(panel);
        }
    };

    /**
     * Constructor de la ventana principal
     */
    public MainFrame() {
        // Configurar ventana
        setTitle("BuyMe Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        // Iniciar con panel de bienvenida
        WelcomeIPanel welcomePanel = new WelcomeIPanel(IPanelSwitcher);
        openPanel(welcomePanel);

        // Mostrar ventana
        setVisible(true);

    }

    @Override
    public void closePanel(JPanel panel) {
        if (panel != null) {
            getContentPane().remove(panel);
            currentPanel = null;
            revalidate();
            repaint();
        }
    }

    @Override
    public void openPanel(JPanel panel) {
        // Si hay un panel actual, cerrarlo primero
        if (currentPanel != null) {
            closePanel(currentPanel);
        }

        // Añadir el nuevo panel
        getContentPane().add(panel);
        currentPanel = panel;
        revalidate();
        repaint();
    }

    /**
     * Método principal para iniciar la aplicación
     */
    public static void main(String[] args) {
        // Crear y mostrar la aplicación en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            try {
                // Usar look and feel del sistema
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MainFrame();
        });
    }
}