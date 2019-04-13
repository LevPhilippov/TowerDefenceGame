package lev.filippov;

import com.badlogic.gdx.Gdx;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

public class GameSaverLoader {

    final static String path;

    static {
        path = findAbsolutePathToDirectory();
    }


    public static boolean createNewPlayer(String playerName) {
        if(!Gdx.files.absolute(path + "saves/" + playerName + ".save").exists()) {
            try {
                return Gdx.files.absolute(path + "saves/" + playerName + ".save").file().createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // "./maps/" - определяет путь к файлу отталкиваясь от директории в которой расположен .jar
    // или использовать new File(GameSaverLoader.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
    public static ArrayList<String> mapListLoader() {
        File[] files = new File(path+"/maps").listFiles();
        ArrayList<String> maps = new ArrayList<String>();
        for (int i = 0; i <files.length ; i++) {
            maps.add(files[i].getName());
        }
        Collections.sort(maps);
        return maps;
    }

    private static String findAbsolutePathToDirectory() {
        File jarFile = null;
        try {
            jarFile = new File(GameSaverLoader.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return jarFile.getParent() + File.separator;
    }

    public static void saveCompletedRound(String playerName, String mapName) {

        Writer writer = Gdx.files.absolute(path + "saves/"+playerName+".save").writer(true);
        try {
            writer.write("\n");
            writer.write(mapName);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String loadGame (String playerName) {
        BufferedReader bufferedReader = Gdx.files.absolute(path + "saves/"+playerName+".save").reader(8192);
        String str = null;
        ArrayList<String> mapList = new ArrayList<String>();
        try {
            while((str = bufferedReader.readLine())!=null) {
                mapList.add(str);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(mapList.isEmpty()) {
            return mapListLoader().get(0);
        }

        return mapListLoader().get(mapListLoader().indexOf(str)+1);
    }
}
