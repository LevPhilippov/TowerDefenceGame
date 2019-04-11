package lev.filippov.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import lev.filippov.*;
import lev.filippov.Emitters.*;
import lev.filippov.Units.Star16;


public class GameScreen implements Screen {

    private SpriteBatch batch;
    private Vector2 mousePosition;
    private Camera camera;
    private Map map;
    private TurretEmitter turretEmitter;
    private TextureRegion selectedCellTexture;
    private ParticleEmitter particleEmitter;
    private BulletEmitter bulletEmitter;
    private InfoEmitter infoEmitter;
    private int selectedCellX, selectedCellY;
    private BitmapFont scoreFont;
    private Stage stage;
    private float respTime;
    private Star16 star16;
    private MonsterWaveController monsterWaveController;

    private BitmapFont winFont;

    private boolean levelCompleted;

    public GameScreen(SpriteBatch batch, Camera camera) {
        this.batch = batch;
        this.camera = camera;
    }

    //метод вызывается при установке экрана как текущего

    @Override
    public void show() {
        this.map = new Map("level_1.map");
        this.scoreFont = Assets.getInstance().getAssetManager().get("fonts/zorque24.ttf");
        this.selectedCellTexture = Assets.getInstance().getAtlas().findRegion("cursor");
        this.turretEmitter = new TurretEmitter(this);
        this.particleEmitter = new ParticleEmitter();
        this.bulletEmitter = new BulletEmitter(this);
        this.infoEmitter = new InfoEmitter(this);
        this.star16 = new Star16(this);
        this.winFont = Assets.getInstance().getAssetManager().get("fonts/zorque36.ttf");
        this.monsterWaveController = new MonsterWaveController(this);
        RoundLoader.loadRound(this, "level_1.map");
        GUICreator.createGUI(this);
    }

    //getters & setters

    public MonsterWaveController getMonsterWaveController() {
        return monsterWaveController;
    }
    public Star16 getStar16() {
        return star16;
    }
    public TurretEmitter getTurretEmitter() {
        return turretEmitter;
    }
    public SpriteBatch getBatch() {
        return batch;
    }
    public void setSelectedCellX(int selectedCellX) {
        this.selectedCellX = selectedCellX;
    }
    public void setSelectedCellY(int selectedCellY) {
        this.selectedCellY = selectedCellY;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public Map getMap() {
        return map;
    }
    public int getSelectedCellX() {
        return selectedCellX;
    }
    public int getSelectedCellY() {
        return selectedCellY;
    }
    public ParticleEmitter getParticleEmitter() {
        return particleEmitter;
    }
    public MonsterEmitter getMonsterEmitter() {
        return monsterWaveController.getMonsterEmitter();
    }
    public BulletEmitter getBulletEmitter() {
        return bulletEmitter;
    }
    public InfoEmitter getInfoEmitter() {
        return infoEmitter;
    }

    @Override
    public void render(float delta) {
        float dt = Gdx.graphics.getDeltaTime();
        update(dt);
        batch.begin();

        map.render(batch);
        star16.render(batch);
        // рисуем клетку под курсором мыши
        batch.setColor(1, 1, 0, 0.5f);
        batch.draw(selectedCellTexture, selectedCellX * 80, selectedCellY * 80);
        batch.setColor(1, 1, 1, 1);
        //эмиттеры
        turretEmitter.render(batch);
        bulletEmitter.render(batch);
        monsterWaveController.render(batch);
//        monsterEmitter.render(batch);
        particleEmitter.render(batch);
        infoEmitter.render(batch, scoreFont);
        //отрисовка скора
        scoreFont.draw(batch, "Gold:" + star16.getMoney(), 20, 650);
        scoreFont.draw(batch, "Time remains: " + (int)monsterWaveController.getWaveReverseTimer(),120, 700 );
        scoreFont.draw(batch, "Next wave in: " + (int)monsterWaveController.getPauseReverseTimer(),120, 650 );
        scoreFont.draw(batch, "Current wave: " + (int)monsterWaveController.getCurrentWave(),120, 600 );


        batch.end();

        stage.draw();
    }

    public void update(float dt) {
        //создание частицы через particleEmitter
        map.update(dt);
        star16.update(dt);

        //эмиттеры
        turretEmitter.update(dt);
        bulletEmitter.update(dt);
        monsterWaveController.update(dt);
        particleEmitter.update(dt);
        infoEmitter.update(dt);

        CheckCollisonServise.checkCollision(this);

        stage.act(dt);

        monsterWaveController.checkPool();
        bulletEmitter.checkPool();
        particleEmitter.checkPool();
        turretEmitter.checkPool();
        infoEmitter.checkPool();
    }

    public void showWinScreen(){
//        groupTurretAction.setVisible(false);
//        groupTurretSelection.setVisible(false);
//        winScrenGroup.setVisible(true);
    }
    @Override
    public void resize(int width, int height) {
        ScreenManager.getInstance().resize(width, height);
    }
    @Override
    public void pause() {

    }
    @Override
    public void resume() {

    }
    @Override
    public void hide() {

    }
    @Override
    public void dispose() {

    }
}