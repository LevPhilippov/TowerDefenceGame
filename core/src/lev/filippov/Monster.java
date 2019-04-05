package lev.filippov;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import java.util.Stack;

public class Monster implements Poolable {
    private Map map;
    private GameScreen gameScreen;

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
    private Vector2 tempVector2;
    private static final Vector2 NORTH = new Vector2(0,1);
    private static final Vector2 SOUTH = new Vector2(0,-1);
    private static final Vector2 WEST = new Vector2(-1,0);
    private static final Vector2 EAST = new Vector2(1,0);
    private static Vector2[] vArray = new Vector2[]{NORTH, SOUTH, EAST, WEST};

    private static int[] North = new int[]{0,1};
    private static int[] South = new int[]{0,-1};
    private static int[] West = new int[]{-1,0};
    private static int[] East = new int[]{1,0};
    private static int[][] directions = {North,South,West,East};
    private int[] tempCoord;

    private Stack<Vector2> pathStack;
    private int[][] routeMatrix;
    private int mapVersion;
    private Vector2 nextPosition;
    private Vector2 dst;
    private int waveCounter;


    //игровые параметры монстра
    private int hp;
    private int hpMax;
    private int damage;

    public Vector2 getPosition() {
        return position;
    }

    public Monster(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.map = gameScreen.getMap();
        this.texture = Assets.getInstance().getAtlas().findRegion("monster");
        this.textureHp = Assets.getInstance().getAtlas().findRegion("monsterHp");
        //движение
        this.position = new Vector2();
        this.velocity = new Vector2();

        //вспомогательные вектора движения и переменные
        this.nextPosition = new Vector2();
        this.dst = new Vector2();
        this.tempVector2 = new Vector2();
        this.pathStack = new Stack<>();
        this.routeMatrix = new int[map.getMAP_WIDTH()][map.getMAP_HEIGHT()];
        this.mapVersion = map.getVersion();

        this.tempCoord = new int[2];

        //игровые характеристики
        this.hpMax = 100;
        this.hp = this.hpMax;
        this.scale = 1;
        this.costForDestroying = 10;
        this.scoreForDestroying = 100;
        this.damage = 10;

        //хитбокс
        this.hitBox = new Circle(position, 40);
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

//        if(mapVersion != map.getVersion()) {
//            clearMonsterWay();
//            buildRoute();
//            mapVersion = map.getVersion();
//            System.out.println("Rebuild route!");
//        }

//        if(position.dst(nextPosition)<2.0f) {
//            System.out.println("В стэке осталось: " + pathStack.size());
//            System.out.println("pop!: " + pathStack.peek());
//            position.set(nextPosition);
//
//            velocity.set(pathStack.peek()).scl(speed);
//
//            Vector2 temp2 = new Vector2().set(pathStack.pop());
//            temp2.scl(80);
//            nextPosition.set(0,0);
//            nextPosition.add(position);
//            nextPosition.add(temp2);
//        }

        position.mulAdd(velocity, dt);
        hitBox.setPosition(position);

        if (pathStack.isEmpty()) {
            gameScreen.getPlayer().receiveDamage(this);
            deactivate();
        }
    }

    public void init (float x, float y, float vx, float vy, float speed) {
        clearMonsterWay();
        hp = hpMax;
        position.set(x*80,y*80);
        active = true;
        this.speed = speed;
        buildRoute();
        nextPosition.set(position);
        this.velocity.set(1,0).scl(speed);
    }

    public void deactivate(){
        active = false;
        pathStack.clear();
        clearMonsterWay();
    }

    private void clearMonsterWay() {
        //очистка стэка направлений
        pathStack.clear();

        //очистка вспомогательных векторов
        dst.set(0,0);
        tempVector2.set(0,0);

        //очистка матрицы пути
        for (int i = 0; i <routeMatrix.length ; i++) {
            for (int j = 0; j <routeMatrix[0].length ; j++) {
                routeMatrix[i][j]=0;
            }
        }
    }

    public void receiveDamage(int damage) {
        hp -= damage*scale;
        if(hp<=0) {
            //добавляем золота и очков игроку
            gameScreen.getPlayer().addGoldandScoreForMonster(this);
            deactivate();
        }
    }

    private void buildRoute(){
        pathStack.clear();
        buildWaveMatrix();
        printMatrix();
//        defineRoute();
//        System.out.println(pathStack);
    }

    private void printMatrix() {
        for (int i = 0; i <routeMatrix[0].length ; i++) {
            for (int j = 0; j <routeMatrix.length; j++) {
                System.out.print("[" + routeMatrix[j][i] + "]" );
            }
            System.out.println();
        }
    }


    //предположим, что монстр уже на карте
    public void buildWaveMatrix() {
        int tempX = 0;
        int tempY = 0;
        waveCounter = 1;
        //составляем матрицу, пока не достигнем точки назначения
        routeMatrix[(int) position.x / 80][(int) position.y / 80] = waveCounter;
        while (!destPointReached) {
            for (int i = 0; i < map.getMAP_WIDTH(); i++) {
                for (int j = 0; j < map.getMAP_HEIGHT(); j++) {
                    if (routeMatrix[i][j] == waveCounter) {
                        for (int[] dir : directions) {
                            tempX=i+dir[0];
                            tempY=j+dir[1];
                            makeNewWave(waveCounter + 1, tempX, tempY);
                            if (destPointReached){
                                destPointReached=false;
                                return;
                            }
                            //добавить условие, когда нет точки назначения
                        }
                    }
                }
            }
            waveCounter++;
        }
    }

    private void defineRoute() {
        //запоминаем точку назначения
        dst.set(tempVector2);
        //идем назад от последней позиции (складываем вектора направлений в List)
        while (routeMatrix[(int)dst.x][(int)dst.y] != 1) {
            for (Vector2 vector2 : vArray) {
                tempVector2.set(dst).add(vector2);
                if (map.isExist(tempVector2) && routeMatrix[(int) tempVector2.x][(int) tempVector2.y] == waveCounter) {
                    if (vector2 == NORTH)
                        pathStack.push(SOUTH);
                    else if (vector2 == SOUTH)
                        pathStack.push(NORTH);
                    else if (vector2 == EAST)
                        pathStack.push(WEST);
                    else if (vector2 == WEST)
                        pathStack.push(EAST);
                    break;
                }
            }
            dst.set(tempVector2);
            waveCounter--;
        }
    }

    private void makeNewWave(int newWaveNumber, int tempX, int tempY) {
        if(map.isExist(tempX, tempY)) {
            if (map.isEmpty(tempX, tempY) && cellNotInRoute(tempX, tempY)) {
                routeMatrix[tempX][tempY] = newWaveNumber;
            }
            if (map.isDestination(tempX, tempY)) {
                destPointReached = true;
                routeMatrix[tempX][tempY] = -1;
            }
        }
    }

    private boolean cellNotInRoute(int x, int y) {
        return routeMatrix[x][y] == 0;
    }
}
