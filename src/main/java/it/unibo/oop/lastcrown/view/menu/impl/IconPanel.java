package it.unibo.oop.lastcrown.view.menu.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import it.unibo.oop.lastcrown.controller.user.api.CollectionController;
import it.unibo.oop.lastcrown.controller.user.impl.CollectionControllerImpl;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.view.ImageLoader;
import it.unibo.oop.lastcrown.view.characters.CharacterPathLoader;

/**
 * A JPanel that displays only the card’s icon (normal or grey), scaled to fit its bounds.
 */
public final class IconPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int WIDTH_FALLBACK = 200;
    private static final int HEIGHT_FALLBACK = 300;

    private final transient BufferedImage originalIcon;
    private final Border defaultBorder = BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(3, 3, 3, 3, new Color(245, 245, 245)),
        BorderFactory.createCompoundBorder(
            BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.GRAY, Color.DARK_GRAY),
            BorderFactory.createLineBorder(new Color(30, 144, 255), 2)
        )
    );
    private final Border hoverBorder = BorderFactory.createLineBorder(new Color(255, 215, 0), 3, true);

    public IconPanel(final CardIdentifier card, final boolean useGrey, final boolean hoverEffect) {
        super(new BorderLayout());
        setBackground(new Color(40, 40, 40));
        setBorder(defaultBorder);

        final CollectionController collContr = new CollectionControllerImpl();
        final String name = collContr.getCardName(card)
            .orElseThrow(() -> new IllegalArgumentException("No name found for card " + card));

        final String iconPath = useGrey
            ? CharacterPathLoader.loadGreyIconPath(card.type().get(), name)
            : CharacterPathLoader.loadIconPath(card.type().get(), name);

        BufferedImage img;
        try {
            img = ImageIO.read(new File(iconPath));
        } catch (final IOException e) {
            img = new BufferedImage(WIDTH_FALLBACK, HEIGHT_FALLBACK, BufferedImage.TYPE_INT_ARGB);
        }
        this.originalIcon = img;

        if (hoverEffect) {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(final MouseEvent e) {
                    setBorder(hoverBorder);
                    repaint();
                }

                @Override
                public void mouseExited(final MouseEvent e) {
                    setBorder(defaultBorder);
                    repaint();
                }
            });
        }
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final int availableWidth = getWidth();
        final int availableHeight = getHeight();
        if (availableWidth <= 0 || availableHeight <= 0) {
            return;
        }
        final double imgWidth = originalIcon.getWidth();
        final double imgHeight = originalIcon.getHeight();
        final double scale = Math.min(availableWidth / imgWidth, availableHeight / imgHeight);
        final int drawWidth = (int) (imgWidth * scale);
        final int drawHeight = (int) (imgHeight * scale);

        final List<BufferedImage> toBeResized = new ArrayList<>(Arrays.asList(originalIcon));
        final List<BufferedImage> resized = ImageLoader.resizeFrames(toBeResized, drawWidth, drawHeight);
        final BufferedImage toDrawImage = resized.get(0);

        final int x = (availableWidth - drawWidth) / 2;
        final int y = (availableHeight - drawHeight) / 2;
        g.drawImage(toDrawImage, x, y, null);
    }
}
