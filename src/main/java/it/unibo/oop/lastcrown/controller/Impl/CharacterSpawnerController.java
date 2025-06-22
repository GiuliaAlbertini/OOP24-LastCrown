package it.unibo.oop.lastcrown.controller.impl;

import java.awt.image.BufferedImage;
import java.util.Locale;
import javax.swing.JComponent;

import it.unibo.oop.lastcrown.controller.characters.api.CharacterDeathObserver;
import it.unibo.oop.lastcrown.controller.characters.api.EnemyController;
import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.controller.characters.impl.enemy.EnemyControllerFactory;
import it.unibo.oop.lastcrown.controller.characters.impl.playablecharacter.PlCharControllerFactory;
import it.unibo.oop.lastcrown.model.api.Hitbox;
import it.unibo.oop.lastcrown.model.api.Radius;
import it.unibo.oop.lastcrown.controller.api.HitboxController;
import it.unibo.oop.lastcrown.model.characters.api.Enemy;
import it.unibo.oop.lastcrown.model.characters.api.PlayableCharacter;
import it.unibo.oop.lastcrown.model.impl.HitboxImpl;
import it.unibo.oop.lastcrown.model.impl.Point2DImpl;
import it.unibo.oop.lastcrown.model.impl.RadiusImpl;
import it.unibo.oop.lastcrown.view.GamePanel;
import it.unibo.oop.lastcrown.view.ImageLoader;
import it.unibo.oop.lastcrown.view.api.HitboxPanel;
import it.unibo.oop.lastcrown.view.characters.CharacterPathLoader;
import it.unibo.oop.lastcrown.view.impl.HitboxMaskBounds;
import it.unibo.oop.lastcrown.view.impl.HitboxPanelImpl;
import it.unibo.oop.lastcrown.view.impl.RadiusPanel;
import it.unibo.oop.lastcrown.view.impl.RadiusPanelImpl;

/**
 * Controller responsible for spawning and initializing characters in the game.
 * Handles both playable and enemy characters by creating their controllers,
 * graphical components, hitboxes, and optional radius panels, then adds them to the GamePanel.
 */
public final class CharacterSpawnerController {

    private static final int CHARACTER_WIDTH = 150;
    private static final int CHARACTER_HEIGHT = 100;
    private static final int HITBOX_WIDTH = 10;
    private static final int HITBOX_HEIGHT = 10;
    private static final int DEFAULT_RADIUS = 200;

    private final GamePanel gamePanel;

    /**
     * Constructs a CharacterSpawnerController with the given game panel.
     *
     * @param gamePanel the main game panel where characters and related components will be added
     */
    public CharacterSpawnerController(final GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    /**
     * Spawns a playable character, attaches its animation panel,
     * and sets up graphical and collision-related components.
     *
     * @param id unique identifier for the character
     * @param characterModel the model representing the playable character
     * @param observer observer for character death events
     * @param x initial x-position on the panel
     * @param y initial y-position on the panel
     * @return a SpawnedCharacter containing the character's controller and hitbox
     */
    public SpawnedCharacter spawnPlayableCharacter(final int id, final PlayableCharacter characterModel,
            final CharacterDeathObserver observer, final int x, final int y) {
        final PlayableCharacterController controller =
            PlCharControllerFactory.createPlCharController(observer, id, characterModel);
        controller.attachCharacterAnimationPanel(CHARACTER_WIDTH, CHARACTER_HEIGHT);
        return setupCharacter(controller, characterModel.getType().name().toLowerCase(Locale.ROOT),
                              characterModel.getName(), x, y, true);
    }

    /**
     * Spawns an enemy character, attaches its animation panel,
     * and sets up graphical and collision-related components.
     *
     * @param id unique identifier for the enemy
     * @param enemyModel the model representing the enemy
     * @param observer observer for character death events
     * @param x initial x-position on the panel
     * @param y initial y-position on the panel
     * @return a SpawnedCharacter containing the enemy's controller and hitbox
     */
    public SpawnedCharacter spawnEnemyCharacter(final int id, final Enemy enemyModel,
            final CharacterDeathObserver observer, final int x, final int y) {
        final EnemyController controller = EnemyControllerFactory.createEnemyController(observer, id, enemyModel);
        controller.attachCharacterAnimationPanel(CHARACTER_WIDTH, CHARACTER_HEIGHT);
        return setupCharacter(controller, enemyModel.getEnemyType().name().toLowerCase(Locale.ROOT),
                              enemyModel.getName(), x, y, false);
    }

    private SpawnedCharacter setupCharacter(final GenericCharacterController controller, final String typeFolder,
            final String name, final int x, final int y, final boolean isPlayable) {

        final JComponent charComp = controller.getGraphicalComponent();
        charComp.setLocation(x, y);
        gamePanel.addCharacterComponent(charComp);

        final Hitbox hitbox = new HitboxImpl(HITBOX_WIDTH, HITBOX_HEIGHT, new Point2DImpl(x, y));
        final HitboxPanel hitboxPanel = new HitboxPanelImpl(hitbox);
        gamePanel.addCharacterComponent(hitboxPanel.getHitboxPanel());

        final String path = CharacterPathLoader.loadHitboxPath(typeFolder, name);
        final BufferedImage image = ImageLoader.getImage(path, CHARACTER_WIDTH, CHARACTER_HEIGHT);
        final HitboxMaskBounds bounds = new HitboxMaskBounds(hitbox, charComp, hitboxPanel);
        bounds.calculateHitboxCenter(image);

        final HitboxController hitboxController = new HitboxControllerImpl(hitbox, hitboxPanel, bounds);

        if (isPlayable) {
            final Radius radius = new RadiusImpl(hitbox, DEFAULT_RADIUS);
            final RadiusPanel radiusPanel = new RadiusPanelImpl(radius, bounds);
            hitboxController.setRadius(radius);
            hitboxController.setRadiusPanel(radiusPanel);
            gamePanel.addCharacterComponent(radiusPanel.getRadiusPanel());
        }

        return new SpawnedCharacter(controller, hitboxController);
    }
}
