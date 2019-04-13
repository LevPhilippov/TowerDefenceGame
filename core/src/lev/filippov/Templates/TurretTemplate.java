package lev.filippov.Templates;

//    # name, imageX, imageY, fireRadius, rotationSpeed(deg), fireRate(time btw shots), cost, upgradedTurretName, bulletName
public class TurretTemplate {
   private String name;
   private int imageX;
   private int imageY;
   private int fireRadius;
   private int rotationSpeed;
   private float fireRate;
   private int cost;
   private String upgradedTurretName;
   private String bulletName;

    public TurretTemplate(String strTemplate) {
        String[] tokens = strTemplate.split(",");
        this.name = tokens[0].trim();
        this.imageX = Integer.parseInt(tokens[1].trim());
        this.imageY = Integer.parseInt(tokens[2].trim());
        this.fireRadius =  Integer.parseInt(tokens[3].trim());
        this.rotationSpeed =  Integer.parseInt(tokens[4].trim());
        this.fireRate =  Float.parseFloat(tokens[5].trim());
        this.cost =  Integer.parseInt(tokens[6].trim());
        this.upgradedTurretName = tokens[7].trim();
        this.bulletName = tokens[8].trim();
    }

    public String getName() {
        return name;
    }

    public int getImageX() {
        return imageX;
    }

    public int getImageY() {
        return imageY;
    }

    public int getFireRadius() {
        return fireRadius;
    }

    public int getRotationSpeed() {
        return rotationSpeed;
    }

    public float getFireRate() {
        return fireRate;
    }

    public int getCost() {
        return cost;
    }

    public String getUpgradedTurretName() {
        return upgradedTurretName;
    }

    public String getBulletName() {
        return bulletName;
    }
}
