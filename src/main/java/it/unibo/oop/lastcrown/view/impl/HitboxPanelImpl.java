package it.unibo.oop.lastcrown.view.impl;
import java.awt.Color;

import javax.swing.JPanel;

import it.unibo.oop.lastcrown.model.api.Hitbox;
import it.unibo.oop.lastcrown.model.impl.Point2DImpl;
import it.unibo.oop.lastcrown.view.api.HitboxPanel;

public class HitboxPanelImpl implements HitboxPanel{
    Hitbox hitbox;
    JPanel hitboxPanel;

    public HitboxPanelImpl(Hitbox hitbox){
        this.hitbox=hitbox;
        this.hitboxPanel= new JPanel();
        this.hitboxPanel.setBackground(new Color(255, 0, 0, 100)); // Rosso semi-trasparente per debug
        updatePanel();
    }

    public JPanel getHitboxPanel(){
        return this.hitboxPanel;
    }


    public void updatePanel(){
        hitboxPanel.setBounds(
            (int) hitbox.getPosition().x(),
            (int) hitbox.getPosition().y(),                
            hitbox.getWidth(),
            hitbox.getHeight()
        );
    }
    
    public void setPanelPosition(int x, int y){
       hitbox.setPosition(new Point2DImpl(x, y));
       updatePanel();
    }
}
