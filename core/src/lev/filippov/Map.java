package lev.filippov;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.io.BufferedReader;
import java.io.IOException;

public class Map {
    private final int MAP_WIDTH = 16;
    private final int MAP_HEIGHT = 9;

    private final int ELEMENT_GRASS = 0;
    private final int ELEMENT_ROAD = 1;
    private final int ELEMENT_WALL = 2;

    private byte[][] data;
    private TextureRegion textureRegionGrass;
    private TextureRegion textureRegionRoad;
//    private TextureRegion textureRegionCursor;

    private int selectedX = 0;
    private int selectedY = 0;

    public Map(String mapName) {
        data = new byte[MAP_WIDTH][MAP_HEIGHT];
        textureRegionGrass = Assets.getInstance().getAtlas().findRegion("grass");
        textureRegionRoad = Assets.getInstance().getAtlas().findRegion("road");
//        textureRegionCursor = Assets.getInstance().getAtlas().findRegion("cursor");
        loadMapFromFile(mapName);
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
            }
        }
//        batch.setColor(1,1,1,0.4f);
//        batch.draw(textureRegionCursor, selectedX * 80, selectedY * 80);
//        batch.setColor(1,1,1,1);
    }

    public void update(float dt) {
//        if (Gdx.input.justTouched()) {
//            selectedX = Gdx.input.getX() / 80;
//            selectedY = (720 - Gdx.input.getY()) / 80;
//        }
    }

    public void loadMapFromFile(String mapName) {
        this.data = new byte[MAP_WIDTH][MAP_HEIGHT];
        BufferedReader reader = null;
        try {
            reader = Gdx.files.internal("maps/" + mapName).reader(8192);
            for (int i = 0; i < 9; i++) {
                String str = reader.readLine();
                for (int j = 0; j < 16; j++) {
                    char symb = str.charAt(j);
                    if (symb == '1') {
                        data[j][8 - i] = ELEMENT_ROAD;
                    }
                    if (symb=='2'){
                        data[j][8-i] = ELEMENT_WALL;
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
