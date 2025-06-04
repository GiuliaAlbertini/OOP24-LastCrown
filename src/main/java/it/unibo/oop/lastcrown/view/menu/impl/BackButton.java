package it.unibo.oop.lastcrown.view.menu.impl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import it.unibo.oop.lastcrown.controller.menu.api.SceneManager;

/**
 * A non-extendable button that navigates back to a specified view.
 */
public final class BackButton extends JButton {
    private static final long serialVersionUID = 1L;

    private static final int BASE_SCREEN_WIDTH = 1920;
    private static final Dimension SCREENSIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int SCREEN_WIDTH = (int) SCREENSIZE.getWidth();
    private static final int SCREEN_HEIGHT = (int) SCREENSIZE.getHeight();

    private static final Color BTN_FG_COLOR = new Color(15, 35, 65);
    private static final Color BTN_BG_COLOR = new Color(255, 215, 0);
    private static final Font BTN_FONT = getResponsiveFont(new Font("DialogInput", Font.BOLD, 20));

    private static final int BTN_HEIGHT = (int) (SCREEN_HEIGHT * 0.05);
    private static final int BTN_WIDTH  = (int) (SCREEN_WIDTH  * 0.1);

    private final String backViewName;
    private final transient SceneManager sceneManager;

    /**
     * Contructs the button and sets the view and the scene manager to use.
     *
     * @param backView the target view identifier
     * @param sceneManager the scene manager to use
     */
    private BackButton(final String backView, final SceneManager sceneManager) {
        super("BACK");
        this.backViewName = backView;
        this.sceneManager = sceneManager;
    }

    /**
     * Static factory method to obtain a fully initialized BackButton.
     *
     * @param backView the target view identifier
     * @param sceneManager the scene manager to use
     * @return the created BackButton
     */
    public static BackButton create(final String backView, final SceneManager sceneManager) {
        final BackButton button = new BackButton(backView, sceneManager);
        button.init(new Dimension(BTN_WIDTH, BTN_HEIGHT));
        return button;
    }

    private void init(final Dimension size) {
        setPreferredSize(size);
        setBackground(BTN_BG_COLOR);
        setFont(BTN_FONT);
        setForeground(BTN_FG_COLOR);
        setBorder(BorderFactory.createBevelBorder(0));
        addActionListener(backButtonActionListener());
    }

    private ActionListener backButtonActionListener() {
        return e -> sceneManager.switchScene(backViewName);
    }

    private static Font getResponsiveFont(final Font baseFont) {
        final double scaleFactor = (double) SCREEN_WIDTH / BASE_SCREEN_WIDTH;
        final int newSize = (int) (baseFont.getSize() * scaleFactor);
        return new Font(baseFont.getName(), baseFont.getStyle(), newSize);
    }
}
