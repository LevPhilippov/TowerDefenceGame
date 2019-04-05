package lev.filippov;

public enum BulletType {
    COMMON(10, false,false,false),
    AUTOAIM(15,true,false,false),
    SPLASH(15,false,false,true),
    FREEZE(10,false,true,false);

    int power;
    boolean autoaim;
    boolean freeze;
    boolean splash;

    BulletType(int power, boolean autoaim, boolean freeze, boolean splash) {
        this.power = power;
        this.autoaim = autoaim;
        this.freeze = freeze;
        this.splash = splash;
    }
}
