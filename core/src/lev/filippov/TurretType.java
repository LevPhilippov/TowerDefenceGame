package lev.filippov;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum TurretType implements Upgradeble {

    COMMON(0,0, 80,80, 300, 180,0.2f, 10, 500, 50) {
        @Override
        public TurretType upgrade() {
            return TurretType.COMMON_UPGRADED;
        }
    },
    COMMON_UPGRADED(0,80, 80,80, 600, 360,0.1f, 20, 700, 150),
    FREEZE(80,0, 80,80, 300, 180,0.2f, 10, 500, 50) {
        @Override
        public TurretType upgrade() {
            return TurretType.FREEZE_UPGRADED;
        }
    },
    FREEZE_UPGRADED(80,80, 80,80, 300, 180,0.2f, 10, 500, 100);

    //координаты в атласе текстур
    final String textureRegionName = "turrets";
    int coordX;
    int coordY;
    int width;
    int height;
    //игровые параметры
    float fireRadius;  // (pix)
    float rotationSpeed; // (deg/sec)
    float fireRate; // (shots/sec)
    int damage; // (hp/shot)
    float bulletSpeed; // (pix/sec)
    int cost;
    boolean upgradeble;

    TurretType(int coordX, int coordY, int width, int height,
               float fireRadius, float rotationSpeed, float fireRate, int damage, float bulletSpeed, int cost) {
        this.coordX = coordX;
        this.coordY = coordY;
        this.width = width;
        this.height = height;
        this.fireRadius = fireRadius;
        this.rotationSpeed = rotationSpeed;
        this.fireRate = fireRate;
        this.damage = damage;
        this.bulletSpeed = bulletSpeed;
        this.cost = cost;
    }


    @Override
    public TurretType upgrade() {
        return null;
    }
}
