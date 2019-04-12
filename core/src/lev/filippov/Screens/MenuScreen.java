package lev.filippov.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import lev.filippov.Assets;
import lev.filippov.GameSaverLoader;
import lev.filippov.ScreenManager;

public class MenuScreen implements Screen{

    private SpriteBatch batch;
    private Camera camera;
    private BitmapFont menuFont;
    private Stage stage;
    private Group menuGroup;
    private Group choosePlayerGroup;

    public MenuScreen(SpriteBatch batch, Camera camera) {
        this.batch = batch;
        this.camera = camera;
    }

    @Override
    public void show() {
        this.menuFont = Assets.getInstance().getAssetManager().get("fonts/zorque36.ttf");
        createGUI();
    }

    public void createGUI() {
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        Gdx.input.setInputProcessor(stage);

        //шкура
        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        //Styles
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = menuFont;
        Label.LabelStyle labelStyle = new Label.LabelStyle(menuFont, Color.WHITE);
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle(menuFont,Color.WHITE, skin.getDrawable("star16"),skin.getDrawable("star16"),skin.getDrawable("simpleButton"));
        //Добавляем в skin
        skin.add("simpleSkin", textButtonStyle);

        //кнопки меню
        menuGroup = new Group();
        menuGroup.setVisible(true);
        menuGroup.setPosition(480, 250);
        //создание кнопок
        Button btnStart = new TextButton("Start", skin, "simpleSkin");
        Button btnExit = new TextButton("EXIT", skin, "simpleSkin");
        Button btnBack = new TextButton("Back to main menu", skin, "simpleSkin");
        Button btnNewGame = new TextButton("New game", skin, "simpleSkin");
        Button btnLoad = new TextButton("Load", skin, "simpleSkin");

        //группа выбор игрока
        choosePlayerGroup = new Group();
        choosePlayerGroup.setVisible(false);
        choosePlayerGroup.setPosition(480, 250);

        //Label
        Label label = new Label("Player name", labelStyle);
        label.setWidth(320);
        label.setAlignment(1);

        //TextField
        final TextField textField = new TextField("", textFieldStyle);
        textField.setWidth(320);
        textField.setAlignment(1);

        //разменение внутри Group
        label.setPosition(0,300);
        textField.setPosition(0, 200);
        btnStart.setPosition(0,100);
        btnLoad.setPosition(0,0);
        btnBack.setPosition(0,-100);

        btnExit.setPosition(0,0);
        btnNewGame.setPosition(0, 100);

        btnNewGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameSaverLoader.createNewPlayer(textField.getText());
                GameScreen.playerName = textField.getText();
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
            }
        });

        btnBack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                choosePlayerGroup.setVisible(false);
                menuGroup.setVisible(true);
            }
        });

        btnStart.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameSaverLoader.mapListLoader();
                choosePlayerGroup.setVisible(true);
                menuGroup.setVisible(false);
            }
        });

        btnExit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });


        menuGroup.addActor(btnStart);
        menuGroup.addActor(btnExit);

        choosePlayerGroup.addActor(label);
        choosePlayerGroup.addActor(textField);
        choosePlayerGroup.addActor(btnBack);
        choosePlayerGroup.addActor(btnNewGame);
        choosePlayerGroup.addActor(btnLoad);

        stage.addActor(choosePlayerGroup);
        stage.addActor(menuGroup);

        skin.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float dt = Gdx.graphics.getDeltaTime();

        update(dt);
        batch.begin();
        for (int i = 0; i < ScreenManager.WORLD_WIDTH; i++) {
            for (int j = 0; j <ScreenManager.WORLD_HEIGHT ; j++) {
                batch.draw(Assets.getInstance().getAtlas().findRegion("road"),i*80,j*80);
            }
        }

        batch.end();
        stage.draw();
    }

    public void update(float dt) {
        stage.act(dt);
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
