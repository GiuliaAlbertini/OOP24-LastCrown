package it.unibo.oop.lastcrown.view.impl;

import it.unibo.oop.lastcrown.controller.api.MainController;
import it.unibo.oop.lastcrown.controller.impl.GameState;
import it.unibo.oop.lastcrown.view.MainView;
import it.unibo.oop.lastcrown.view.characters.api.GenericCharacterGUI;

import javax.swing.*;
import java.awt.*;

public class MainViewImpl extends JFrame implements MainView {
    
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final MainController controller;
    GenericCharacterGUI character;
    
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

        // Game Panel - IMPORTANTE: usa layout null per posizionamento assoluto
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(null);
        gamePanel.setName(GameState.GAME.toString());
        gamePanel.setBackground(Color.LIGHT_GRAY); // Colore di debug
        gamePanel.setPreferredSize(new Dimension(800, 600));
        

        JButton addCharacterBtn = new JButton("Aggiungi personaggio");
        addCharacterBtn.setBounds(10, 50, 200, 30);
        addCharacterBtn.addActionListener(e -> controller.getMatchController().onAddCharacterButtonPressed());
        gamePanel.add(addCharacterBtn);



        // Pause Panel
        JPanel pausePanel = new JPanel();
        pausePanel.setName(GameState.MENU_IN_GAME.toString());
        pausePanel.add(new JLabel("Pausa"));

        // Aggiungi i pannelli al mainPanel
        mainPanel.add(menuPanel, GameState.MENU.toString());
        mainPanel.add(gamePanel, GameState.GAME.toString());
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
}