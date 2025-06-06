package it.unibo.oop.lastcrown.controller.api;

import javax.swing.JComponent;
import javax.swing.JPanel;

import it.unibo.oop.lastcrown.model.api.Hitbox;
import it.unibo.oop.lastcrown.model.api.Radius;
import it.unibo.oop.lastcrown.view.impl.RadiusPanel;

public interface HitboxController {
    public void setnewPosition(int x, int y);
    public void updateView();
    public void setVisibile(boolean visible);
    public JComponent getGraphicalComponent(); 
    public Hitbox getHitbox();
    public Radius getRadius();
    public JPanel getRadiusPanel();
    public void setRadius(Radius radius);
    public void setRadiusPanel(RadiusPanel radiusPanel);
}
