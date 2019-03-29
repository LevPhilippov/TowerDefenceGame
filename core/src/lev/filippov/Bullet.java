package lev.filippov;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Bullet implements Poolable {

    private TextureRegion texture;
    private Vector2 position;
    private  Vector2 velocity;
    private boolean active;

    public Bullet () {
        texture = Assets.getInstance().getAtlas().findRegion("star16");
        position = new Vector2();
        velocity = new Vector2();
    }

    @Override
    public boolean isActive() {
        return active;
    }

    void init (float x, float y, float vx, float vy, float speed) {
        position.x = x;
        position.y = y;
        velocity.x = x;
        velocity.y = y;
        velocity.scl(speed);
        active = true;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
    }

    public void deactivate(){
        active = false;
    }

}
