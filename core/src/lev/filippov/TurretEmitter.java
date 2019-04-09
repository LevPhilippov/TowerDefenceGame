package lev.filippov;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class TurretEmitter extends ObjectPool<Turret> {
    private GameScreen gameScreen;
    private TextureRegion[][] allTextures;
    private HashMap<String, TurretTemplate> turretTemplates;

    public TurretEmitter(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.allTextures = Assets.getInstance().getAtlas().findRegion("turrets").split(80,80);
        this.turretTemplates = new HashMap<String, TurretTemplate>();
        loadTemplates();
    }

    private void loadTemplates() {
        BufferedReader reader;
        try{
            reader = Gdx.files.internal("armory.dat").reader(8192);
            String str;
            Boolean read=false;
            while ((str = reader.readLine())!=null) {
                if(str.equals("# turrets-down"))
                    break;
                if(str.equals("# turrets-up")) {
                    read = true;
                    continue;
                }
                if(read) {
                    TurretTemplate template = new TurretTemplate(str);
                    turretTemplates.put(template.getName(), template);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Turret newObject() {
        return new Turret(gameScreen, allTextures);
    }

//    public boolean setup(int cellX, int cellY, String turretTemplateName) {
//        if (!canIDeployItHere(cellX, cellY)) {
//            return false;
//        }
//
//        Turret turret = getActiveElement();
//        turret.init(cellX, cellY, turretTemplates.get(turretTemplateName));
//        gameScreen.getMap().deployElementInMap(cellX,cellY,gameScreen.getMap().getELEMENT_TURRET());
//        return true;
//    }

    public void setup (int cellX, int cellY, String turretTemplateName) {
        TurretTemplate template = turretTemplates.get(turretTemplateName);
        if (gameScreen.getStar16().isMoneyEnough(template.getCost()) && canIDeployItHere(cellX,cellY)) {
            Turret turret = getActiveElement();
            turret.init(cellX, cellY, template);
            gameScreen.getMap().deployElementInMap(cellX,cellY,gameScreen.getMap().getELEMENT_TURRET());
            gameScreen.getStar16().addMoney(-template.getCost());
            gameScreen.getInfoEmitter().setup(cellX,cellY,"-" + template.getCost());
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
            gameScreen.getStar16().addMoney(t.getTurretCost()/2);
            t.deactivate();
            gameScreen.getMap().deployElementInMap(cellX,cellY,gameScreen.getMap().getELEMENT_GRASS());
            return true;
        }
        return false;
    }

    public boolean upgradeTurret(int cellX, int cellY) {
        Turret t = findTurret(cellX, cellY);
        if (isTurretExist(t) && isPossibleToUpgrade(t)) {
            TurretTemplate upgradedTurretTemplate = turretTemplates.get(t.getTurretTemplate().getUpgradedTurretName());
            if(gameScreen.getStar16().isMoneyEnough(upgradedTurretTemplate.getCost())) {
                t.init(cellX, cellY, upgradedTurretTemplate);
                gameScreen.getStar16().addMoney(-upgradedTurretTemplate.getCost());
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
       if(!t.getTurretTemplate().getUpgradedTurretName().equals("-")) {
           return true;
       }
        System.out.println("Максимальный апгрейд!");
        gameScreen.getInfoEmitter().setup(gameScreen.getSelectedCellX(), gameScreen.getSelectedCellY(), "Already upgraded!");
        return false;
    }



}
