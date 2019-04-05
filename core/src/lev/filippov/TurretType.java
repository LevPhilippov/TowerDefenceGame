package lev.filippov;

public enum TurretType {

    COMMON_UPGRADED(1,0, 600, 360,0.1f, 20, 700, 150, null, BulletType.AUTOAIM),
    FREEZE_UPGRADED(1,1, 300, 180,0.2f, 10, 500, 100, null, BulletType.SPLASH_FREEZE),
    COMMON(0,0, 300, 180,0.2f, 10, 500, 50, TurretType.COMMON_UPGRADED, BulletType.COMMON),
    FREEZE(0,1, 300, 180,0.2f, 10, 500, 50, TurretType.FREEZE_UPGRADED, BulletType.FREEZE);

    //координаты в атласе текстур
    int imageX;
    int imageY;
    //игровые параметры
    float fireRadius;  // (pix)
    float rotationSpeed; // (deg/sec)
    float fireRate; // (shots/sec)
    int cost;
    BulletType bulletType; //Type of usable bullets
    TurretType upgradeTurret; // Upgraded type of this turret


    TurretType(int imageX, int imageY, float fireRadius, float rotationSpeed, float fireRate, int damage, float bulletSpeed, int cost, TurretType upgradeType, BulletType bulletType) {
        this.imageX = imageX;
        this.imageY = imageY;
        this.fireRadius = fireRadius;
        this.rotationSpeed = rotationSpeed;
        this.fireRate = fireRate;
        this.cost = cost;
        this.upgradeTurret = upgradeType;
        this.bulletType = bulletType;
    }

    public TurretType upgrade() {
        return upgradeTurret;
    }
}
