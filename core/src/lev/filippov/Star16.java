package lev.filippov;

//финишная точка для монстров
//Например, каждую минуту оно произвольно перемещатся по карте.
//Просто рандомно выбирает точку на карте и телепортируется туда.
// Применять логику перемещения, как для монстров не имеет смысла, т.к. он в теории будет самоубиваться, либо ее нужно менять,
// а этого делать не хочется. Честно говоря, задумка мне не очень нравится, т.к. я не понимаю в этом смысла.

//В этой реализации стар16 вместо "короля". Каждые X секунд они менют положения на произвольное.

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.LinkedList;
import java.util.List;

public class Star16 {
    private GameScreen gameScreen;
    private Map map;
    private float maxRoundTime;

    public float getTimer() {
        return timer;
    }

    private float reverseTimer;
    private float timer;
    private float changePositionRate;
    private TextureRegion star16;
    private float tempTimer;
    private boolean finished;

    //позиции точек
    private List<Vector2> positions;

    public Star16(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.map = gameScreen.getMap();
        this.maxRoundTime = 180f;
        this.timer = maxRoundTime;
        this.star16 = Assets.getInstance().getAtlas().findRegion("star16");
        this.positions = new LinkedList<>();
        this.changePositionRate = 60;
        this.reverseTimer = maxRoundTime;
        this.timer = 0;
        init();
    }

    private void init() {
        for (int i = 0; i < map.getMAP_WIDTH(); i++) {
            for (int j = 0; j < map.getMAP_HEIGHT(); j++) {
                if (map.getData()[i][j] == map.getELEMENT_DESTINATION()) {
                    positions.add(new Vector2(i,j));
                }
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (Vector2 position : positions) {
            batch.draw(star16, position.x*80+24, position.y*80+24,0,0,16,16,3,3,0);
        }
    }

    public void update (float dt) {
        float angle;
        for (Vector2 position : positions) {
            for (int i = 0; i <6 ; i++) {
                angle = i*60+MathUtils.random(-30,30);
                gameScreen.getParticleEmitter().setup(position.x*80+40, position.y*80+40,
            30* MathUtils.cosDeg(angle), 30*MathUtils.sinDeg(angle), 2,1f, 0.2f,
            1,1,1,1,1,1,1,0);
            }
        }
        updateState(dt);
    }

    public void updateState(float dt) {
        if(reverseTimer<=0 && !finished){
            finished = true;
            finishRound();
        }

        reverseTimer-=dt;
        timer=(int)reverseTimer;
        tempTimer +=dt;

        if(tempTimer >=changePositionRate) {
            changeStarPosition();
            tempTimer = 0;
        }
    }

    private void changeStarPosition() {
        int tempX;
        int tempY;
        for (Vector2 position : positions) {
            do {
                tempX = MathUtils.random(0,map.getMAP_WIDTH()-2);
                tempY = MathUtils.random(0, map.getMAP_HEIGHT()-1);

            } while (!map.isEmpty(tempX,tempY) && !isMonstersInCell(tempX,tempY));

            map.deployElementInMap((int)position.x, (int)position.y,map.getELEMENT_GRASS());
            position.x = tempX;
            position.y = tempY;
            map.deployElementInMap((int)position.x, (int)position.y,map.getELEMENT_DESTINATION());
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

    public void finishRound() {
        gameScreen.getPlayer().roundCompleted();
    }
}
