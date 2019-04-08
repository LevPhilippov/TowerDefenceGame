package lev.filippov;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Bullet implements Poolable {

    private TextureRegion texture;
    private Vector2 position;
    private  Vector2 velocity;
    private boolean active;
    private GameScreen gameScreen;
    private Circle hitBox;
    private final float bulletRadius = 8;
    private float speed;
    private int power;
    private float maxLifetime;
    private float lifetime;
    private Monster target;
    private BulletTemplate bulletTemplate;
    private boolean autoAim;


    public Bullet (GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        texture = Assets.getInstance().getAtlas().findRegion("star16");
        position = new Vector2();
        velocity = new Vector2();
        hitBox = new Circle(position, bulletRadius);
        maxLifetime = 3;
    }

    public Circle getHitBox() {
        return hitBox;
    }

    public int getPower() {
        return power;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    void init (float x, float y, float vx, float vy, BulletTemplate bulletTemplate, Monster target) {
        this.bulletTemplate = bulletTemplate;
        this.target = target;
        this.speed = bulletTemplate.getSpeed();
        this.power = bulletTemplate.getPower();
        this.autoAim = bulletTemplate.isAutoaim();
        position.set(x,y);
        velocity.set(vx,vy).nor().scl(speed);
        active = true;
        lifetime=0;
    }

    public void render(SpriteBatch batch) {
        batch.setColor(1,0,0,1);
        batch.draw(texture, position.x-8, position.y-8);
        batch.setColor(1,1,1,1);
    }

    public void update(float dt) {
        lifetime +=dt;
        //если пуля самонаводящаяся
        if(autoAim) {
            if(target.isActive()) {
                velocity.set(target.getPosition()).sub(position).nor().scl(speed);
            }
            else {
                autoAim = false;
            }
        }

        position.mulAdd(velocity, dt);
        hitBox.setPosition(position);
        gameScreen.getParticleEmitter().setup(position.x, position.y, MathUtils.random(-25, 25), MathUtils.random(-25, 25), 0.1f,1.2f,0.2f,1,0,0,1,1,1,0,1);

        if (position.x < 0 || position.x>1280 || position.y<0 || position.y>720 || lifetime>maxLifetime) {
            deactivate();
        }
    }

    public void deactivate(){
        active = false;
    }

    public void makeDamage(Monster m){
        m.receiveDamage(power);
        deactivate();
    }

}
