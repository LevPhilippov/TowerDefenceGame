package lev.filippov;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private Vector2 mousePosition;
    private Camera camera;
    private Map map;
    private Monster monster;
    private Turret turret;
    private TextureRegion selectedCellTexture;
    private ParticleEmitter particleEmitter;
    private int selectedCellX, selectedCellY;

    public Monster getMonster() {
        return monster;
    }

    public GameScreen(SpriteBatch batch, Camera camera) {
        this.batch = batch;
        this.camera = camera;
    }
    //метод вызывается при установке экрана как текущего
    @Override
    public void show() {
        prepare();
        this.particleEmitter = new ParticleEmitter();
        this.map = new Map("level01.map");
        this.monster = new Monster(this);
        this.turret = new Turret(this);
        this.selectedCellTexture = Assets.getInstance().getAtlas().findRegion("cursor");
    }

    @Override
    public void render(float delta) {
        float dt = Gdx.graphics.getDeltaTime();
        update(dt);
        batch.begin();
        map.render(batch);
        // рисуем клетку под курсором мыши
        batch.setColor(1, 1, 0, 0.5f);
        batch.draw(selectedCellTexture, selectedCellX * 80, selectedCellY * 80);
        batch.setColor(1, 1, 1, 1);

        monster.render(batch);
        turret.render(batch);

        particleEmitter.render(batch);
        batch.end();
    }

    public void update(float dt) {
        //создание частицы через particleEmitter
        particleEmitter.setup(640, 360, MathUtils.random(-20.0f, 20.0f), MathUtils.random(20.0f, 80.0f), 0.9f, 1.0f, 0.2f, 1, 0, 0, 1, 1, 1, 0, 1);
        map.update(dt);
        monster.update(dt);
        turret.update(dt);
        particleEmitter.update(dt);
        particleEmitter.checkPool();
    }
    //
    public void prepare() {
        mousePosition = new Vector2(0, 0);
        InputProcessor myProc = new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                mousePosition.set(screenX, screenY);
                // метод переводит координаты экраны в координаты нашего окна
                ScreenManager.getInstance().getViewport().unproject(mousePosition);
                selectedCellX = (int) (mousePosition.x / 80);
                selectedCellY = (int) (mousePosition.y / 80);
                return true;
            }
        };
        // InputMultiplexer im = new InputMultiplexer(stage, myProc);
        Gdx.input.setInputProcessor(myProc);
    }
    //метод вызывается при изменении размера экрана
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
