package lev.filippov;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class MonsterWaveController {

    private int currentWave;
    private float pauseBetweenWaves; //sec
    private float monsterRespawnTimer;
    private float pauseReverseTimer;
    private float waveReverseTimer;
    private HashMap<Integer, WaveTemplate> waveTemplates;
    private LinkedList<WaveTemplate> waveTemplatesList;
    private WaveTemplate currentTemplate;
    private String mapName;
    private GameScreen gameScreen;
    private boolean waveIsActive;
    private MonsterEmitter monsterEmitter;

    public MonsterWaveController(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.monsterEmitter = new MonsterEmitter(gameScreen);
        this.waveTemplates = new HashMap<Integer, WaveTemplate>();
        this.waveTemplatesList = new LinkedList<WaveTemplate>();
        this.mapName = gameScreen.getMap().getFileName();
        loadTemplates();
        this.pauseBetweenWaves = 10f;
    }

    public void render (SpriteBatch batch) {
        monsterEmitter.render(batch);
    }

    public void update(float dt) {
        setupMonsters(dt);
        monsterEmitter.update(dt);
    }

    private void loadTemplates() {
        BufferedReader reader;
        try{
            reader = Gdx.files.internal("maps/" + mapName).reader(8192);
            String str;
            Boolean read=false;

            do{
                str = reader.readLine();

            } while (!str.equals("# waves-up"));

            while (!(str = reader.readLine()).equals("# waves-down")){
                WaveTemplate template = new WaveTemplate(str);
                waveTemplatesList.addLast(template);
                waveTemplates.put(template.getWaveNumber(), template);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupMonsters(float dt) {
//        timer+=dt;
        monsterRespawnTimer += dt;


        // пауза между волнами
        if (!waveIsActive) {
            pauseReverseTimer -=dt;
            if(pauseReverseTimer <0) {
                waveIsActive = true;
                pauseReverseTimer = pauseBetweenWaves;
            }
            return;
        }
        //
        if(waveReverseTimer<=0) {
            waveIsActive = false;
            if (waveTemplatesList.isEmpty()) {
                System.out.println("Успех!");
                //уровень закончился, монстры взрываются, король всех убивает и т.д., затем идет переход на другой уровень.
                return;
            }
            currentTemplate = waveTemplatesList.removeFirst();
            waveReverseTimer = currentTemplate.getDuration();
            this.currentWave = currentTemplate.getWaveNumber();
        }

        // респ монстров
        if(waveIsActive){
            waveReverseTimer -= dt;
            if(monsterRespawnTimer > currentTemplate.getRate()) {
                monsterEmitter.setup(MathUtils.random(7,10), MathUtils.random(3,5), currentTemplate.getMonsterHP());
                monsterRespawnTimer = 0;
            }
        }
    }

    public MonsterEmitter getMonsterEmitter() {
        return monsterEmitter;
    }

    public void checkPool() {
        monsterEmitter.checkPool();
    }

    public float getPauseReverseTimer() {
        return pauseReverseTimer;
    }

    public float getWaveReverseTimer() {
        return waveReverseTimer;
    }

    public int getCurrentWave() {
        return currentWave;
    }
}
