package lev.filippov;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Turret implements Poolable {
    //игра и отрисовка
    private GameScreen gameScreen;
    private TextureRegion[][] allTextures;
    private int imageX;
    private int imageY;
    //позиция и угол разворота
    private Vector2 position;
    private Vector2 temp;
    private int cellX, cellY;
    private float angle;

    //вспомогательные поля
    private boolean charged;
    private boolean active;
    private float chargeTime; //для проверки готовности к стрельбе

    //игровые характеристики
    private TurretTemplate turretTemplate;


    private float rotationSpeed;
    private float fireRadius;
    private float fireRate;
    private int power;
    private float bulletSpeed;
    private int turretCost;

    //прочие сущности
    private Monster target;


    public Turret(GameScreen gameScreen, TextureRegion[][] allTextures) {
        this.gameScreen = gameScreen;
        this.allTextures = allTextures;
        position = new Vector2();
        temp = new Vector2();
    }

    public void init (int cellX, int cellY, TurretTemplate turretTemplate) {
        //текстуры и координаты
        this.turretTemplate = turretTemplate;
        this.imageX = turretTemplate.getImageX();
        this.imageY = turretTemplate.getImageY();
        this.cellX = cellX;
        this.cellY = cellY;
        position.set(cellX*80+40, cellY*80+40);
        active=true;
        //игровые параметры
        initGameParam();
    }

    private void initGameParam() {
        //параметры пушки
        this.fireRadius = turretTemplate.getFireRadius();
        this.rotationSpeed = turretTemplate.getRotationSpeed();
        this.fireRate = turretTemplate.getFireRate();
        this.turretCost = turretTemplate.getCost();
        //параметры пули
        this.power = bulletType.power;
        this.bulletSpeed = bulletType.speed;
        //вспомогательные
        charged = false;
        chargeTime = 0;
    }

    public TurretTemplate getTurretTemplate() {
        return turretTemplate;
    }

    public int getTurretCost() {
        return turretCost;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void render(SpriteBatch batch) {
        batch.draw(allTextures[imageX][imageY], cellX * 80, cellY * 80, 40, 40, 80, 80, 1, 1, angle);
    }
    public void update(float dt) {

        if (target != null) {
             if(!isMonsterInRange(target) || !target.isActive())
                target = null;
        }

        if (target == null) {
            float maxDst = fireRadius;
            for (int i = 0; i < gameScreen.getMonsterEmitter().getActiveList().size(); i++) {
                Monster m = gameScreen.getMonsterEmitter().getActiveList().get(i);
                float dst = getRangeToTarget(m);
                if (dst < maxDst) {
                    target = m;
//                    break;
                    maxDst = dst;
                }
            }
        }
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
            float rad = (float)Math.toRadians(angle+ MathUtils.random(-5,5)); //введен разброс
            gameScreen.getBulletEmitter().setup(position.x, position.y, (float)Math.cos(rad), (float)Math.sin(rad), bulletType, target);
            System.out.println("Fire!");
            charged = false;
        }
    }

    public void deactivate() {
        active=false;
    }

    public int getCellX() {
        return cellX;
    }

    public int getCellY() {
        return cellY;
    }

}
