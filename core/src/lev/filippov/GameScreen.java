package lev.filippov;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private Vector2 mousePosition;
    private Camera camera;
    private Map map;
    private Turret turret;
    private TextureRegion selectedCellTexture;
    private ParticleEmitter particleEmitter;
    private MonsterEmitter monsterEmitter;
    private BulletEmitter bulletEmitter;
    private int selectedCellX, selectedCellY;
    private BitmapFont scoreFont;
    private float respTime;
    String score;


    public GameScreen(SpriteBatch batch, Camera camera) {
        this.batch = batch;
        this.camera = camera;
    }
    //метод вызывается при установке экрана как текущего
    @Override
    public void show() {
        prepare();
        this.particleEmitter = new ParticleEmitter();
        this.monsterEmitter = new MonsterEmitter();
        this.bulletEmitter = new BulletEmitter(this);
        this.map = new Map("level01.map");
        this.turret = new Turret(this);
        this.scoreFont = Assets.getInstance().getAssetManager().get("fonts/zorque24.ttf");
        this.selectedCellTexture = Assets.getInstance().getAtlas().findRegion("cursor");
    }

    public ParticleEmitter getParticleEmitter() {
        return particleEmitter;
    }

    public MonsterEmitter getMonsterEmitter() {
        return monsterEmitter;
    }

    public BulletEmitter getBulletEmitter() {
        return bulletEmitter;
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
        monsterEmitter.render(batch);
        turret.render(batch);
        bulletEmitter.render(batch);

        particleEmitter.render(batch);
        scoreFont.draw(batch, "Score:" + score, 20, 700);
        batch.end();
    }

    public void update(float dt) {
        //создание частицы через particleEmitter
        particleEmitter.setup(640, 360, MathUtils.random(-20.0f, 20.0f), MathUtils.random(20.0f, 80.0f), 0.9f, 1.0f, 0.2f, 1, 0, 0, 1, 1, 1, 0, 1);

        respTime +=dt;

        if(respTime > 3) {
            monsterEmitter.setup(16*80,MathUtils.random(1,9)*80, -1,0, 100);
            respTime=0;
        }

        map.update(dt);
        turret.update(dt);
        particleEmitter.update(dt);
        monsterEmitter.update(dt);
        bulletEmitter.update(dt);


        monsterEmitter.checkPool();
        particleEmitter.checkPool();
        bulletEmitter.checkPool();
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

                if (selectedCellX == ((int) mousePosition.x/80) && selectedCellY == ((int) mousePosition.y/80)) {
                    //здесь неоюходимо передать управление методу, определяющему (по карте объектов) -
                    // какой элемент подсвечен и передать управление соответсвующему методу
                    map.setWall(selectedCellX, selectedCellY);
                }

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
