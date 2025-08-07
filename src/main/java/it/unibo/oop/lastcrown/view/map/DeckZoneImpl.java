
package it.unibo.oop.lastcrown.view.map;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import it.unibo.oop.lastcrown.controller.app_managing.impl.InGameDeckController;
import it.unibo.oop.lastcrown.controller.collision.api.MatchController;
import it.unibo.oop.lastcrown.controller.user.api.CollectionController;
import it.unibo.oop.lastcrown.controller.user.impl.CollectionControllerImpl;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.view.ImageLoader;
import it.unibo.oop.lastcrown.view.characters.CharacterPathLoader;

/**
 * A JPanel that contains an energyBar and a panel with 4 card-buttons.
 */
public final class DeckZoneImpl extends JPanel implements DeckZone {
    private static final int VERTICAL_GAP_BTNS = 5;
    private static final long serialVersionUID = 1L;
    private static final int SECTIONS = 10;
    private static final Color BG = new Color(27, 120, 175);
    private static final String KEY_PROPERTY = "info";
    private static final Font LABEL_FONT = new Font("Monospaced", Font.BOLD, 16);

    private static final int MAX_ENERGY = SECTIONS;
    private static final int TIME_RECHARGE_SINGLE_ENERGY = 1000;
    private int currentEnergy;
    private final Timer rechargeTimer;
    private transient InGameDeckController inGameDeckController;
    private transient ActionListener buttonListener;
    private transient MouseListener mouseListener;
    private final transient ActionListener buttonListenerDefensiveCopy;
    private final transient MouseListener mouseListenerDefensiveCopy;

    private JPanel energyBarPanel;
    private final JPanel btnsPanel;
    private transient Optional<CardIdentifier> lastClicked = Optional.empty();
    private final int deckZoneWidth;
    private final int deckZoneHeight;
    private final int energyZoneWidth;
    private boolean bossFight;

    /**
     * @param mainContr main controller
     * @param pos positioning zone
     * @param deckZoneWidth deck zone width
     * @param deckZoneHeight deck zone height
     * @param energyBarWidth energy bar width
     * @param deck the deck
     */
    public DeckZoneImpl(final MatchController mainContr, final PositioningZone pos,
     final int deckZoneWidth, final int deckZoneHeight, final int energyBarWidth, final Set<CardIdentifier> deck) {
        this.deckZoneWidth = deckZoneWidth;
        this.deckZoneHeight = deckZoneHeight;
        this.energyZoneWidth = energyBarWidth;
        this.inGameDeckController = InGameDeckController.create(deck);
        this.setLayout(null);
        this.setupEnergyBar(this, energyBarWidth);
        this.setBackground(BG);
        this.setPreferredSize(new Dimension(this.deckZoneWidth, deckZoneHeight));
        this.currentEnergy = MAX_ENERGY;

        this.rechargeTimer = new Timer(TIME_RECHARGE_SINGLE_ENERGY, e -> {
            if (currentEnergy < MAX_ENERGY) {
                currentEnergy++;
                updateEnergyBar(currentEnergy);
            } else {
                ((Timer) e.getSource()).stop();
            }
        });
        this.rechargeTimer.start();

        this.buttonListener = e -> {
            final var button = (JButton) e.getSource();
            final CardIdentifier id = (CardIdentifier) button.getClientProperty(KEY_PROPERTY);
            lastClicked = Optional.of(id);
            mainContr.notifyButtonPressed(id);
        };
        this.buttonListenerDefensiveCopy = buttonListener;
        this.mouseListener = new MouseAdapter() {
            @Override
            public void mouseEntered(final MouseEvent e) {
                if (!bossFight) {
                    final var jb = (JButton) e.getSource();
                    final CardIdentifier id = (CardIdentifier) jb.getClientProperty(KEY_PROPERTY);
                    pos.highlightCells(id.type());
                }
            }
            @Override
            public void mouseExited(final MouseEvent e) {
                final var jb = (JButton) e.getSource();
                final CardIdentifier id = (CardIdentifier) jb.getClientProperty(KEY_PROPERTY);
                pos.stopHighLight(id.type());
            }
        };
        this.mouseListenerDefensiveCopy = mouseListener;
        this.btnsPanel = new JPanel(new GridLayout(3, 1, 0, VERTICAL_GAP_BTNS));
        this.btnsPanel.setPreferredSize(new Dimension(deckZoneWidth - energyBarWidth, deckZoneHeight));
        this.btnsPanel.setBounds(energyBarWidth, 0, deckZoneWidth - energyBarWidth, deckZoneHeight);
        this.add(btnsPanel);

        updateCardButtons(buttonListener, mouseListener);
    }

    @Override
    public Optional<CardIdentifier> getLastClicked() {
        return this.lastClicked;
    }

