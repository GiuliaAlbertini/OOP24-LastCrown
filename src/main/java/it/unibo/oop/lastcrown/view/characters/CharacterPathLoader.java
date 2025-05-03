package it.unibo.oop.lastcrown.view.characters;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides the frames paths of the characters animations.
 */
public final class CharacterPathLoader {
    private static final String SEP = System.getProperty("file.separator");
    private CharacterPathLoader() { }
    /**
     * @param charType the character type
     * @param charName the character name
     * @return all the attack paths of the specific character.
     */
    public static synchronized List<List<String>> loadAttackPaths(final String charType, final String charName) {
        int cont = 1;
        final List<List<String>> attacksPaths = new ArrayList<>();
        attacksPaths.addLast(loadPaths(charType, charName, Keyword.ATTACK.get() + cont + "_"));
        while (!attacksPaths.getLast().isEmpty()) {
            cont++;
            attacksPaths.addLast(loadPaths(charType, charName, Keyword.ATTACK.get() + cont + "_"));
        }
        attacksPaths.removeLast();
        return attacksPaths;
    }

    /**
     * @param charType the character type
     * @param charName the character name
     * @param keyWord the specific animation keyword
     * @return the animation paths corresponding to the given keyword of the specific character.
     */
    public static synchronized List<String> loadPaths(final String charType, final String charName, final String keyWord) {
        int cont = 1;
        final List<String> paths = new ArrayList<>();
        String relativePath = "src" + SEP + "main" + SEP + "resources" + SEP + "pngs"
         + SEP + charType + SEP + charName + SEP + keyWord + cont + ".png";
        boolean exists = Files.exists(Paths.get(relativePath));
        while (exists) {
            paths.addLast(relativePath);
            cont++;
            relativePath = ("src" + SEP + "main" + SEP + "resources" + SEP + "pngs"
             + SEP + charType + SEP + charName + SEP + keyWord + cont + ".png");
            exists = Files.exists(Paths.get(relativePath));
        }
        return paths;
    }
}
