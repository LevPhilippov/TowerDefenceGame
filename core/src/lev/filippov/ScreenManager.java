package lev.filippov;

/**
 * Класс занимается управлением (установкой) экранами/ов, изменением их размеров, управлением камерой
 */

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ScreenManager {
    public enum ScreenType {
        GAME,
        MENU
    }

    public static final int WORLD_WIDTH = 1280;
    public static final int WORLD_HEIGHT = 720;

    private TowerDefenseGame game;
    private SpriteBatch batch;
    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    private LoadingScreen loadingScreen;
    private Screen targetScreen;
    private Viewport viewport;
    private Camera camera;

    private static ScreenManager ourInstance = new ScreenManager();

    public static ScreenManager getInstance() {
        return ourInstance;
    }

    public Viewport getViewport() {
        return viewport;
    }

    private ScreenManager() {
    }
    //инициализирующий метод, вызывается в TowerDefenceGame, создаются все экраны и основные обьекты игры
    public void init(TowerDefenseGame game, SpriteBatch batch) {
        this.game = game;
        this.batch = batch;
        this.camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        this.viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        this.menuScreen = new MenuScreen(batch, camera);
        this.gameScreen = new GameScreen(batch, camera);
        this.loadingScreen = new LoadingScreen(batch);
    }

    // метод вызвается при изменении размера окна
    public void resize(int width, int height) {
        viewport.update(width, height);
        viewport.apply();
    }
    //метод вызвается при смене экрана, центрирует камеру
    public void resetCamera() {
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        batch.setProjectionMatrix(camera.combined); // что здесь происходит?
    }
    //смена текущего экрана, позволяет переходить между экранами меню, игры и прочими через загрузочный
    public void changeScreen(ScreenType type) {
        Screen screen = game.getScreen();
        Assets.getInstance().clear();
        //удаляем данные (освобождаем пямять) предыдущего экрана, если он существовал
        if (screen != null) {
            screen.dispose();
        }
        resetCamera();
        //переход к экрану загрузки ()
        game.setScreen(loadingScreen);
        // устанавливает targetScreeen и в зависимости от заданного типа экрана дает задание на загрузку data для экрана
        switch (type) {
            case GAME:
                targetScreen = gameScreen;
                Assets.getInstance().loadAssets(ScreenType.GAME);
                break;
            case MENU:
                targetScreen = menuScreen;
                Assets.getInstance().loadAssets(ScreenType.GAME);
                break;
        }
    }
    //переход к загруженному экрану
    public void goToTarget() {
        game.setScreen(targetScreen);
    }
}
