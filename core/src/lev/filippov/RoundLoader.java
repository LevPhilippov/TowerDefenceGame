package lev.filippov;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import lev.filippov.Screens.GameScreen;
import lev.filippov.Templates.BulletTemplate;
import lev.filippov.Templates.TurretTemplate;
import lev.filippov.Templates.WaveTemplate;
import lev.filippov.Units.Star16;

import java.io.BufferedReader;
import java.io.IOException;

public class RoundLoader {


    public static void loadRound(GameScreen gameScreen, String mapName) {
        loadMapFromFile(gameScreen, mapName);
        loadWaveTemplates(gameScreen, mapName);
        loadTurretTemplates(gameScreen);
        loadBulletTemplates(gameScreen);

    }

    public static void loadMapFromFile(GameScreen gameScreen, String mapName) {
        BufferedReader reader = null;
        String str;
        try {
            reader = Gdx.files.internal("maps/" + mapName).reader(8192);
            while ((str = reader.readLine()).equals("# map-up"))
                for (int i = 0; i < 9; i++) {
                    str = reader.readLine();
                    for (int j = 0; j < 16; j++) {
                        char symb = str.charAt(j);
                        if (symb == '1') {
                            gameScreen.getMap().getData()[j][8 - i] = (byte)gameScreen.getMap().getELEMENT_ROAD();
                        }
                        if (symb=='2'){
                            gameScreen.getMap().getData()[j][8 - i] = (byte)gameScreen.getMap().getELEMENT_WALL();
                        }
                        if (symb =='5') {
                            gameScreen.getMap().getData()[j][8 - i] = (byte)gameScreen.getMap().getELEMENT_DESTINATION();
                        }
                    }
                }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadWaveTemplates(GameScreen gameScreen, String mapName) {
        BufferedReader reader;
        try{
            reader = Gdx.files.internal("maps/" + mapName).reader(8192);
            String str;

            while (!(str = reader.readLine()).equals("#coordinates-up"));

                for (int i = 0; i < 9; i++) {
                    str = reader.readLine();
                    for (int j = 0; j < 16; j++) {
                        char symb = str.charAt(j);
                        if (symb =='5') {
                            gameScreen.getStar16().getElements().add(gameScreen.getStar16().new StarElement(new Vector2(j,8-i)));
                        }
                    }
                }

            while (!(str = reader.readLine()).equals("# waves-up"));

            while (!(str = reader.readLine()).equals("# waves-down")){
                WaveTemplate template = new WaveTemplate(str);
                gameScreen.getMonsterWaveController().getWaveTemplatesList().addLast(template);
            }

            while (!(str = reader.readLine()).equals("# monsterRespCells-up"));

            str = reader.readLine();

            for (WaveTemplate waveTemplate : gameScreen.getMonsterWaveController().getWaveTemplatesList()) {
                waveTemplate.addMonsterRespawnCells(str);
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadTurretTemplates(GameScreen gameScreen) {
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
                    gameScreen.getTurretEmitter().getTurretTemplates().put(template.getName(), template);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadBulletTemplates(GameScreen gameScreen) {
            BufferedReader reader;
            try{
                reader = Gdx.files.internal("armory.dat").reader(8192);
                String str;
                while (!(str = reader.readLine()).equals("# bullets-up"));

                while (!(str = reader.readLine()).equals("# bullets-down")) {
                    BulletTemplate template = new BulletTemplate(str);
                    gameScreen.getBulletEmitter().getBulletTemplates().put(template.getName(), template);

                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

}
