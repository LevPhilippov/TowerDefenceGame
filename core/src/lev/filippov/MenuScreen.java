package lev.filippov;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MenuScreen implements Screen{

    private SpriteBatch batch;
    private Camera camera;
    private BitmapFont menuFont;
    private Stage stage;
    private Group menuGroup;

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

        //шкура для кнопок
        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();

        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = menuFont;
        skin.add("simpleSkin", textButtonStyle);

        //кнопки меню
        menuGroup = new Group();
        menuGroup.setVisible(true);
        menuGroup.setPosition(480, 250);
        //создание кнопок
        Button btnNewGame = new TextButton("NEW GAME", skin, "simpleSkin");
        Button btnExit = new TextButton("EXIT", skin, "simpleSkin");

        //разменение внутри Stage
        btnExit.setPosition(0,10);
        btnNewGame.setPosition(0, 100);

        btnNewGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
            }
        });

        btnExit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        menuGroup.addActor(btnNewGame);
        menuGroup.addActor(btnExit);
        stage.addActor(menuGroup);

        skin.dispose();
    }

    @Override
    public void render(float delta) {
        float dt = Gdx.graphics.getDeltaTime();
        update(dt);
        batch.begin();
        for (int i = 0; i < ScreenManager.WORLD_WIDTH; i++) {
            for (int j = 0; j <ScreenManager.WORLD_HEIGHT ; j++) {
                batch.draw(Assets.getInstance().getAtlas().findRegion("road"),i*80,j*80);
            }
        }
        menuFont.setColor(0,0,0,1);
        menuFont.draw(batch,"My Extra Fucking Tower Defence", 388, 600);
        menuFont.setColor(1,1,1,1);

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
