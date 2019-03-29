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
    private float chargeTime;
    private boolean charged;


    public Turret(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.texture = new TextureRegion(Assets.getInstance().getAtlas().findRegion("turrets"), 0, 0, 80, 80);
        this.cellX = 8;
        this.cellY = 4;
        position = new Vector2(cellX*80+40, cellY*80+40);
        temp = new Vector2();
        this.fireRadius = 500f;
        this.rotationSpeed = 360f;
        this.fireRate = 1f;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, cellX * 80, cellY * 80, 40, 40, 80, 80, 1, 1, angle);
    }

    public void update(float dt) {
        target = null;

//        if (target != null && !isMonsterInRange(target)) {
//            target = null;
//        }
 //       if (target == null) {
            float maxDst = fireRadius;
            for (int i = 0; i < gameScreen.getMonsterEmitter().getActiveList().size(); i++) {
                Monster m = gameScreen.getMonsterEmitter().getActiveList().get(i);
                float dst = getRangeToTarget(m);
                if (dst < maxDst) {
                    target = m;
                    break;
 //                   maxDst = dst;
                }
            }
//        }
        if (target != null) {
            checkRotation(dt);
            openFire(dt);
        }
    }
    private float getRangeToTarget(Monster target) {
        return position.dst(target.getPosition());
    }
    private boolean isMonsterInRange(Monster target) {
        return fireRadius >= getRangeToTarget(target);
    }

    public void checkRotation(float dt) {
//        float angleTo = getAngleToTarget();
//        angleTo%=360;
//        angle%=360;
//
//        if (angle >= angleTo) {
//            if (angle-angleTo > 180) {
//                angle += rotationSpeed * dt;
//            } else {
//                angle -= rotationSpeed * dt;
//            }
//        }
//
//        if (angle < angleTo) {
//            if (angle-angleTo < -180) {
//                angle -= rotationSpeed * dt;
//            } else {
//                angle += rotationSpeed * dt;
//            }
//        }
        if (target != null) {
            float angleTo = getAngleToTarget();
            if (angle > angleTo) {
                if (Math.abs(angle - angleTo) <= 180.0f) {
                    angle -= rotationSpeed * dt;
                } else {
                    angle += rotationSpeed * dt;
                }
            }
            if (angle < angleTo) {
                if (Math.abs(angle - angleTo) <= 180.0f) {
                    angle += rotationSpeed * dt;
                } else {
                    angle -= rotationSpeed * dt;
                }
            }
            if (angle < 0.0f) {
                angle += 360.0f;
            }
            if (angle > 360.0f) {
                angle -= 360.0f;
            }
        }
    }

    public float getAngleToTarget() {
        return temp.set(target.getPosition()).sub(position).angle();
    }

    public void openFire(float dt) {
        chargeTime += dt;

        if(chargeTime>=fireRate)
            charged = true;

        if (Math.abs(getAngleToTarget()-angle)<4 && charged) {
            chargeTime = 0.0f;
            float rad = (float)Math.toRadians(angle);
            gameScreen.getBulletEmitter().setup(position.x, position.y, (float)Math.cos(rad), (float)Math.sin(rad), 500);
            System.out.println("Fire!");
            charged = false;
        }
    }}
