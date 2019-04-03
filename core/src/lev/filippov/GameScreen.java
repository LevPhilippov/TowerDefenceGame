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
    private int selectedCellX, selectedCellY;
    private BitmapFont scoreFont;
    private Stage stage;
    private Player player;

    //GUI
    private Group groupTurretAction;
    private Group groupTurretSelection;
    private float transparence;


    public Map getMap() {
        return map;
    }

    private float respTime;


    public GameScreen(SpriteBatch batch, Camera camera) {
        this.batch = batch;
        this.camera = camera;
    }
    //метод вызывается при установке экрана как текущего
    @Override
    public void show() {
        this.map = new Map("level01.map");
        this.scoreFont = Assets.getInstance().getAssetManager().get("fonts/zorque24.ttf");
        this.selectedCellTexture = Assets.getInstance().getAtlas().findRegion("cursor");
        this.turretEmitter = new TurretEmitter(this);
        this.particleEmitter = new ParticleEmitter();
        this.monsterEmitter = new MonsterEmitter(this);
        this.bulletEmitter = new BulletEmitter(this);
        this.player = new Player(this);
        createGUI();
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
        //эмиттеры
        monsterEmitter.render(batch);
        turretEmitter.render(batch);
        bulletEmitter.render(batch);
        particleEmitter.render(batch);
        //отрисовка скора
        scoreFont.draw(batch, "Score:" + player.getScore(), 20, 700);
        scoreFont.draw(batch, "Gold:" + player.getMoney(), 120, 700);
        scoreFont.draw(batch, "HP:" + player.getHp(), 220, 700);
        //сообщения игроку
        drawMessage(dt);


        batch.end();

        stage.draw();
    }

    public void drawMessage(float dt) {
        if(transparence>=0.0f){
            scoreFont.setColor(1,1,0,transparence/3);
            scoreFont.draw(batch, "Not enough gold!", selectedCellX*80, selectedCellY*80);
            transparence -=dt;
        }
        scoreFont.setColor(1,1,1,1);
    }
    public void setTransparence(){
        transparence = 3.0f;
    }

    public void update(float dt) {
        //создание частицы через particleEmitter
        map.update(dt);


        setupMonster(dt);
        //particleEmitter.setup(640, 360, MathUtils.random(-20.0f, 20.0f), MathUtils.random(20.0f, 80.0f), 0.9f, 1.0f, 0.2f, 1, 0, 0, 1, 1, 1, 0, 1);
        //эмиттеры
        turretEmitter.update(dt);
        monsterEmitter.update(dt);
        bulletEmitter.update(dt);
        particleEmitter.update(dt);

        checkCollisions();

        stage.act(dt);

        monsterEmitter.checkPool();
        bulletEmitter.checkPool();
        particleEmitter.checkPool();
        turretEmitter.checkPool();
    }

    public void setTurret() {
        if (player.isMoneyEnough(50)) {
            if (turretEmitter.setup(selectedCellX, selectedCellY)) {
                player.spendMoney(50);
            }
        }
    }

    private void setupMonster(float dt) {
        respTime +=dt;
        if(respTime > 0f) {
            monsterEmitter.setup(15, MathUtils.random(1,8), 0,0, 100);
            respTime=-0.1f;
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
    public void createGUI() {
        stage = new Stage(ScreenManager.getInstance().getViewport(), batch);

        InputProcessor myProc = new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                mousePosition = new Vector2();
                mousePosition.set(screenX, screenY);
                ScreenManager.getInstance().getViewport().unproject(mousePosition);
                if (selectedCellX == (int) (mousePosition.x / 80) && selectedCellY == (int) (mousePosition.y / 80)) {
                    map.setWall((int) (mousePosition.x / 80), (int) (mousePosition.y / 80));
                }
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

        textButtonStyle.up = skin.getDrawable("shortButton");
        textButtonStyle.font = scoreFont;
        skin.add("simpleSkin", textButtonStyle);

        //подготовка групп кнопок
            //кнопки группы "действие" (первичная)
        groupTurretAction = new Group();
        groupTurretAction.setPosition(250, 600);
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
             setTurret();
            }
        });

        btnSetTurret2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setTurret();
            }
        });

//        btnDestroyTurret.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                turretEmitter.destroyTurret(selectedCellX, selectedCellY);
//            }
//        });
//
//        btnUpgradeTurret.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                turretEmitter.upgradeTurret(playerInfo, selectedCellX, selectedCellY);
//            }
//        });

        stage.addActor(groupTurretSelection);
        stage.addActor(groupTurretAction);

//        upperPanel = new UpperPanel(playerInfo, stage, 0, 720 - 60);

        btnSetTurret.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                groupTurretSelection.setVisible(!groupTurretSelection.isVisible());
            }
        });

        skin.dispose();
    }


    //
//    public void prepare() {
//        mousePosition = new Vector2(0, 0);
//        InputProcessor myProc = new InputAdapter() {
//            @Override
//            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//                mousePosition.set(screenX, screenY);
//                // метод переводит координаты экраны в координаты нашего окна
//                ScreenManager.getInstance().getViewport().unproject(mousePosition);
//
//                if (selectedCellX == ((int) mousePosition.x/80) && selectedCellY == ((int) mousePosition.y/80)) {
//                    //здесь неоюходимо передать управление методу, определяющему (по карте объектов) -
//                    // какой элемент подсвечен и передать управление соответсвующему методу
//                    map.setWall(selectedCellX, selectedCellY);
//                    map.updateMapVersion();
//                }
//
//                selectedCellX = (int) (mousePosition.x / 80);
//                selectedCellY = (int) (mousePosition.y / 80);
//
//                return true;
//            }
//        };
//        // InputMultiplexer im = new InputMultiplexer(stage, myProc);
//        Gdx.input.setInputProcessor(myProc);
//    }
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

    public Player getPlayer() {
        return player;
    }
}