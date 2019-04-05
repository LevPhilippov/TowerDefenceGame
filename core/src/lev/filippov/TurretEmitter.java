package lev.filippov;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TurretEmitter extends ObjectPool<Turret> {
    private GameScreen gameScreen;
    private TextureRegion[][] allTextures;

    public TurretEmitter(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.allTextures = Assets.getInstance().getAtlas().findRegion("turrets").split(80,80);
    }

    @Override
    protected Turret newObject() {
        return new Turret(gameScreen, allTextures);
    }

    public boolean setup(int cellX, int cellY, TurretType type) {
        if (!canIDeployItHere(cellX, cellY)) {
       //     gameScreen.getInfoEmitter().setup(cellX,cellY, "Can't be deployed here!");
            return false;
        }

        Turret turret = getActiveElement();
        turret.init(cellX, cellY, type);
        gameScreen.getPlayer().addMoney(-turret.getCost());
        gameScreen.getMap().deployElementInMap(cellX,cellY,gameScreen.getMap().getELEMENT_TURRET());
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
        //проверка условия: пуста ли клетка?
        if (!gameScreen.getMap().isEmpty(cellX, cellY)) {
            gameScreen.getInfoEmitter().setup(cellX,cellY,"Cell isn't empty!");
            return false;
        }
        //проверка условия: усли мобы в клетке?
        if (isMonstersInCell(cellX, cellY)) {
            gameScreen.getInfoEmitter().setup(cellX,cellY, "You can't deploy tower upon monsters!");
            return false;
        }
        return true;
    }

    //проверим условие, есть ли монстры в клетке, в которой мы хотим разместить турель
    private boolean isMonstersInCell(int cellX, int cellY) {
        for (Monster monster : gameScreen.getMonsterEmitter().getActiveList()) {
            if((int) monster.getPosition().x/80 == cellX && (int) monster.getPosition().y/80 == cellY) {
                return true;
            }
        }
        return false;
    }

    public boolean destroyTurret(int cellX, int cellY) {
        Turret t = findTurret(cellX, cellY);
        if(isTurretExist(t)) {
            gameScreen.getPlayer().addMoney(t.getCost()/2);
            t.deactivate();
            gameScreen.getMap().deployElementInMap(cellX,cellY,gameScreen.getMap().getELEMENT_GRASS());
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

    private boolean isTurretExist(Turret t) {
        if(t != null) {
            return true;
        }
        gameScreen.getInfoEmitter().setup(gameScreen.getSelectedCellX(), gameScreen.getSelectedCellY(), "Turret is not exist!");
        return false;
    }

    private boolean isPossibleToUpgrade(Turret t) {
       if(t.getType().upgradeTurret !=null) {
           return true;
       }
        System.out.println("Максимальный апгрейд!");
        gameScreen.getInfoEmitter().setup(gameScreen.getSelectedCellX(), gameScreen.getSelectedCellY(), "Already upgraded!");
           return false;
    }

    public void setTurret(int cellX, int cellY, TurretType type) {
        if (gameScreen.getPlayer().isMoneyEnough(50)) {
            if (setup(cellX, cellY, type)) {
                gameScreen.getPlayer().addMoney(-50);
            }
        }
    }

}
