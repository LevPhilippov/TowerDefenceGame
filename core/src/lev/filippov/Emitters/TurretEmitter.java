package lev.filippov.Emitters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lev.filippov.*;
import lev.filippov.Screens.GameScreen;
import lev.filippov.Templates.TurretTemplate;
import lev.filippov.Units.Monster;
import lev.filippov.Units.Turret;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class TurretEmitter extends ObjectPool<Turret> {
    private GameScreen gameScreen;
    private TextureRegion[][] allTextures;
    private HashMap<String, TurretTemplate> turretTemplates;
    private byte[][] turretsMap;

    public TurretEmitter(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.allTextures = Assets.getInstance().getAtlas().findRegion("turrets").split(80,80);
        this.turretTemplates = new HashMap<String, TurretTemplate>();
        this.turretsMap = new byte[gameScreen.getMap().getMAP_WIDTH()][gameScreen.getMap().getMAP_HEIGHT()];
    }

    @Override
    protected Turret newObject() {
        return new Turret(gameScreen, allTextures);
    }

    public void setup (int cellX, int cellY, String turretTemplateName) {
        TurretTemplate template = turretTemplates.get(turretTemplateName);
        if (gameScreen.getStar16().isMoneyEnough(template.getCost()) && canIDeployItHere(cellX,cellY)) {
            Turret turret = getActiveElement();
            turret.init(cellX, cellY, template);
            turretsMap[cellX][cellY] = 1; // добавляем в карту турелей
            gameScreen.getStar16().addMoney(-template.getCost());
            gameScreen.getInfoEmitter().setup(cellX,cellY,"-" + template.getCost());
            gameScreen.getMap().updateMapVersion();
        }
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
        //проверка условия: пуста ли клетка? На дороге строить нельзя, поэтому
        if (gameScreen.getMap().isCellGrass(cellX,cellY) && isTurretInCell(cellX,cellY)) {
            gameScreen.getInfoEmitter().setup(cellX,cellY,"You can't build here!");
            return false;
        }
        //проверка условия: если мобы в клетке?
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
        if(!isTurretExist(cellX,cellY)) {
            return false;
        }

        Turret t = findTurret(cellX, cellY);

        gameScreen.getStar16().addMoney(t.getTurretCost()/2);
        t.deactivate();
        //убираем с карты турелей
        turretsMap[cellX][cellY] = 0;
        gameScreen.getMap().updateMapVersion();
        return true;
}

    public boolean upgradeTurret(int cellX, int cellY) {
        if(!isTurretExist(cellX,cellY)) {
            return false;
        }
        Turret t = findTurret(cellX, cellY);

        if(!isPossibleToUpgrade(t)){
            return false;
        }
        TurretTemplate upgradedTurretTemplate = turretTemplates.get(t.getTurretTemplate().getUpgradedTurretName());

        if(!gameScreen.getStar16().isMoneyEnough(upgradedTurretTemplate.getCost())) {
            return false;
        }

        t.init(cellX, cellY, upgradedTurretTemplate);
        gameScreen.getStar16().addMoney(-upgradedTurretTemplate.getCost());
        return true;
    }

    private Turret findTurret (int cellX, int cellY) {
        for (Turret t : activeList) {
            if (t.getCellX() == cellX && t.getCellY() == cellY) {
                return t;
            }
        }
        return null;
    }

    private boolean isTurretExist(int cellX, int cellY) {
        if(turretsMap[cellX][cellY]==1) {
            return true;
        }

        gameScreen.getInfoEmitter().setup(gameScreen.getSelectedCellX(), gameScreen.getSelectedCellY(), "Turret is not exist!");
        return false;
    }

    private boolean isPossibleToUpgrade(Turret t) {
       if(!t.getTurretTemplate().getUpgradedTurretName().equals("-")) {
           return true;
       }
        gameScreen.getInfoEmitter().setup(gameScreen.getSelectedCellX(), gameScreen.getSelectedCellY(), "Already upgraded!");
        return false;
    }

    public boolean isTurretInCell(int cellX, int cellY) {
        return turretsMap[cellX][cellY] == 1;
    }

    public HashMap<String, TurretTemplate> getTurretTemplates() {
        return turretTemplates;
    }
}
