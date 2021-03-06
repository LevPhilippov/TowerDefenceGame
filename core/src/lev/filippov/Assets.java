package lev.filippov;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

/**
 * класс-Singleton для организации загрузки и хранения данных/медиа экранов
 * */

public class Assets {
    private static final Assets ourInstance = new Assets();

    public static Assets getInstance() {
        return ourInstance;
    }

    private AssetManager assetManager;
    private TextureAtlas textureAtlas;

    public TextureAtlas getAtlas() {
        return textureAtlas;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    private Assets() {
        assetManager = new AssetManager();
    }

    //метод, позволяющий загрузить в память (с помощью assetManager) материалы для запрошенного экрана
    public void loadAssets(ScreenManager.ScreenType type) {
        switch (type) {
            case GAME:
                assetManager.load("images/game.pack", TextureAtlas.class);
                createStandardFont(24);
                createStandardFont(36);
                createStandardFont(16);
                break;
        }
    }

    // подготовка шрифта заданного размера из стандартного шрифта

    public void createStandardFont(int size) {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        FreetypeFontLoader.FreeTypeFontLoaderParameter fontParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();

        fontParameter.fontFileName = "fonts/gomarice.ttf";
        fontParameter.fontParameters.size = size;
        fontParameter.fontParameters.color = Color.WHITE;
        fontParameter.fontParameters.borderWidth = 1;
        fontParameter.fontParameters.borderColor = Color.BLACK;
        fontParameter.fontParameters.shadowOffsetX = 1;
        fontParameter.fontParameters.shadowOffsetY = 1;
        fontParameter.fontParameters.shadowColor = Color.BLACK;

        assetManager.load("fonts/zorque" + size + ".ttf", BitmapFont.class, fontParameter);
    }
    // подвязка TextureAtlas после его загрузки
    public void makeLinks() {
        textureAtlas = assetManager.get("images/game.pack", TextureAtlas.class);
    }
    // выгружает из памяти (затирает ссылки, обнуляет счетчики) все ранее хранящиеся assets и зависимости
    public void clear() {
        assetManager.clear();
    }
}
