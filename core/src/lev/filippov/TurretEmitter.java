package lev.filippov;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TurretEmitter extends ObjectPool<Turret> {
    private GameScreen gameScreen;

    public TurretEmitter(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    protected Turret newObject() {
        return new Turret(gameScreen);
    }

    public boolean setup(int cellX, int cellY) {
        if (!canIDeployItHere(cellX, cellY)) {
            return false;
        }
        Turret turret = getActiveElement();
        turret.init(cellX, cellY);
        return true;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
    }

    public boolean canIDeployItHere(int cellX, int cellY) {
        if (!gameScreen.getMap().isEmpty(cellX, cellY)) {
            return false;
        }
        //проверка на предмет уже установленной турели (позже заменить - будет модификация карты)
        for (int i = 0; i < activeList.size(); i++) {
            Turret t = activeList.get(i);
            if (t.getCellX() == cellX && t.getCellY() == cellY) {
                return false;
            }
        }
        return true;
    }
}
