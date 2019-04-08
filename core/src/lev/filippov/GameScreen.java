package lev.filippov;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class GameScreen implements Screen {

    private SpriteBatch batch;
    private Vector2 mousePosition;
    private Camera camera;
    private Map map;
    private TurretEmitter turretEmitter;
    private TextureRegion selectedCellTexture;
    private ParticleEmitter particleEmitter;
    private MonsterEmitter monsterEmitter;
    private BulletEmitter bulletEmitter;
    private InfoEmitter infoEmitter;
    private int selectedCellX, selectedCellY;
    private BitmapFont scoreFont;
    private Stage stage;
    private Player player;
    private float respTime;
    private Star16 star16;
    private BitmapFont winFont;


    //GUI
    private Group groupTurretAction;
    private Group groupTurretSelection;
    private Group winScrenGroup;


    public GameScreen(SpriteBatch batch, Camera camera) {
        this.batch = batch;
        this.camera = camera;
    }

    //метод вызывается при установке экрана как текущего
    @Override
    public void show() {
        this.player = ScreenManager.getInstance().getPlayer();
        this.map = new Map("level_"+ player.getRoundProgress()+ ".map");
        this.scoreFont = Assets.getInstance().getAssetManager().get("fonts/zorque24.ttf");
        this.selectedCellTexture = Assets.getInstance().getAtlas().findRegion("cursor");
        this.turretEmitter = new TurretEmitter(this);
        this.particleEmitter = new ParticleEmitter();
        this.monsterEmitter = new MonsterEmitter(this);
        this.bulletEmitter = new BulletEmitter(this);
        this.infoEmitter = new InfoEmitter(this);
        this.star16 = new Star16(this);
        this.winFont = Assets.getInstance().getAssetManager().get("fonts/zorque36.ttf");
        createGUI();
    }
    public void createGUI() {
        stage = new Stage(ScreenManager.getInstance().getViewport(), batch);

        InputProcessor myProc = new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                mousePosition = new Vector2();
                mousePosition.set(screenX, screenY);
                ScreenManager.getInstance().getViewport().unproject(mousePosition);

                //установка стены на карте
//                if (selectedCellX == (int) (mousePosition.x / 80) && selectedCellY == (int) (mousePosition.y / 80)) {
//                    map.setWall((int) (mousePosition.x / 80), (int) (mousePosition.y / 80));
//                }

                selectedCellX = (int) (mousePosition.x / 80);
                selectedCellY = (int) (mousePosition.y / 80);
                return true;
            }
        };

        InputMultiplexer inputMultiplexer = new InputMultiplexer(stage, myProc);
        Gdx.input.setInputProcessor(inputMultiplexer);

        //шкура для кнопок
        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        TextButton.TextButtonStyle winStyle = new TextButton.TextButtonStyle();

        textButtonStyle.up = skin.getDrawable("shortButton");
        textButtonStyle.font = scoreFont;

        winStyle.up = skin.getDrawable("simpleButton");
        winStyle.font = winFont;
        skin.add("simpleSkin", textButtonStyle);
        skin.add("winSkin", winStyle);




        //подготовка групп кнопок
        //кнопки группы "действие" (первичная)
        groupTurretAction = new Group();
        groupTurretAction.setPosition(400, 600);
        //создание кнопок
        Button btnSetTurret = new TextButton("Set", skin, "simpleSkin");
        Button btnUpgradeTurret = new TextButton("Upg", skin, "simpleSkin");
        Button btnDestroyTurret = new TextButton("Dst", skin, "simpleSkin");
        //разменение внутри Stage
        btnSetTurret.setPosition(10, 10);
        btnUpgradeTurret.setPosition(110, 10);
        btnDestroyTurret.setPosition(210, 10);
        groupTurretAction.addActor(btnSetTurret);
        groupTurretAction.addActor(btnUpgradeTurret);
        groupTurretAction.addActor(btnDestroyTurret);

        //кнопки группы "установка" (дочерняя)
        groupTurretSelection = new Group();
        groupTurretSelection.setVisible(false);
        groupTurretSelection.setPosition(250, 500);
        Button btnSetTurret1 = new TextButton("T1", skin, "simpleSkin");
        Button btnSetTurret2 = new TextButton("T2", skin, "simpleSkin");
        btnSetTurret1.setPosition(10, 10);
        btnSetTurret2.setPosition(110, 10);
        groupTurretSelection.addActor(btnSetTurret1);
        groupTurretSelection.addActor(btnSetTurret2);

        btnSetTurret1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                turretEmitter.setup(selectedCellX, selectedCellY, TurretType.COMMON);
            }
        });

        btnSetTurret2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                turretEmitter.setup(selectedCellX, selectedCellY, TurretType.FREEZE);
            }
        });

        btnDestroyTurret.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                turretEmitter.destroyTurret(selectedCellX, selectedCellY);
            }
        });

        btnUpgradeTurret.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                turretEmitter.upgradeTurret(selectedCellX, selectedCellY);
            }
        });


        btnSetTurret.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                groupTurretSelection.setVisible(!groupTurretSelection.isVisible());
            }
        });

        winScrenGroup = new Group();
        winScrenGroup.setPosition(480, 250);
        winScrenGroup.setVisible(false);
        Button nextLevelButton = new TextButton("Go to next level", skin, "winSkin");
        nextLevelButton.setPosition(0,0);
        winScrenGroup.addActor(nextLevelButton);

        nextLevelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
            }
        });

        stage.addActor(groupTurretSelection);
        stage.addActor(groupTurretAction);
        stage.addActor(winScrenGroup);

        skin.dispose();
    }
    //getters
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
        return monsterEmitter;
    }
    public BulletEmitter getBulletEmitter() {
        return bulletEmitter;
    }
    public Player getPlayer() {
        return player;
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
        monsterEmitter.render(batch);
        particleEmitter.render(batch);
        infoEmitter.render(batch, scoreFont);
        //отрисовка скора
        scoreFont.draw(batch, "Score:" + player.getScore(), 20, 700);
        scoreFont.draw(batch, "Gold:" + player.getMoney(), 20, 650);
        scoreFont.draw(batch, "HP:" + player.getHp(), 20, 600);
        scoreFont.draw(batch, "Remaining Time:" + star16.getTimer(),120, 700 );
        //сообщения игроку
//        drawMessage(dt);


        batch.end();

        stage.draw();
    }
    public void update(float dt) {
        //создание частицы через particleEmitter
        map.update(dt);
        star16.update(dt);


        setupMonster(dt);
        //particleEmitter.init(640, 360, MathUtils.random(-20.0f, 20.0f), MathUtils.random(20.0f, 80.0f), 0.9f, 1.0f, 0.2f, 1, 0, 0, 1, 1, 1, 0, 1);
        //эмиттеры
        monsterEmitter.update(dt);
        turretEmitter.update(dt);
        bulletEmitter.update(dt);
        particleEmitter.update(dt);
        infoEmitter.update(dt);

        checkCollisions();

        stage.act(dt);

        monsterEmitter.checkPool();
        bulletEmitter.checkPool();
        particleEmitter.checkPool();
        turretEmitter.checkPool();
        infoEmitter.checkPool();
    }
    private void setupMonster(float dt) {
        respTime +=dt;
        if(respTime > 5f) {
            monsterEmitter.setup(MathUtils.random(7,10), MathUtils.random(3,5), 0,0, 100);
            respTime=4.5f;
        }
    }
    private void checkCollisions(){
        Monster m;
        Bullet b;
        for (int i = 0; i <monsterEmitter.getActiveList().size() ; i++) {
             m = monsterEmitter.getActiveList().get(i);
            for (int j = 0; j <bulletEmitter.getActiveList().size() ; j++) {
                b =bulletEmitter.getActiveList().get(j);
                if(m.getHitBox().overlaps(b.getHitBox())){
                    b.makeDamage(m);
                }
            }
        }
    }

    public void showWinScreen(){
        groupTurretAction.setVisible(false);
        groupTurretSelection.setVisible(false);
        winScrenGroup.setVisible(true);
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