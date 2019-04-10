package lev.filippov.Emitters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lev.filippov.Screens.GameScreen;
import lev.filippov.Units.Monster;

public class MonsterEmitter extends ObjectPool<Monster> {
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

    public void setup(float x, float y, int monsterHP) {
        Monster temp = getActiveElement();
        temp.init(x,y, monsterHP);
    }

    public void killAllMonsters() {
        for (int i = 0; i <activeList.size() ; i++) {
            activeList.get(i).deactivate();
        }

    }

}
