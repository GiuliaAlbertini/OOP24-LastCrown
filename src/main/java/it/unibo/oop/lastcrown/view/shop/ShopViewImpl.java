package it.unibo.oop.lastcrown.view.shop;

import java.awt.Dimension;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import it.unibo.oop.lastcrown.controller.app_managing.impl.InGameAccountManager;
import it.unibo.oop.lastcrown.controller.menu.api.SceneManager;
import it.unibo.oop.lastcrown.controller.shop.impl.ShopCardsSelectionControllerImpl;
import it.unibo.oop.lastcrown.controller.user.api.CollectionController;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.user.api.Account;
import it.unibo.oop.lastcrown.view.Dialog;
import it.unibo.oop.lastcrown.view.SceneName;
import it.unibo.oop.lastcrown.view.dimensioning.DimensionResolver;

/**
 * The main Window of the shop.
 */
public final class ShopViewImpl extends JPanel implements ShopView, ContainerObserver {
    private static final int COINS_TO_ESCAPE = 100;
    private static final long serialVersionUID = 1L;
    private static final int ESCAPE_TAX = 100;
    private static final String TRADER1 = "trader1";
    private static final String TRADER2 = "trader2"; 
    private static final String TRADER3 = "trader3";
    private final int width;
    private final int height;
    private final int panelSize;
    private final transient CollectionController collContr;
    private final transient List<CardIdentifier> userCollDefensiveCopy;
    private transient List<CardIdentifier> userColl;
    private final ShopContent shopContent;
    private final List<TraderPanel> traders;
    private final transient InGameAccountManager accManager;
    private transient Optional<CardSelectionView> selection = Optional.empty();
    private final transient SceneManager sceneManager;

    /**
     * @param sceneManager the scene manager
     * @param collContr the collection controller
     * @param userColl the observer of the hero action in the shop
     * @param width the width of this JFrame
     * @param height the height of this JFrame
     * @param account the account used to create the InGameAccountManager
     */
    public ShopViewImpl(final SceneManager sceneManager, final CollectionController collContr,
     final List<CardIdentifier> userColl, final int width, final int height, final Account account) {
        this.collContr = collContr;
        this.sceneManager = sceneManager;
        this.accManager = InGameAccountManager.create(account);
        this.userCollDefensiveCopy = Collections.unmodifiableList(new ArrayList<>(userColl));
        this.userColl = Collections.unmodifiableList(new ArrayList<>(userColl));
        this.traders = new ArrayList<>();
        this.width = width;
        this.height = height;
        this.panelSize = (int) (width * DimensionResolver.TRADER.width());
        this.shopContent = new ShopContent(this, width, height);
        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(null);
        final var panel1 = TraderPanel.create(0, this, TRADER1, CardType.HERO, panelSize, panelSize);
        this.traders.addLast(panel1);
        final var panel2 = TraderPanel.create(1, this, TRADER2, CardType.FRIENDLY, panelSize, panelSize);
        this.traders.addLast(panel2);
        final var panel3 = TraderPanel.create(2, this, TRADER3, CardType.SPELL, panelSize, panelSize);
        this.traders.addLast(panel3);
        this.addTraderPanel(panel1, shopContent.getWidth() / 4, shopContent.getHeight() / 2);
        this.addTraderPanel(panel2, shopContent.getWidth() / 2, shopContent.getHeight() / 2);
        this.addTraderPanel(panel3, shopContent.getWidth() / 4 * 3, shopContent.getHeight() / 2);
        this.add(shopContent);
        this.setOpaque(true);
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.userColl = this.userCollDefensiveCopy;
    }

    private void addTraderPanel(final JComponent component, final int x, final int y) {
        this.shopContent.add(component);
        this.shopContent.setComponentZOrder(component, 0);
        component.setLocation(x - this.panelSize / 2, y - this.panelSize / 2);
        component.repaint();
    }

    @Override
    public void notifyVisible() {
        for (final var trader : this.traders) {
            trader.startStopLoop();
        }
    }

