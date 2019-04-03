package lev.filippov;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MonsterEmitter extends ObjectPool <Monster> {
    private GameScreen gameScreen;

    public MonsterEmitter(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    protected Monster newObject() {
        return new Monster(gameScreen);
    }

    public void render (SpriteBatch batch) {
        for (int i = 0; i <activeList.size() ; i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt) {
        for (int i = 0; i <activeList.size() ; i++) {
            activeList.get(i).update(dt);
        }
    }

    public void setup(float x, float y, float vx, float vy, float speed) {
        Monster temp = getActiveElement();
        temp.init(x,y,vx,vy,speed);
    }
}
