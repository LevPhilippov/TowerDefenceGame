package lev.filippov;

public enum TurretType {

    COMMON_UPGRADED(1,0, 600, 360,0.1f, 20, 700, 150, null),
    FREEZE_UPGRADED(1,1, 300, 180,0.2f, 10, 500, 100, null),
    COMMON(0,0, 300, 180,0.2f, 10, 500, 50, TurretType.COMMON_UPGRADED),
    FREEZE(0,1, 300, 180,0.2f, 10, 500, 50, TurretType.FREEZE_UPGRADED);

    //координаты в атласе текстур
    int imageX;
    int imageY;
    //игровые параметры
    float fireRadius;  // (pix)
    float rotationSpeed; // (deg/sec)
    float fireRate; // (shots/sec)
    int damage; // (hp/shot)
    float bulletSpeed; // (pix/sec)
    int cost;
    TurretType upgradeTurret; // Upgraded type of this turret


    TurretType(int imageX, int imageY, float fireRadius, float rotationSpeed, float fireRate, int damage, float bulletSpeed, int cost, TurretType upgradeType) {
        this.imageX = imageX;
        this.imageY = imageY;
        this.fireRadius = fireRadius;
        this.rotationSpeed = rotationSpeed;
        this.fireRate = fireRate;
        this.damage = damage;
        this.bulletSpeed = bulletSpeed;
        this.cost = cost;
        this.upgradeTurret = upgradeType;
    }

    public TurretType upgrade() {
        return upgradeTurret;
    }
}
