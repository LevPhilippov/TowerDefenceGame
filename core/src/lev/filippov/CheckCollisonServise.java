package lev.filippov;

import lev.filippov.Screens.GameScreen;
import lev.filippov.Units.Bullet;
import lev.filippov.Units.Monster;

public class CheckCollisonServise {

    public static void checkCollision(GameScreen gameScreen) {
        Monster m;
        Bullet b;
        for (int i = 0; i <gameScreen.getMonsterEmitter().getActiveList().size() ; i++) {
            m = gameScreen.getMonsterEmitter().getActiveList().get(i);
            if(!m.isActive())
                return;
            for (int j = 0; j <gameScreen.getBulletEmitter().getActiveList().size() ; j++) {
                b =gameScreen.getBulletEmitter().getActiveList().get(j);
                if(!b.isActive())
                    return;
                if(m.getHitBox().overlaps(b.getHitBox())){
                    b.makeDamage(m);
                }
            }
        }
    }

    public static void checkBulletCollisionsWithWalls(GameScreen gameScreen) {
        for (Bullet bullet : gameScreen.getBulletEmitter().getActiveList()) {
            if (!bullet.isActive())
                return;
            int cellX = (int)(bullet.getPosition().x/80);
            int cellY = (int)(bullet.getPosition().y/80);
            if(gameScreen.getMap().getData()[cellX][cellY] == gameScreen.getMap().getELEMENT_WALL()){
                bullet.deactivate();
            }
        }
    }
}
