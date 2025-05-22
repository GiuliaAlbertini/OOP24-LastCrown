package it.unibo.oop.lastcrown.view.impl;

import it.unibo.oop.lastcrown.controller.api.MainController;
import it.unibo.oop.lastcrown.controller.impl.GameState;
import it.unibo.oop.lastcrown.view.GamePanel;
import it.unibo.oop.lastcrown.view.MainView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainViewImpl extends JFrame implements MainView {
    
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private GamePanel gamePanel;  // la classe wrapper
    private final MainController controller;
    private final JButton addCharacterBtn = new JButton("Aggiungi personaggio");  

    public MainViewImpl(MainController controller) {
        this.controller = controller;
        this.cardLayout = new CardLayout();
        this.mainPanel = new JPanel(cardLayout); // IMPORTANTE: Assegna il CardLayout al mainPanel

        // Impostazioni di base della finestra
        setTitle("Last Crown");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Aggiungi il pannello principale
        add(mainPanel);
        initPanel();
        setVisible(true);
    }

    @Override
    public void initPanel() {
        // Menu Panel
        JPanel menuPanel = new JPanel();
        menuPanel.setName(GameState.MENU.toString());
        menuPanel.add(new JLabel("Menu del gioco"));


        this.gamePanel= new GamePanelImpl();
        addCharacterBtn.setBounds(10, 50, 200, 30);
        gamePanel.getPanel().add(addCharacterBtn);

        // Pause Panel
        JPanel pausePanel = new JPanel();
        pausePanel.setName(GameState.MENU_IN_GAME.toString());
        pausePanel.add(new JLabel("Pausa"));

        // Aggiungi i pannelli al mainPanel
        mainPanel.add(menuPanel, GameState.MENU.toString());
        mainPanel.add(gamePanel.getPanel(), GameState.GAME.toString());
        mainPanel.add(pausePanel, GameState.MENU_IN_GAME.toString());
    }

    @Override
    public void showPanel(GameState panel) {
        cardLayout.show(mainPanel, panel.toString());
    }

    @Override
    public JPanel getPanel(GameState panel) {
        Component[] components = mainPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel && comp.getName() != null && 
                comp.getName().equals(panel.toString())) {
                return (JPanel) comp;
            }
        }
        return null;
    }


    @Override
    public void setAddCharacterListener(ActionListener listener) {
        addCharacterBtn.addActionListener(listener);
    }

    @Override
        public GamePanel getGamePanel() {
            return this.gamePanel;
        }

}