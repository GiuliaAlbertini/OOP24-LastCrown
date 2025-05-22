package it.unibo.oop.lastcrown.view.impl;

import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import it.unibo.oop.lastcrown.model.api.Hitbox;
import it.unibo.oop.lastcrown.model.impl.Point2DImpl;
import it.unibo.oop.lastcrown.view.api.FrameListener;

public class HitboxMaskBounds implements FrameListener {
    int width,height,minX,minY,maxX,maxY; 
    private final Hitbox hitbox;
    private final JComponent charComponent; 
    private final HitboxPanelImpl hitboxPanel;


    public HitboxMaskBounds(Hitbox hitbox, JComponent charComponent, HitboxPanelImpl hitboxPanel){
        this.charComponent= charComponent;
        this.hitbox=hitbox;
        this.hitboxPanel=hitboxPanel;
    }

    public void calculateHitboxCenter(BufferedImage image) {
        minX = image.getWidth();
        minY = image.getHeight();
        maxX = 0;
        maxY = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xff;
                if (alpha > 0) {
                    if (x < minX) minX = x;
                    if (y < minY) minY = y;
                    if (x > maxX) maxX = x;
                    if (y > maxY) maxY = y;
                }
            }
        }
        int newWidth = maxX - minX + 1;
        int newHeight = maxY - minY + 1;

        int globalX = charComponent.getX() + minX;
        int globalY = charComponent.getY() + minY;

        hitbox.setPosition(new Point2DImpl(globalX, globalY));
        hitbox.setWidth(newWidth);
        hitbox.setHeight(newHeight);
        hitboxPanel.updatePanel();
        
        System.out.println("Hitbox aggiornata: pos=" + hitbox.getPosition() + 
                           " size=" + newWidth + "x" + newHeight);
    }

    @Override
    public void onFrame(BufferedImage frame) {
        calculateHitboxCenter(frame);
    }
}