    @Override
    public void notifyHidden() {
        for (final var trader : this.traders) {
            trader.stopAnimations();
        }
    }

    @Override
    public void notifyEscape() {
    final String title = "Quitting...";
    final String message = "Do you really want to escape? You will mantain all the acquirements"
     + " obtained after the last match but you must pay " + ESCAPE_TAX + " coins";
    final Dialog dialog = new Dialog(title, message, true);
    final JButton escape = new JButton("ESCAPE");
    escape.addActionListener(act -> {
        if (this.accManager.canEscape(COINS_TO_ESCAPE)) {
            this.accManager.removeCoins(COINS_TO_ESCAPE);
            dialog.dispose();
            this.sceneManager.switchScene(SceneName.SHOP, SceneName.MENU);
        } else {
            dialog.dispose();
            final String titleEsc = "(MUAHAHAH)";
            final String messageEsc = "THERE IS NO ESCAPE...\n\n(You need more money)";
            final Dialog dialogEsc = new Dialog(titleEsc, messageEsc, true);
            dialogEsc.setLocationRelativeTo(this);
            dialogEsc.setVisible(true);
        }
    });
    dialog.addButton(escape);
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
    }

    @Override
    public void notifyCollection() {
        this.sceneManager.switchScene(SceneName.SHOP, SceneName.COLLECTION);
    }

    @Override
    public void notifyDeck() {
        this.sceneManager.switchScene(SceneName.SHOP, SceneName.DECK);
    }

    @Override
    public void notifyUpdateAccount(final int amount, final boolean bossDefeated) {
        this.accManager.updateAccount(amount, bossDefeated);
    }

    @Override
    public void notifyMatch() {
        final String title = "To the Match...";
        final String message = "Do you really want to start a new match? You will not be able"
        + " to return to the shop until you defeat a Boss ";
        final Dialog dialog = new Dialog(title, message, true);
        final JButton match = new JButton("MATCH");
        match.addActionListener(act -> {
            dialog.dispose();
            this.sceneManager.switchScene(SceneName.SHOP, SceneName.MATCH);
        });
        dialog.addButton(match);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    @Override
    public void notifyInteraction(final int id, final CardType cardType) {
        final ShopCardsSelectionControllerImpl ctrl = new ShopCardsSelectionControllerImpl(collContr, userColl);
        final List<CardIdentifier> cards = ctrl.getRandomCardsByType(cardType);
        if (cards.isEmpty()) {
            final String title = "No cards available";
            final String message = "There are no more " + cardType + " cards to purchase.";
            final Dialog dialog = new Dialog(title, message, true);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
            this.traders.get(id).startCloseSequence();
            return;
        }
        final int currentCoins = this.accManager.getAccount().getCoins();
        this.selection = Optional.of(new CardSelectionView(this.width, this.height,
        id, cardType, ctrl, this, currentCoins));
        selection.get().setLocation(this.getLocation());
        selection.get().setVisible(true);
    }

    @Override
    public void notifyEndInteraction(final Optional<CardIdentifier> cardIdentifier, final int id) {
        if (cardIdentifier.isPresent()) {
            final CardIdentifier ci = cardIdentifier.get();
            if (accManager.isBuyable(ci)) {
                this.accManager.addCard(ci);
                this.userColl = new ArrayList<>(this.accManager.getUserCollection());
                this.sceneManager.updateUserCollectionUsers(this.accManager.getUserCollection());
                this.selection.get().dispose();
                this.traders.get(id).startApprovalSequence();
            } else {
                this.traders.get(id).startCloseSequence();
                final String title = "OPS...";
                final String message = "You don't have enough coins to buy this card";
                final Dialog dialog = new Dialog(title, message, true);
                dialog.setLocationRelativeTo(this.selection.get());
                dialog.setVisible(true);
            }
        } else {
            this.traders.get(id).startCloseSequence();
        }
    }

    @Override
    public SceneName getSceneName() {
        return SceneName.SHOP;
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public Account getManagedAccount() {
       return this.accManager.getAccount();
    }
}
