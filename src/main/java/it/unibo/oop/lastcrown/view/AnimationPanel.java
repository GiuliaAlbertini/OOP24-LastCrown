package it.unibo.oop.lastcrown.view;

/**
 * An interface that models a generic AnimationPanel.
 */
public interface AnimationPanel {

    /**
     * @return the horizontal position of the top left corner of this animation panel
     */
    int getX();

    /**
     * @return the vertical position of the top left corner of this animation panel
     */
    int getY();

    /**
     * @return the horizontal size of this panel
     */
    int getWidth();

    /**
     * @return the vertical size of this panel
     */
    int getHeight();

    /**
     * Set the location (coordinates x, y) of this animation panel.
     * @param x horizontal coordinate of the top left corner 
     * @param y vertical coordinate of the top left corner
     */
    void setLocation(int x, int y);
}
