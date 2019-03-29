package lev.filippov;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Turret {
    private GameScreen gameScreen;

    private TextureRegion texture;
    private Vector2 position;
    private Vector2 temp;
    private int cellX, cellY;
    private float angle;
    private Monster target;
    private float rotationSpeed;
    private float fireRadius;

    private float fireRate;
    private float fireTime;


    public Turret(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.texture = new TextureRegion(Assets.getInstance().getAtlas().findRegion("turrets"), 0, 0, 80, 80);
        this.cellX = 8;
        this.cellY = 4;
        position = new Vector2(cellX*80+40, cellY*80+40);
        temp = new Vector2();
        this.fireRadius = 500f;
        this.rotationSpeed = 180f;
        this.fireRate = 0.4f;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, cellX * 80, cellY * 80, 40, 40, 80, 80, 1, 1, angle);
    }

    public void update(float dt) {
        if (target != null && !isMonsterInRange(target)) {
            target = null;
        }
        if (target == null) {
            float maxDst = 1000.0f;
            for (int i = 0; i < gameScreen.getMonsterEmitter().getActiveList().size(); i++) {
                Monster m = gameScreen.getMonsterEmitter().getActiveList().get(i);
                float dst = position.dst(m.getPosition());
                if (dst < maxDst) {
                    target = m;
                    maxDst = dst;
                }
            }
        }
        if (target != null) {
            checkRotation(dt);
            tryToFire(dt);
        }
    }

    private boolean isMonsterInRange(Monster target) {
        return fireRadius >= Vector2.dst(position.x,position.y, target.getPosition().x, target.getPosition().y);
    }

    public void checkRotation(float dt) {
        float angleTo = getAngleToTarget();

        if (angle > angleTo) {
            if (angle-angleTo > 180) {
                angle += rotationSpeed * dt;
            } else {
                angle -= rotationSpeed * dt;
            }
        }

        if (angle < angleTo) {
            if (angle-angleTo < -180) {
                angle -= rotationSpeed * dt;
            } else {
                angle += rotationSpeed * dt;
            }
        }
        angle%=360;
    }

    private float getAngleToTarget() {
        return temp.set(target.getPosition()).sub(position).angle();
    }

    public void tryToFire(float dt) {
//        if(Math.abs(getAngleToTarget()) > 3)
//            return;
        fireTime += dt;
        if (fireTime > fireRate) {
            fireTime = 0.0f;
            float rad = (float)Math.toRadians(angle);
            gameScreen.getBulletEmitter().setup(position.x, position.y, (float)Math.cos(rad), (float)Math.sin(rad), 250);
            System.out.println("Fire!");
        }
    }}
