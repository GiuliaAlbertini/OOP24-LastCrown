package it.unibo.oop.lastcrown.controller.impl;
import javax.swing.JComponent;
import javax.swing.JPanel;

import it.unibo.oop.lastcrown.controller.api.HitboxController;
import it.unibo.oop.lastcrown.model.api.Hitbox;
import it.unibo.oop.lastcrown.model.api.Radius;
import it.unibo.oop.lastcrown.view.api.HitboxPanel;
import it.unibo.oop.lastcrown.view.impl.HitboxMaskBounds;
import it.unibo.oop.lastcrown.view.impl.RadiusPanel;

public class HitboxControllerImpl implements HitboxController {
    private Hitbox hitbox;
    private HitboxPanel view;
    private HitboxMaskBounds bounds;
    private Radius radius;
    private RadiusPanel radiusPanel;

    
    public HitboxControllerImpl (Hitbox hitbox, HitboxPanel panel,HitboxMaskBounds bounds){
        this.hitbox=hitbox;
        this.view=panel;
        this.bounds=bounds;
    }

    public void setnewPosition(int x, int y){
        bounds.updateHitboxPosition(x, y);
        if (radiusPanel != null) {
            radiusPanel.updatePosition();  
        }
        view.updatePanel();
    }

    public void updateView(){
        view.updatePanel();
    }

    public void setVisibile(boolean visible){
        view.getHitboxPanel().setVisible(visible);
    }

    @Override
    public JComponent getGraphicalComponent() {
        return view.getHitboxPanel();
    }
    
    @Override
    public Radius getRadius() {
        return this.radius;
    }

    @Override
    public JPanel getRadiusPanel() {
        return radiusPanel.getRadiusPanel();
    }
    
    @Override
    public Hitbox getHitbox(){
        return this.hitbox;
    }
    
    @Override
    public void setRadius(Radius radius) {
        this.radius = radius;
    }

    @Override
    public void setRadiusPanel(RadiusPanel radiusPanel) {
        this.radiusPanel = radiusPanel;
    }
}
