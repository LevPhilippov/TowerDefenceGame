package lev.filippov;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class BulletEmitter extends ObjectPool<Bullet> {
    private Bullet temp;
    private GameScreen gameScreen;
    private HashMap <String, BulletTemplate> bulletTemplates;

    public BulletEmitter (GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.bulletTemplates = new HashMap<String, BulletTemplate>();
        loadTemplates();
    }

    private void loadTemplates() {
        BufferedReader reader;
        try{
            reader = Gdx.files.internal("armory.dat").reader(8192);
            String str;
            Boolean read=false;
            while ((str = reader.readLine())!=null) {
                if(str.equals("# bullets-down"))
                    break;
                if(str.equals("# bullets-up")) {
                    read = true;
                    continue;
                }
                if(read) {
                    BulletTemplate template = new BulletTemplate(str);
                    bulletTemplates.put(template.getName(), template);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected Bullet newObject() {
        return new Bullet(gameScreen);
    }

    public void setup(float x, float y, float vx, float vy, String bulletTemplateName, Monster target){
        Bullet b = getActiveElement();
        b.init(x,y,vx,vy,bulletTemplates.get(bulletTemplateName), target);
    }

    public void update(float dt){
        for (int i = 0; i <activeList.size() ; i++) {
            activeList.get(i).update(dt);
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i <activeList.size() ; i++) {
            activeList.get(i).render(batch);
        }
    }


}
