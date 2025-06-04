package it.unibo.oop.lastcrown.view.spell.impl;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides the frames paths of the spells animations.
 */
public final class SpellPathLoader {
    private static final String SEP = System.getProperty("file.separator");
    private static final String ICON = "icon";
    private static final String ICON_GREY = "icon_grey";

    private SpellPathLoader() { }

    /**
     * Loads the frames animation paths of a specific spell.
     * @param spellName the name of the spell
     * @return a list of the animation frames paths
     */
    public static synchronized List<String> loadSpellPaths(final String spellName) {
        final List<String> spellPaths = new ArrayList<>();
        int cont = 1; 
        String relativePath = getGenericSpellPath(spellName, Integer.toString(cont));
        boolean exists = Files.exists(Paths.get(relativePath));
        while (exists) {
            spellPaths.addLast(relativePath);
            cont++;
            relativePath = getGenericSpellPath(spellName, Integer.toString(cont));
            exists = Files.exists(Paths.get(relativePath));
        }
        return spellPaths;
    }

    /**
     * @param spellName the name of a spell
     * @return the icon path of the given spell 
     */
    public static synchronized String loadIconPath(final String spellName) {
        return getGenericSpellPath(spellName, ICON);
    }

    /**
     * @param spellName the name of a spell
     * @return the grey icon path of the given spell 
     */
    public static synchronized String loadGreyIconPath(final String spellName) {
        return getGenericSpellPath(spellName, ICON_GREY);
    }

    /**
     * @return the spell border path 
     */
    public static synchronized String loadSpellBorder() {
        return "src" + SEP + "main" + SEP + "resources" + SEP + "pngs" 
        + SEP + "spell" + SEP + "spell_border";
    }

    private static String getGenericSpellPath(final String spellName, final String keyWord) {
        return "src" + SEP + "main" + SEP + "resources" + SEP + "pngs"
        + SEP + "spell" + SEP + spellName + SEP + keyWord + ".png";
    }

}

