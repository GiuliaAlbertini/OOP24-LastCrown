package it.unibo.oop.lastcrown.model.impl.Handler;
import it.unibo.oop.lastcrown.model.api.Point2D;
import it.unibo.oop.lastcrown.model.characters.api.InGameCharacter;
import it.unibo.oop.lastcrown.model.impl.Point2DImpl;

public class HandleFollowEnemy {
    private final InGameCharacter enemy;
    private final InGameCharacter character;

   
    private double t=0.0; //parametro t per moviment lungo la curva
    private final float mapHeight=100.0f;
    private final float mapcenter=mapHeight/2;
    
    public HandleFollowEnemy(final InGameCharacter character,final InGameCharacter enemy, double t){
        this.character=character;
        this.enemy=enemy;
        this.t=t;
    }

    
    public void updateMovement(){
        if (isMovementDone()){
            return;
        }
        advanceTime();
        
        final Point2D characterPos= character.getHitbox().getPosition();//pos0
        final Point2D enemyPos= enemy.getHitbox().getPosition(); //pos2
       
        Point2D controlPoint= computeControlPoint(characterPos, enemyPos);
        Point2D newPos= quadraticBezier(characterPos, enemyPos, controlPoint, t);
        character.getHitbox().setPosition(newPos);

        if (hasCollided()) {
            stopMovement();
        }
    
    }



    public Point2D quadraticBezier(Point2D characterPos, Point2D enemyPos, Point2D controlPoint, double t ){
        double oneMinusT = 1 - t;
        double x= oneMinusT * oneMinusT * characterPos.x()
                  + 2 * (oneMinusT) * controlPoint.x()
                  + t * t * enemyPos.x();
        double y= oneMinusT * oneMinusT * characterPos.y()
                  + 2 * (oneMinusT) * controlPoint.y()
                  + t * t * enemyPos.y();
        return new Point2DImpl(x, y);        
    }


    // Funzione per calcolare il punto di controllo P1 in base a P0 e P2
    public Point2D computeControlPoint(Point2D characterPos, Point2D enemyPos){
        double cx = (characterPos.x() +enemyPos.x()) / 2;
        double cy = (characterPos.y() > mapcenter) 
                    ? Math.min(characterPos.y(), enemyPos.y()) 
                    : Math.max(characterPos.y(), enemyPos.y()); 
        return new Point2DImpl(cx, cy);
    }

    private void advanceTime() {
        t = Math.min(1.0, t + 0.01);
    }


    private void stopMovement() {
        t = 1.0;
    }
    
    public boolean isMovementDone() {
        return t >= 1.0;
    }

    public boolean hasCollided(){
        return character.getHitbox().checkCollision(enemy.getHitbox());
    }
}
