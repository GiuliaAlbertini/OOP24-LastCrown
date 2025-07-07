package it.unibo.oop.lastcrown.view.dimensioning;

/**
 * Enum that returns the size percentage occupied by a specific graphic element in a JFrame.
 */
public enum DimensionResolver {
    /**
     * JButton percentage. 
     */
    JBUTTON(new SizePercentage(0.1, 0.085)),

    /**
     * CustomInstruction percentage. 
     */
    INSTRUCTIONS(new SizePercentage(0.5, 0.5)),

    /**
     * Character panel percentage. 
     */
    CHAR(new SizePercentage(0.14, 0.187)),

    /**
     * Hero panel percentage. 
     */
    HERO(new SizePercentage(0.20, 0.30)),

    /**
     * Spell panel percentage. 
     */
    SPELL(new SizePercentage(0.20, 0.30)),

    /**
     * Boss panel percentage. 
     */
    BOSS(new SizePercentage(0.6, 0.7)),

    /**
     * Trader panel percentage. 
     */
    TRADER(new SizePercentage(0.2, 0.4)),

    /**
     * Energy bar percentage. 
     */
    ENERGYBAR(new SizePercentage(0.05, 0.7)),

    /**
     * Deck zone percentage. 
     */
    DECKZONE(new SizePercentage(0.12, 0.9)),

    /**
     * Positioning zone percentage. 
     */
    POSITIONINGZONE(new SizePercentage(0.28, 0.9)),

    /**
     * Wall percentage. 
     */
    WALL(new SizePercentage(0.08, 0.9)),

    /**
     * Trups zone percentage. 
     */
    TRUPSZONE(new SizePercentage(0.3, 0.9)),

    /**
     * Enemies zone percentage. 
     */
    ENEMIESZONE(new SizePercentage(0.22, 0.9));

    private final SizePercentage size;

    /**
     * @param size the size percentage of the selected graphic element.
     */
    DimensionResolver(final SizePercentage size) {
        this.size = size;
    }

    /**
     * @return the width percentage.
     */
    public double width() {
        return this.size.widthPerc();
    }

    /**
     * @return the height percentage.
     */
    public double height() {
        return this.size.heightPerc();
    }
}
