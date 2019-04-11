package lev.filippov.Emitters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lev.filippov.Screens.GameScreen;
import lev.filippov.Templates.BulletTemplate;
import lev.filippov.Units.Bullet;
import lev.filippov.Units.Monster;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class BulletEmitter extends ObjectPool<Bullet> {
    private Bullet temp;
    private GameScreen gameScreen;
    private HashMap <String, BulletTemplate> bulletTemplates;

    public BulletEmitter (GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.bulletTemplates = new HashMap<String, BulletTemplate>();
    }

    @Override
    protected Bullet newObject() {
        return new Bullet(gameScreen);
    }

    public void setup(float x, float y, float vx, float vy, String bulletTemplateName, Monster target){
        Bullet b = getActiveElement();
        b.init(x,y,vx,vy,bulletTemplates.get(bulletTemplateName), target);
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

    public HashMap<String, BulletTemplate> getBulletTemplates() {
        return bulletTemplates;
    }
}
