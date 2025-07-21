package it.unibo.oop.lastcrown.view.collision.impl;

import it.unibo.oop.lastcrown.controller.collision.api.MainController;
import it.unibo.oop.lastcrown.controller.collision.impl.GameState;
import it.unibo.oop.lastcrown.view.collision.api.GamePanel;
import it.unibo.oop.lastcrown.view.collision.api.MainView;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionListener;

/**
 * Main implementation of the MainView interface using Swing.
 * This class manages the main application window with different panels
 * such as menu, game, and pause, using a CardLayout to switch between them.
 */
public final class MainViewImpl extends JFrame implements MainView {
    private static final long serialVersionUID = 1L;

    // Constants for window size
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    // Constants for button position and size
    private static final int BUTTON_X = 10;
    private static final int BUTTON_Y = 50;
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 30;

    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private GamePanel gamePanel; // la classe wrapper
    private final JButton addCharacterBtn = new JButton("Aggiungi personaggio");

    /**
     * Constructs the main application window, initializes the layout and panels,
     * sets basic window properties such as title, size, and close operation.
     *
     * @param controller the main controller to link with the view (currently not used directly here)
     */
    public MainViewImpl(final MainController controller) {
        this.cardLayout = new CardLayout();
        this.mainPanel = new JPanel(cardLayout); // IMPORTANTE: Assegna il CardLayout al mainPanel

        // Impostazioni di base della finestra
        setTitle("Last Crown");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Aggiungi il pannello principale
        add(mainPanel);
        initPanel();
        setVisible(true);
    }

    @Override
    public void initPanel() {
        // Menu Panel
        final JPanel menuPanel = new JPanel();
        menuPanel.setName(GameState.MENU.toString());
        menuPanel.add(new JLabel("Menu del gioco"));

        this.gamePanel = new GamePanelImpl();
        //addCharacterBtn.setBounds(BUTTON_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        //gamePanel.getPanel().add(addCharacterBtn);

        // Pause Panel
        final JPanel pausePanel = new JPanel();
        pausePanel.setName(GameState.MENU_IN_GAME.toString());
        pausePanel.add(new JLabel("Pausa"));

        // Aggiungi i pannelli al mainPanel
        mainPanel.add(menuPanel, GameState.MENU.toString());
        mainPanel.add(gamePanel.getPanel(), GameState.GAME.toString());
        mainPanel.add(pausePanel, GameState.MENU_IN_GAME.toString());
    }

    @Override
    public void showPanel(final GameState panel) {
        cardLayout.show(mainPanel, panel.toString());
    }

    @Override
    public JPanel getPanel(final GameState panel) {
        final Component[] components = mainPanel.getComponents();
        for (final Component comp : components) {
            if (comp instanceof JPanel && comp.getName() != null
                    && comp.getName().equals(panel.toString())) {
                return (JPanel) comp;
            }
        }
        return null;
    }

    @Override
    public void setAddCharacterListener(final ActionListener listener) {
        addCharacterBtn.addActionListener(listener);
    }

    @Override
    public GamePanel getGamePanel() {
        return this.gamePanel;
    }
}
