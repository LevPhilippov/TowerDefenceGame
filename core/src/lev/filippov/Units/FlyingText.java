package lev.filippov.Units;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lev.filippov.Emitters.Poolable;

public class FlyingText implements Poolable {
    private String text;
    private boolean active;
    private Vector2 position;
    private Vector2 velocity;
    private float time;
    private float maxTime;

    @Override
    public boolean isActive() {
        return active;
    }

    public String getText() {
        return text;
    }

    public Vector2 getPosition() {
        return position;
    }

    public FlyingText() {
        this.text = "";
        this.active = false;
        this.position = new Vector2(0.0f, 0.0f);
        this.velocity = new Vector2(0, 0);
        this.time = 0.0f;
        this.maxTime = 1f;
    }

    public void init(float x, float y, String text) {
        this.position.set(x*80, y*80);
        this.velocity.set(MathUtils.random(-1,1), MathUtils.random(-1,1)).scl(20);
        this.active = true;
        this.text = text;
        this.time = 0.0f;
    }

    public void render (SpriteBatch batch, BitmapFont font) {
        font.setColor(1, 1, 0, ((maxTime-time)/maxTime));
        font.draw(batch, text, position.x, position.y);
        font.setColor(1, 1, 1, 1);

    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        time += dt;
        if (time >= maxTime) {
            active = false;
        }
    }
}
