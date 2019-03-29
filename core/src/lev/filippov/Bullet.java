package lev.filippov;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Bullet implements Poolable {

    private TextureRegion texture;
    private Vector2 position;
    private  Vector2 velocity;
    private boolean active;
    private GameScreen gameScreen;

    public Bullet (GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        texture = Assets.getInstance().getAtlas().findRegion("star16");
        position = new Vector2();
        velocity = new Vector2();
    }

    @Override
    public boolean isActive() {
        return active;
    }

    void init (float x, float y, float vx, float vy, float speed) {
        position.set(x,y);
        velocity.set(vx,vy);
        velocity.nor();
        velocity.scl(speed);
        active = true;
    }

    public void render(SpriteBatch batch) {
        batch.setColor(1,0,0,1);
        batch.draw(texture, position.x-8, position.y-8);
        batch.setColor(1,1,1,1);
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        gameScreen.getParticleEmitter().setup(position.x, position.y, MathUtils.random(-25, 25), MathUtils.random(-25, 25), 0.1f,1.2f,0.2f,1,0,0,1,1,1,0,1);

        if (position.x < 0 || position.x>1280 || position.y<0 || position.y>720)
            deactivate();
    }

    public void deactivate(){
        active = false;
    }

}
