package lev.filippov.Emitters;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lev.filippov.Units.FlyingText;
import lev.filippov.Screens.GameScreen;

public class InfoEmitter extends ObjectPool<FlyingText> {
    private GameScreen gameScreen;

    @Override
    protected FlyingText newObject() {
        return new FlyingText();
    }

    public InfoEmitter(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public void setup(float x, float y, String  text) {
        FlyingText flyingText = getActiveElement();
        flyingText.init(x, y, text);
    }

    public void render(SpriteBatch batch, BitmapFont font) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch,font);
        }
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            FlyingText flyingText = activeList.get(i);
            flyingText.update(dt);
        }
    }
}
