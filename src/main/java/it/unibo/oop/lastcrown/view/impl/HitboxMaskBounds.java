package it.unibo.oop.lastcrown.view.impl;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import it.unibo.oop.lastcrown.model.api.Hitbox;
import it.unibo.oop.lastcrown.model.api.Point2D;
import it.unibo.oop.lastcrown.model.impl.Point2DImpl;
import it.unibo.oop.lastcrown.view.api.HitboxPanel;

public class HitboxMaskBounds{
    private int offsetX;
    private int offsetY;
    private int hitboxHeight;
    private int hitboxWidth;
    private final Hitbox hitbox;
    private final JComponent charComponent; 
    private final HitboxPanel hitboxPanel;


    public HitboxMaskBounds(Hitbox hitbox, JComponent charComponent, HitboxPanel hitboxPanel){
        this.charComponent= charComponent;
        this.hitbox=hitbox;
        this.hitboxPanel=hitboxPanel;
        this.offsetX = 0;
        this.offsetY = 0;
        this.hitboxWidth = 0;
        this.hitboxHeight = 0;
    }

    public void calculateHitboxCenter(BufferedImage image) {
        int minX = image.getWidth();
        int minY = image.getHeight();
        int maxX = 0;
        int maxY = 0;
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
        this.hitboxWidth = maxX - minX + 1;
        this.hitboxHeight = maxY - minY + 1;
        this.offsetX=minX;
        this.offsetY=minY;
        
        // Aggiorna la posizione iniziale
        updateHitboxPosition(charComponent.getX(), charComponent.getY());
        //dimensioni hitbox
        hitbox.setWidth(hitboxWidth);
        hitbox.setHeight(hitboxHeight);
        hitboxPanel.updatePanel();         
        
    }

    public void updateHitboxPosition(int componentX, int componentY){
        int globalX = componentX + offsetX;
        int globalY = componentY + offsetY;
        hitbox.setPosition(new Point2DImpl(globalX, globalY));
        hitboxPanel.updatePanel();
    }

    public Point2D getCenter(){
        // centro relativo alla hitbox (offset + metà dimensioni)
        double centerX = offsetX + (hitboxWidth / 2.0);
        double centerY = offsetY + (hitboxHeight / 2.0);
        return new Point2DImpl(centerX, centerY);
    }

    public JComponent getCharComponent() {
        return this.charComponent;
    }
}
