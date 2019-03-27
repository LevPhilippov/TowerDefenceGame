package lev.filippov;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Monster {
    private GameScreen gameScreen;

    private TextureRegion texture;
    private TextureRegion textureHp;
    private Vector2 position;
    private Vector2 velocity;

    private int hp;
    private int hpMax;

    public Vector2 getPosition() {
        return position;
    }

    public Monster(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.texture = Assets.getInstance().getAtlas().findRegion("monster");
        this.textureHp = Assets.getInstance().getAtlas().findRegion("monsterHp");
        this.position = new Vector2(640, 360);
        this.velocity = new Vector2(MathUtils.random(-1.0f, 1.0f), MathUtils.random(-1.0f, 1.0f));
        this.velocity.nor().scl(300.0f);
        this.hpMax = 100;
        this.hp = this.hpMax;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 40, position.y - 40);
        batch.draw(textureHp, position.x - 40, position.y + 40 - 12, 56 * ((float)hp / hpMax), 12);
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        if (position.x > 1280) {
            position.x = 0;
        }
        if (position.x < 0) {
            position.x = 1280;
        }
        if (position.y > 720) {
            position.y = 0;
        }
        if (position.y < 0) {
            position.y = 720;
        }
    }
}
