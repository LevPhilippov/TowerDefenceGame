package lev.filippov;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BulletEmitter extends ObjectPool<Bullet> {
    private Bullet temp;
    private GameScreen gameScreen;

    public BulletEmitter (GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    protected Bullet newObject() {
        return new Bullet(gameScreen);
    }

    public void setup(float x, float y, float vx, float vy, float speed, int damage){
        Bullet temp = getActiveElement();
        temp.init(x,y,vx,vy,speed, damage);
    }

    public void update(float dt){
        for (int i = 0; i <activeList.size() ; i++) {
            activeList.get(i).update(dt);
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i <activeList.size() ; i++) {
            activeList.get(i).render(batch);
        }
    }


}
