package it.unibo.oop.lastcrown.view.shop;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JPanel;

import it.unibo.oop.lastcrown.controller.HeroInShopObserver;
import it.unibo.oop.lastcrown.view.dimensioning.DimensionResolver;

/**
 * The main Panel of the shop JFrame.
 */
public final class ShopContent extends JPanel implements KeyListener {
    private static final long serialVersionUID = 1L;
    private static final long INPUT_LAG = 100;
    private static final long INSTRUCTION_TIME = 10_000L;
    private final transient HeroInShopObserver heroObs;
    private final ContainerObserver contObs;
    private final CustomInstructions instructions;
    private final JButton escape;
    private final JButton collection;
    private transient Timer inputTimer;
    private boolean commandExecuted;
    private final transient Timer textTimer;
    private transient TimerTask currentTask;
    private boolean interaction;

    private final Set<Integer> keysPressed = new HashSet<>();

    /**
     * @param heroObs the observer of the hero action in the shop
     * @param contObs the observer of the JFrame of the shop
     * @param width the width of this panel
     * @param height the height of this panel
     */
    public ShopContent(final HeroInShopObserver heroObs, final ContainerObserver contObs,
     final int width, final int height) {
        this.heroObs = heroObs;
        this.contObs = contObs;
        this.textTimer = new Timer();
        this.escape = new JButton("ESCAPE");
        this.escape.setFocusable(false);
        this.escape.setSize((int) (width * DimensionResolver.JBUTTON.width()),
         (int) (height * DimensionResolver.JBUTTON.height()));
        this.escape.addActionListener(act -> this.contObs.notifyEscape());
        this.collection = new JButton("COLLECTION");
        this.collection.setFocusable(false);
        this.collection.setSize((int) (width * DimensionResolver.JBUTTON.width()),
         (int) (height * DimensionResolver.JBUTTON.height()));
        this.collection.addActionListener(act -> this.contObs.notifyCollection());
        this.setSize(width, height);
        this.setLayout(null);
        this.addKeyListener(this);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.instructions = new CustomInstructions((int) (width * DimensionResolver.INSTRUCTIONS.width()),
         (int) (height * DimensionResolver.INSTRUCTIONS.height()));
        this.add(instructions);
        this.instructions.setLocation(width / 10, height / 10);
        this.add(collection);
        this.add(escape);
        this.collection.setLocation(width - collection.getWidth() * 2, 0 + collection.getHeight());
        this.escape.setLocation(width - escape.getWidth() * 2, height - escape.getHeight() * 2);
    }

    @Override
    public void keyTyped(final KeyEvent e) { }

    @Override
    public void keyPressed(final KeyEvent e) {
        if (!this.keysPressed.contains(e.getKeyCode()) && this.inputTimer == null && !interaction) {
            this.keysPressed.add(e.getKeyCode());
            this.inputTimer = new Timer();
            inputTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handleKeyPressed(e.getKeyCode());
                    commandExecuted = true;
                }
            }, INPUT_LAG);
        }
    }

    @Override
    public void keyReleased(final KeyEvent e) {
        if (this.keysPressed.contains(e.getKeyCode())) {
            this.keysPressed.remove(e.getKeyCode());
            if (this.inputTimer != null) {
                this.inputTimer.cancel();
                this.inputTimer = null;
            }

            if (this.commandExecuted) {
                handleKeyReleased(e.getKeyCode());
                this.commandExecuted = false;
            }
        }
    }

    /**
     * Handles the pression of a button of the keyboard.
     * @param keyCode the keyCode of the button pressed
     */
    private void handleKeyPressed(final int keyCode) {
        buttonPressed();
        switch (keyCode) {
            case KeyEvent.VK_SPACE -> this.contObs.notifyExit();
            case KeyEvent.VK_UP -> {
                this.interaction = true;
                this.contObs.notifyInteraction();
            }
            case KeyEvent.VK_LEFT -> this.heroObs.notifyLeft();
            case KeyEvent.VK_RIGHT -> this.heroObs.notifyRight();
            default -> { }
        }
    }

    /**
     * Handles the release of a button of the keyboard.
     * @param keyCode the keyCode of the button released
     */
    private void handleKeyReleased(final int keyCode) {
        buttonReleased();
         switch (keyCode) {
            case KeyEvent.VK_LEFT -> this.heroObs.notifyStopLeft();
            case KeyEvent.VK_RIGHT -> this.heroObs.notifyStopRight();
            default -> { }
        }
    }

    /**
     * The interaction with the trader is ended.
     */
    public void interactionEnded() {
        this.interaction = false;
    }

    /**
     * The player has pressed a button, the instruction must disappear temporarily.
     */
    private void buttonPressed() {
        this.instructions.setVisible(false);
    }

    /**
     * If the player does not press a button
     * for a certain period of time, the instructions appear again.
     */
    private void buttonReleased() {
        if (currentTask != null) {
            currentTask.cancel();
        }
        currentTask = new TimerTask() {
        @Override
        public void run() {
            instructions.setVisible(true);
        }
        };
        this.textTimer.schedule(currentTask, INSTRUCTION_TIME);
    }
}

