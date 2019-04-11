package lev.filippov;

import com.badlogic.gdx.Gdx;

import java.io.*;
import java.util.*;

public class GameSaverLoader {

    public static void saveProgress(String playerName) {
        if(!Gdx.files.external("saves/" + playerName + ".save").exists()) {
            try {
                Gdx.files.external("saves/" + playerName + ".save").file().createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String mapName = SaveGameRound(playerName);
        try {
            Writer writer = Gdx.files.external("saves/" + playerName + ".save").writer(true);
            writer.write(mapName);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<String> mapListLoader() {
        File[] files = new File("maps/").listFiles();
        ArrayList<String> maps = new ArrayList<>();
        for (int i = 0; i <files.length ; i++) {
            maps.add(files[i].getName());
        }
        Collections.sort(maps);
        return maps;
    }

    private static String SaveGameRound(String playerName) {
        BufferedReader bufferedReader = Gdx.files.external("saves/"+playerName+".save").reader(8192);
        String str = null;
        try {
            str = bufferedReader.readLine();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String> list = mapListLoader();
        if(str==null) {
            return list.get(0);
        }
        return list.get(list.indexOf(str)+1);
    }

    public static String loadGame (String playerName) {
        BufferedReader bufferedReader = Gdx.files.external("saves/"+playerName+".save").reader(8192);
        String str = null;
        try {
            str = bufferedReader.readLine();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

}
