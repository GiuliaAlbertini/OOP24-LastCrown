package it.unibo.oop.lastcrown.controller.impl;

import java.awt.image.BufferedImage;

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

public class CharacterSpawnerController {

    private final GamePanel gamePanel;

    public CharacterSpawnerController(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

    }

    public SpawnedCharacter spawnPlayableCharacter(int id, PlayableCharacter characterModel, CharacterDeathObserver observer, int x, int y) {
        PlayableCharacterController controller = PlCharControllerFactory.createPlCharController(observer, id, characterModel);
        controller.attachCharacterAnimationPanel(150, 100);
        return setupCharacter(controller, characterModel.getType().name().toLowerCase(), characterModel.getName(), x, y, true);
    }

    public SpawnedCharacter spawnEnemyCharacter(int id, Enemy enemyModel, CharacterDeathObserver observer, int x, int y) {
        EnemyController controller = EnemyControllerFactory.createEnemyController(observer, id, enemyModel);
        controller.attachCharacterAnimationPanel(150, 100);
        return setupCharacter(controller, enemyModel.getEnemyType().name().toLowerCase(), enemyModel.getName(), x, y, false);
    }

    private SpawnedCharacter setupCharacter(GenericCharacterController controller, String typeFolder, String name, int x, int y, boolean isPlayable) {
        JComponent charComp = controller.getGraphicalComponent();
        charComp.setLocation(x, y);
        gamePanel.addCharacterComponent(charComp);

        Hitbox hitbox = new HitboxImpl(10, 10, new Point2DImpl(x, y));
        HitboxPanel hitboxPanel = new HitboxPanelImpl(hitbox);
        gamePanel.addCharacterComponent(hitboxPanel.getHitboxPanel());

        String path = CharacterPathLoader.loadHitboxPath(typeFolder, name);
        BufferedImage image = ImageLoader.getImage(path, 150, 100);
        HitboxMaskBounds bounds = new HitboxMaskBounds(hitbox, charComp, hitboxPanel);
        bounds.calculateHitboxCenter(image);

        HitboxController hitboxController = new HitboxControllerImpl(hitbox, hitboxPanel, bounds);

        if (isPlayable) {
            Radius radius = new RadiusImpl(hitbox, 300);
            RadiusPanel radiusPanel = new RadiusPanelImpl(radius, bounds);
            hitboxController.setRadius(radius);
            hitboxController.setRadiusPanel(radiusPanel);
            gamePanel.addCharacterComponent(radiusPanel.getRadiusPanel());
        }

        return new SpawnedCharacter(controller, hitboxController);
    }
}
