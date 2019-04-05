package lev.filippov;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;
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

    private Vector2 tempVector;
    private Vector2 nextPosition;
    private Vector2 dst;

    private static int[] North = new int[]{0,1};
    private static int[] South = new int[]{0,-1};
    private static int[] West = new int[]{-1,0};
    private static int[] East = new int[]{1,0};
    private static int[][] directions = {North,South,West,East};
    private static int[][][] coordMatrix;
    private Stack <int[]> path;
    private int[] tempDstCell;

    private int[][] routeMatrix;
    private int mapVersion;
    private int waveCounter;


    //игровые параметры монстра
    private int hp;
    private int hpMax;
    private int damage;

    static {
        coordMatrix = new int[16][9][2];
        // заполнение матрицы координат
        for (int i = 0; i <16; i++) {
            for (int j = 0; j <9; j++) {
                int[] temp = new int[] {i,j};
                coordMatrix[i][j]=temp;
            }
        }
    }

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
        this.tempVector = new Vector2();
        this.tempDstCell = new int[2];

        this.routeMatrix = new int[map.getMAP_WIDTH()][map.getMAP_HEIGHT()];
        this.mapVersion = map.getVersion();

        this.path = new Stack<>();


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

    public void update(float dt) {

        if(mapVersion != map.getVersion()) {
            clearMonsterWay();
            buildRoute();
            mapVersion = map.getVersion();
            System.out.println("Rebuild route!");
        }

        if(position.dst(nextPosition)<2.0f) {
            System.out.println("В стэке осталось: " + path.size());
            tempDstCell = path.pop();
            System.out.println("Следующая клетка: " + Arrays.toString(tempDstCell));
            nextPosition.set(tempDstCell[0]*80+40, tempDstCell[1]*80+40);

            tempVector.set(nextPosition).sub(position).nor();
            velocity.set(tempVector).scl(speed);
        }

        position.mulAdd(velocity, dt);
        hitBox.setPosition(position);

        if(path.isEmpty()){
            deactivate();
            gameScreen.getPlayer().receiveDamage(damage);
            clearMonsterWay();
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x-40, position.y-40);
        batch.draw(textureHp, position.x-28, position.y+32, 56 * ((float)hp / hpMax), 6);
    }

    public Circle getHitBox() {
        return hitBox;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public int getDamage() {
        return damage;
    }

    public void init (float x, float y, float vx, float vy, float speed) {
        clearMonsterWay();
        hp = hpMax;
        position.set(x*80+40,y*80+40);
        active = true;
        this.speed = speed;
        buildRoute();
    }

    public void deactivate(){
        active = false;
    }

    private void clearMonsterWay() {
        //очистка стэка координат
        path.clear();
        //очистка вспомогательных векторов
        dst.set(0,0);
        tempVector.set(0,0);

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
            gameScreen.getPlayer().addMoney(costForDestroying);
            gameScreen.getPlayer().addScore(scoreForDestroying);
            deactivate();
        }
    }

    private void buildRoute(){
        clearMonsterWay();
        buildWaveMatrix();
        printMatrixAndRoute();
        nextPosition.set(position);
    }

    private void printMatrixAndRoute() {
        System.out.println("Matrix");
        for (int i = 0; i <routeMatrix[0].length ; i++) {
            for (int j = 0; j <routeMatrix.length; j++) {
                System.out.print("[" + routeMatrix[j][i] + "]" );
            }
            System.out.println();
        }
        System.out.println("Route:");
        for (int[] ints : path) {
            System.out.print("[" + Arrays.toString(ints)+ "]");
        }

    }


    //предположим, что монстр уже на карте
    public void buildWaveMatrix() {
        int tempX;
        int tempY;
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
                                defineRoute(tempX, tempY, waveCounter);
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

    private void defineRoute(int dstX, int dstY, int waveCounter) {
        //запоминаем точку назначения
        int tempX=dstX;
        int tempY=dstY;
        path.push(coordMatrix[tempX][tempY]);
        //идем назад от последней позиции (складываем вектора направлений в Стэк)

        while (routeMatrix[tempX][tempY] != 2) {
            for (int[] dir : directions) {
                tempX = dstX+dir[0];
                tempY = dstY+dir[1];
                if (map.isExist(tempX, tempY) && routeMatrix[tempX][tempY] == waveCounter) {
                    path.push(coordMatrix[tempX][tempY]);
                    break;
                }
            }
            dstX = tempX;
            dstY = tempY;
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