    @Override
    public boolean playCard() {
        if (this.lastClicked.isPresent()) {
            final int cost = inGameDeckController.getEnergyToPlay(this.lastClicked.get());
            if (currentEnergy >= cost) {
                currentEnergy -= cost;
                updateEnergyBar(currentEnergy);
                if (!rechargeTimer.isRunning()) {
                    rechargeTimer.start();
                }
                inGameDeckController.playCard(this.lastClicked.get());
                updateCardButtons(this.buttonListener, this.mouseListener);
                this.lastClicked = Optional.empty();
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateInGameDeck(final Set<CardIdentifier> newDeck) {
        this.inGameDeckController = InGameDeckController.create(newDeck);
        this.updateCardButtons(buttonListener, mouseListener);
    }

    @Override
    public void handleButtonsEnabling(final boolean start) {
        this.bossFight = start;
        enablePanelAndChildren(btnsPanel, !start);
    }

    @Override
    public void setTimerStopping(final boolean stop) {
        if (stop && this.rechargeTimer.isRunning()) {
            this.rechargeTimer.stop();
        } else if (!stop && !this.rechargeTimer.isRunning()) {
            this.rechargeTimer.start();
        }
    }

    private void updateCardButtons(final ActionListener act, final MouseListener ml) {
        this.btnsPanel.removeAll();
        final List<CardIdentifier> nextCards = inGameDeckController.getNextAvailableCards();
        for (final CardIdentifier id : nextCards) {
            final JPanel singleCardPanel = initSingleCardPanel(act, ml, id);
            this.btnsPanel.add(singleCardPanel);
        }
        if (this.bossFight) {
            enablePanelAndChildren(btnsPanel, !bossFight);
        }
    }

    private JPanel initSingleCardPanel(final ActionListener act, final MouseListener ml, final CardIdentifier id) {
        final JPanel singleCardPanel = new JPanel(new BorderLayout());
        singleCardPanel.setPreferredSize(new Dimension(
            this.deckZoneWidth - this.energyZoneWidth,
            this.deckZoneHeight - 2 * VERTICAL_GAP_BTNS
        ));
        final JButton jb = initButton(act, ml, id);
        switch (id.type()) {
            case MELEE:
                jb.setBackground(Color.ORANGE);
                singleCardPanel.setBackground(Color.ORANGE);
                break;
            case RANGED:
                jb.setBackground(Color.GREEN);
                singleCardPanel.setBackground(Color.GREEN);
                break;
            default:
                jb.setBackground(Color.CYAN);
                singleCardPanel.setBackground(Color.CYAN);
                break;
        }
        singleCardPanel.add(jb, BorderLayout.CENTER);
        final String energyText = "Energy Cost: " + this.inGameDeckController.getEnergyToPlay(id);
        final JLabel energyLabel = new JLabel(energyText, SwingConstants.CENTER);
        energyLabel.setFont(LABEL_FONT);
        singleCardPanel.add(energyLabel, BorderLayout.SOUTH);
        return singleCardPanel;
    }

    private JButton initButton(final ActionListener act, final MouseListener ml, final CardIdentifier id) {
        final JButton jb = new JButton();
        jb.putClientProperty(KEY_PROPERTY, id);
        jb.addActionListener(act);
        jb.addMouseListener(ml);
        jb.setBorder(BorderFactory.createEmptyBorder());
        jb.setFocusPainted(false);
        jb.setMargin(new Insets(0, 0, 0, 0));
        addIconToBtn(id, jb);
        return jb;
    }

    private void addIconToBtn(final CardIdentifier id, final JButton jb) {
        final int jbWidth = this.deckZoneWidth - this.energyZoneWidth;
        final CollectionController collContr = new CollectionControllerImpl();
        final String name = collContr.getCardName(id)
            .orElseThrow(() -> new IllegalArgumentException(
                "No name found for card " + id
            ));
        final String iconPath = CharacterPathLoader.loadIconPath(id.type().get(), name);
        final BufferedImage img = ImageLoader.getImage(iconPath, jbWidth, jbWidth);
        final ImageIcon icon = new ImageIcon(img);
        jb.setIcon(icon);
    }

    private void setupEnergyBar(final JPanel container, final int width) {
        energyBarPanel = new JPanel();
        energyBarPanel.setBackground(Color.BLACK);
        energyBarPanel.setPreferredSize(new Dimension(width, deckZoneHeight));
        final int sections = SECTIONS;
        energyBarPanel.setLayout(new GridLayout(sections, 1, 0, 2));
        energyBarPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        updateEnergyBar(SECTIONS);
        energyBarPanel.setBounds(0, 0, width, deckZoneHeight);
        container.add(energyBarPanel);
    }

    private void updateEnergyBar(final int energyLevel) {
        energyBarPanel.removeAll();
        final Color full = new Color(255, 105, 180);
        final Color empty = Color.WHITE;
        final Color separator = Color.DARK_GRAY;
        for (int i = 0; i < SECTIONS; i++) {
            final JPanel section = new JPanel(new BorderLayout());
            section.setBackground(i < energyLevel ? full : empty);
            section.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, separator));
            energyBarPanel.add(section);
        }
        energyBarPanel.revalidate();
        energyBarPanel.repaint();
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.buttonListener = this.buttonListenerDefensiveCopy;
        this.mouseListener = this.mouseListenerDefensiveCopy;
    }

    private void enablePanelAndChildren(final JPanel panel, final boolean start) {
            for (final Component comp : panel.getComponents()) {
                comp.setEnabled(start);
                if (comp instanceof Container && !(comp instanceof JButton || comp instanceof JLabel)) {
                    enablePanelAndChildren((JPanel) comp, start);
            }
        }
    }
}
