package lev.filippov.Units;

//финишная точка для монстров
//Например, каждую минуту оно произвольно перемещатся по карте.
//Просто рандомно выбирает точку на карте и телепортируется туда.
// Применять логику перемещения, как для монстров не имеет смысла, т.к. он в теории будет самоубиваться, либо ее нужно менять,
// а этого делать не хочется. Честно говоря, задумка мне не очень нравится, т.к. я не понимаю в этом смысла.

//В этой реализации стар16 вместо "короля". Каждый раунд они менют положения на произвольное.

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lev.filippov.Assets;
import lev.filippov.Screens.GUICreator;
import lev.filippov.Screens.GameScreen;
import lev.filippov.Map;

import java.util.ArrayList;
import java.util.List;

public class Star16 {
    private GameScreen gameScreen;
    private Map map;
    private float maxRoundTime;
    private int hp;
    private int money;
    private byte[][] data;

    private float reverseTimer;
    private float timer;
    private float changePositionRate;
    private TextureRegion star16;
    private float tempTimer;
    private boolean defeatFlag;

    //позиции точек
  //  private List<Vector2> positions;
    private List<StarElement> elements;

    public Star16(GameScreen gameScreen) {
        this.hp = 100;
        this.gameScreen = gameScreen;
        this.map = gameScreen.getMap();
        data = new byte[map.getMAP_WIDTH()][map.getMAP_HEIGHT()];
        this.timer = maxRoundTime;
        this.star16 = Assets.getInstance().getAtlas().findRegion("star16");
 //       this.positions = new LinkedList<>();
        this.elements = new ArrayList<StarElement>();
        this.changePositionRate = 60;
        this.reverseTimer = maxRoundTime;
        this.timer = 0;
        this.money = 300;
        init();
    }

    // элемент класса стар16 представляет собой одну из точек назначения, к которой строится маршрут.
    // обладает собственным ХП, начальая позиция вычисляется согласно расположению элементов DESTINATION_POINT на карте
    // при снижении ХП до 0 элемент исчезает, на карте также исчезает эелемент D_P.
    // * при уничтожении монстры приносят меньше денег

    public class StarElement {
        private int hp;
        private final int maxHP=100;
        private Vector2 position;
        private boolean active;

        public StarElement(Vector2 position) {
            this.hp = maxHP;
            this.position = position;
            this.active = true;
        }

        public int getHp() {
            return hp;
        }

        public Vector2 getPosition() {
            return position;
        }

        public void setPosition(Vector2 position) {
            this.position = position;
        }

        public void addHP(int damage){
            hp-=damage;
        }

        public boolean isActive() {
            return active;
        }
    }

    private void init() {
        for (int i = 0; i < map.getMAP_WIDTH(); i++) {
            for (int j = 0; j < map.getMAP_HEIGHT(); j++) {
                if (map.getData()[i][j] == map.getELEMENT_DESTINATION()) {
                    elements.add(new StarElement(new Vector2(i,j)));
                }
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (StarElement element : elements) {
            batch.draw(star16, element.getPosition().x*80+24, element.getPosition().y*80+24,0,0,16,16,3,3,0);
        }
    }

    public void update (float dt) {
        updateParticles();
        updateElementsState(dt);
    }

    private void updateParticles() {
        for (StarElement element : elements) {
            for (int i = 0; i <6 ; i++) {
                float angle = i*60+ MathUtils.random(-30,30);
                gameScreen.getParticleEmitter().setup(element.getPosition().x*80+40, element.getPosition().y*80+40,
            30* MathUtils.cosDeg(angle), 30*MathUtils.sinDeg(angle), 2,1f, 0.2f,
            1,1,1,1,1,1,1,0);
            }
        }
    }

    public void updateElementsState(float dt) {

        reverseTimer-=dt;
        timer=(int)reverseTimer;
        tempTimer +=dt;

        if(tempTimer >=changePositionRate) {
            changeStarPosition();
            tempTimer = 0;
        }
    }

    public void changeStarPosition() {
        int tempX;
        int tempY;
        for (StarElement element : elements) {
            do {
                tempX = MathUtils.random(0,map.getMAP_WIDTH()-1);
                tempY = MathUtils.random(0, map.getMAP_HEIGHT()-1);
            } while (!map.isEmpty(tempX,tempY) && !isMonstersInCell(tempX,tempY));
            element.getPosition().x = tempX;
            element.getPosition().y = tempY;
        }
    }

    private boolean isMonstersInCell(int cellX, int cellY) {
        for (Monster monster : gameScreen.getMonsterEmitter().getActiveList()) {
            if((int) monster.getPosition().x/80 == cellX && (int) monster.getPosition().y/80 == cellY) {
                return true;
            }
        }
        return false;
    }


    public void addHP(StarElement element, int damage) {
        element.addHP(-damage);
        if (element.hp<=0) {
            hp = 0;
            element.active = false;
            elements.remove(element);
            //проверка поражения
            if(isDefeat()) {
                gameScreen.getMonsterEmitter().killAllMonsters();
                gameScreen.defeatAction();
            }
        }
    }

    public void addMoney(int amount) {
        money+=amount;
    }

    public boolean isMoneyEnough(int amount) {
        if(money>=amount)
            return true;
        gameScreen.getInfoEmitter().setup(gameScreen.getSelectedCellX(),gameScreen.getSelectedCellY(), "Not enough money!");
        return false;
    }

    public int getMoney() {
        return money;
    }

    public List<StarElement> getElements() {
        return elements;
    }

    public boolean isStar16(int cellX, int cellY) {
        for (StarElement element : elements) {
            if(element.position.x == cellX && element.position.y == cellY) {
                return true;
            }
        }
        return false;
    }

    public boolean isDefeat() {
        if (elements.isEmpty()) {
            defeatFlag = true;
            return true;
        }
        return false;
    }

    public boolean isDefeatFlag() {
        return defeatFlag;
    }
}
