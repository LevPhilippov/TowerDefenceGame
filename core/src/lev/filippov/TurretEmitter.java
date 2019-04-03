package lev.filippov;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TurretEmitter extends ObjectPool<Turret> {
    private GameScreen gameScreen;
    private final String noTurretHere = "No turret here";
    private final String notPossibleToUpgrade = "Not possible to upgrade!";

    public TurretEmitter(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    protected Turret newObject() {
        return new Turret(gameScreen);
    }

    public boolean setup(int cellX, int cellY, TurretType type) {
        if (!canIDeployItHere(cellX, cellY)) {
            return false;
        }
        Turret turret = getActiveElement();
        turret.init(cellX, cellY, type);
        return true;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
    }

    public boolean canIDeployItHere(int cellX, int cellY) {
        if (!gameScreen.getMap().isEmpty(cellX, cellY)) {
            return false;
        }
        //проверка на предмет уже установленной турели (позже заменить - будет модификация карты)
        for (int i = 0; i < activeList.size(); i++) {
            Turret t = activeList.get(i);
            if (isTurretExist(cellX, cellY)) {
                return false;
            }
        }
        return true;
    }

    public boolean destroyTurret(int cellX, int cellY) {
        Turret t = findTurret(cellX, cellY);
        if(isTurretExist(t)) {
            gameScreen.getPlayer().addMoney(t.getCost()/2);
            t.deactivate();
            return true;
        }
        return false;
    }

    public boolean upgradeTurret(int cellX, int cellY) {
        Turret t = findTurret(cellX, cellY);
        if (isTurretExist(t) && isPossibleToUpgrade(t)) {
            if(gameScreen.getPlayer().isMoneyEnough(t.getCost())) {
                t.upgrade();
                gameScreen.getPlayer().spendMoney(t.getCost());
            }
        }
            return false;
    }

    private Turret findTurret (int cellX, int cellY) {
        for (Turret t : activeList) {
            if (t.getCellX() == cellX && t.getCellY() == cellY) {
                return t;
            }
        }
        return null;
    }

    private boolean isTurretExist (int cellX, int cellY) {
        if(findTurret(cellX, cellY) != null) {
            return true;
        }
            gameScreen.setTransparence(noTurretHere);
            return false;
    }

    private boolean isTurretExist(Turret t) {
        if(t != null) {
            return true;
        }
        gameScreen.setTransparence(noTurretHere);
        return false;
    }

    private boolean isPossibleToUpgrade(Turret t) {
       if(t.getType().isPossibleToUpgrade()) {
           return true;
       }
           gameScreen.setTransparence(notPossibleToUpgrade);
           return false;
    }
}
