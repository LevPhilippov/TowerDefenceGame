package lev.filippov;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.io.BufferedReader;
import java.io.IOException;


public class Map {
    private final int MAP_WIDTH = 16;
    private final int MAP_HEIGHT = 9;
    private String fileName;

    private final int ELEMENT_GRASS = 0;
    private final int ELEMENT_ROAD = 1;
    private final int ELEMENT_WALL = 2;
    private final int ELEMENT_DESTINATION = 5;
    private final int ELEMENT_TURRET = 3;

    private byte[][] data;
    private TextureRegion textureRegionGrass;
    private TextureRegion textureRegionRoad;

    private int version;

    public int getELEMENT_GRASS() {
        return ELEMENT_GRASS;
    }

    public int getELEMENT_TURRET() {
        return ELEMENT_TURRET;
    }

    public Map(String mapName) {
        this.fileName = mapName;
        data = new byte[MAP_WIDTH][MAP_HEIGHT];
        textureRegionGrass = Assets.getInstance().getAtlas().findRegion("grass");
        textureRegionRoad = Assets.getInstance().getAtlas().findRegion("road");
        this.data = new byte[MAP_WIDTH][MAP_HEIGHT];
//        loadMapFromFile(mapName);
    }

    public int getELEMENT_WALL() {
        return ELEMENT_WALL;
    }

    public int getELEMENT_ROAD() {
        return ELEMENT_ROAD;
    }

    public String getFileName() {
        return fileName;
    }

    public int getVersion() {
        return version;
    }

    public void updateMapVersion() {
        this.version++;
    }

    public byte[][] getData() {
        return data;
    }

    public int getMAP_WIDTH() {
        return MAP_WIDTH;
    }

    public int getMAP_HEIGHT() {
        return MAP_HEIGHT;
    }

    public int getELEMENT_DESTINATION() {
        return ELEMENT_DESTINATION;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < MAP_WIDTH; i++) {
            for (int j = 0; j < MAP_HEIGHT; j++) {
                if (data[i][j] == ELEMENT_GRASS) {
                    batch.draw(textureRegionGrass, i * 80, j * 80);
                }
                if (data[i][j] == ELEMENT_ROAD) {
                    batch.draw(textureRegionRoad, i * 80, j * 80);
                }
                if (data[i][j] == ELEMENT_WALL) {
                    batch.setColor(0,0,0,1);
                    batch.draw(textureRegionGrass, i*80, j*80);
                    batch.setColor(1,1,1,1);
                }
                if (data[i][j] == ELEMENT_TURRET) {
                    batch.draw(textureRegionGrass, i * 80, j * 80);
                }
            }
        }
    }

    public void update(float dt) {
    }


    public void setWall(int cx, int cy) {
        data[cx][cy] = ELEMENT_WALL;
        updateMapVersion();
    }

//    public void loadMapFromFile(String mapName) {
//        BufferedReader reader = null;
//        String str;
//        try {
//            reader = Gdx.files.internal("maps/" + mapName).reader(8192);
//            while ((str = reader.readLine()).equals("# map-up"))
//            for (int i = 0; i < 9; i++) {
//                str = reader.readLine();
//                for (int j = 0; j < 16; j++) {
//                    char symb = str.charAt(j);
//                    if (symb == '1') {
//                        data[j][8 - i] = ELEMENT_ROAD;
//                    }
//                    if (symb=='2'){
//                        data[j][8-i] = ELEMENT_WALL;
//                    }
//                }
//            }
//            reader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public boolean isEmpty(int x, int y){
        return data[x][y] == ELEMENT_GRASS || data[x][y] == ELEMENT_ROAD;
    }


    public boolean isExist(int x, int y) {
        return (x >=0 && x<MAP_WIDTH && y>=0 && y<MAP_HEIGHT);
    }
}
