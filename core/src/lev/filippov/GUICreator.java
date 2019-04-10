package lev.filippov;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
//import oracle.jrockit.jfr.ActiveSettingEvent;

public class GUICreator {
    private static Vector2 mousePosition;
    private static int selectedCellX;
    private static int selectedCellY;

    public static void createGUI(GameScreen gameScreen) {

            Stage stage = new Stage(ScreenManager.getInstance().getViewport(), gameScreen.getBatch());

            //шкура для кнопок
            Skin skin = new Skin();
            skin.addRegions(Assets.getInstance().getAtlas());

            TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
            TextButton.TextButtonStyle winStyle = new TextButton.TextButtonStyle();

            textButtonStyle.up = skin.getDrawable("shortButton");
            textButtonStyle.font = Assets.getInstance().getAssetManager().get("fonts/zorque24.ttf");

            winStyle.up = skin.getDrawable("simpleButton");
            winStyle.font = Assets.getInstance().getAssetManager().get("fonts/zorque36.ttf");;
            skin.add("simpleSkin", textButtonStyle);
            skin.add("winSkin", winStyle);


            //подготовка групп кнопок
            //кнопки группы "действие" (первичная)
            Group groupTurretAction = new Group();
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
            Group groupTurretSelection = new Group();
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
                    gameScreen.getTurretEmitter().setup(selectedCellX, selectedCellY, "COMMON");
                }
            });

            btnSetTurret2.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    gameScreen.getTurretEmitter().setup(selectedCellX, selectedCellY, "FREEZE");
                }
            });

            btnDestroyTurret.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    gameScreen.getTurretEmitter().destroyTurret(selectedCellX, selectedCellY);
                }
            });

            btnUpgradeTurret.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    gameScreen.getTurretEmitter().upgradeTurret(selectedCellX, selectedCellY);
                }
            });


            btnSetTurret.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    groupTurretSelection.setVisible(!groupTurretSelection.isVisible());
                }
            });
            // Окошко победы/поражения
            Group winScrenGroup = new Group();
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
            gameScreen.setStage(stage);
            skin.dispose();

        InputProcessor myProc = new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                mousePosition = new Vector2();
                mousePosition.set(screenX, screenY);
                ScreenManager.getInstance().getViewport().unproject(mousePosition);

                selectedCellX = (int) (mousePosition.x / 80);
                selectedCellY = (int) (mousePosition.y / 80);

                gameScreen.setSelectedCellX(selectedCellX);
                gameScreen.setSelectedCellY(selectedCellY);
                return true;
            }
        };

        InputMultiplexer inputMultiplexer = new InputMultiplexer(stage, myProc);
        Gdx.input.setInputProcessor(inputMultiplexer);
        }

}
