package lev.filippov;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Monster implements Poolable {
    private Map map;

    private TextureRegion texture;
    private TextureRegion textureHp;
    private Vector2 position;
    private Vector2 velocity;
    private float speed = 100;
    private boolean active;
    private Circle hitBox;
    private int scale;
    private int costForDestroying;
    private int scoreForDestroying;
    //вектора направлений и построение маршрута
    private boolean destPointReached;
    private Vector2 temp;
    private final Vector2 NORTH = new Vector2(0,1);
    private final Vector2 SOUTH = new Vector2(0,-1);
    private final Vector2 WEST = new Vector2(-1,0);
    private final Vector2 EAST = new Vector2(1,0);
    private Vector2[] vArray;
    private List<Vector2> path;
    private int[][] routeMatrix;
    int mapVersion;
    boolean routeIsActual;
    Vector2 nextPosition;
    int currentposition;


    //игровые параметры монстра
    private int hp;
    private int hpMax;
    private int damage;

    public Vector2 getPosition() {
        return position;
    }

    public Monster(Map map) {
        this.map = map;
        this.texture = Assets.getInstance().getAtlas().findRegion("monster");
        this.textureHp = Assets.getInstance().getAtlas().findRegion("monsterHp");
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.temp= new Vector2();
        this.hpMax = 100;
        this.hp = this.hpMax;
        this.hitBox = new Circle(position, 40);
        this.scale = 1;
        this.costForDestroying = 10;
        this.scoreForDestroying = 100;
        this.damage = 10;
        this.path = new ArrayList<>();
        this.routeMatrix = new int[map.getMAP_WIDTH()][map.getMAP_HEIGHT()];
        this.vArray = new Vector2[]{NORTH, SOUTH, EAST, WEST};
        this.mapVersion = map.getVersion();
        this.nextPosition = new Vector2();
    }

    public int getCostForDestroying() {
        return costForDestroying;
    }

    public int getScoreForDestroying() {
        return scoreForDestroying;
    }

    public Circle getHitBox() {
        return hitBox;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x-40, position.y-40);
        batch.draw(textureHp, position.x-40+12, position.y-40+70, 56 * ((float)hp / hpMax), 12);
    }

    public int getDamage() {
        return damage;
    }

    public void update(float dt) {
        if(mapVersion != map.getVersion()) {
            buildRoute();
            mapVersion = map.getVersion();
            System.out.println("Rebuild route!");
            currentposition= path.size();
        }

        if (position.dst(nextPosition) < 2.0f){
            currentposition--;
            position.set(nextPosition);
            nextPosition.set(path.get(currentposition)).scl(80).add(position);
            System.out.println("Обновление вектора скорости!");
            velocity.set(path.get(currentposition)).scl(speed);
        }


        position.mulAdd(velocity, dt);
        hitBox.setPosition(position);

        if (currentposition<0) {
            Player.getInstance().receiveDamage(this);
            deactivate();
        }
    }

    public void init (float x, float y, float vx, float vy, float speed) {
       position.set(x*80,y*80);
       active = true;
       this.speed = speed;
       buildRoute();
       nextPosition.set(position);
        currentposition= path.size();
    }

    public void deactivate(){
        active = false;
        hp = hpMax;
    }

    public void receiveDamage(int damage) {
        hp -= damage*scale;
        if(hp<=0) {
            //добавляем золота и очков игроку
            Player.getInstance().addGoldandScore(this);
            deactivate();
        }
    }

    private void buildRoute(){
        path.clear();
     int waveCounter= buildWaveMatrix();
        defineRoute(waveCounter);
        System.out.println(path);
    }


    //предположим, что монстр уже на карте
    public int buildWaveMatrix() {
        int waveCounter=1;
        //составляем матрицу, пока не достигнем точки назначения
        routeMatrix[(int) position.x / 80][(int) position.y / 80] = waveCounter;
        while (!destPointReached) {
            for (int i = 0; i < map.getMAP_WIDTH(); i++) {
                for (int j = 0; j < map.getMAP_HEIGHT(); j++) {
                    if (routeMatrix[i][j] == waveCounter) {
                        for (Vector2 vector2 : vArray) {
                            temp.set(i, j).add(vector2);
                            makeNewWave(waveCounter + 1, temp);
                            if (destPointReached)
                                return waveCounter;
                        }
                    }
                }
            }
            waveCounter++;
        }
        return 0;
    }

    private void defineRoute(int waveCounter) {
        //запоминаем точку назначения
        Vector2 dst = new Vector2().set(temp);
        //идем назад от последней позиции (складываем вектора направлений в List)
        while (routeMatrix[(int) dst.x][(int) dst.y] != 1) {
            for (Vector2 vector2 : vArray) {
                temp.set(dst);
                temp.add(vector2);
                if (map.isExist(temp) && routeMatrix[(int) temp.x][(int) temp.y] == waveCounter) {
                    if (vector2 == NORTH)
                        path.add(SOUTH);
                    else if (vector2 == SOUTH)
                        path.add(NORTH);
                    else if (vector2 == EAST)
                        path.add(WEST);
                    else if (vector2 == WEST)
                        path.add(EAST);
                    break;
                }
            }
            dst.set(temp);
            waveCounter--;
        }
    }

    private void makeNewWave(int newWaveNumber, Vector2 temp) {
        int tempX = (int)temp.x;
        int tempY = (int)temp.y;
        if (map.isExist(tempX, tempY) && map.isEmpty(tempX, tempY) && routeMatrix[tempX][tempY] == 0
                || map.isExist(tempX, tempY) && map.isDestination(tempX, tempY)) {
            routeMatrix[tempX][tempY] = newWaveNumber;
            destPointReached = map.isDestination(tempX, tempY);
        }
    }
}
