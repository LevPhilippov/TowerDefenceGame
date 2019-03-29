package lev.filippov;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Monster implements Poolable {
//    private GameScreen gameScreen;

    private TextureRegion texture;
    private TextureRegion textureHp;
    private Vector2 position;
    private Vector2 velocity;
    private float speed = 100;
    private boolean active;

    private int hp;
    private int hpMax;

    public Vector2 getPosition() {
        return position;
    }

    public Monster() {
//        this.gameScreen = gameScreen;
        this.texture = Assets.getInstance().getAtlas().findRegion("monster");
        this.textureHp = Assets.getInstance().getAtlas().findRegion("monsterHp");
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.hpMax = 100;
        this.hp = this.hpMax;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 40, position.y - 40);
        batch.draw(textureHp, position.x - 40, position.y + 40 - 12, 56 * ((float)hp / hpMax), 12);
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);

        if (position.x < 0)
            deactivate();

//        if (position.x > 1280) {
//            position.x = 0;
//        }
//        if (position.x < 0) {
//            position.x = 1280;
//        }
//        if (position.y > 720) {
//            position.y = 0;
//        }
//        if (position.y < 0) {
//            position.y = 720;
//        }
    }

    public void init (float x, float y, float vx, float vy, float speed) {
        position.x = x;
        position.y = y;
        velocity.x = vx;
        velocity.y = vy;
        velocity.scl(speed);
        active = true;
    }

    public void deactivate(){
        active = false;
    }
}
