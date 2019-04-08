package lev.filippov;

public class CheckCollisonServise {

    public static void checkCollision(GameScreen gameScreen) {
        Monster m;
        Bullet b;
        for (int i = 0; i <gameScreen.getMonsterEmitter().getActiveList().size() ; i++) {
            m = gameScreen.getMonsterEmitter().getActiveList().get(i);
            for (int j = 0; j <gameScreen.getBulletEmitter().getActiveList().size() ; j++) {
                b =gameScreen.getBulletEmitter().getActiveList().get(j);
                if(m.getHitBox().overlaps(b.getHitBox())){
                    b.makeDamage(m);
                }
            }
        }
    }
}
