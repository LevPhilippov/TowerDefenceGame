package lev.filippov;

import com.badlogic.gdx.Gdx;

import java.io.*;
import java.util.*;

public class GameSaverLoader {

//    public static void saveProgress(String playerName) {
//
//        String mapName = saveCompletedRound(playerName);
//        try {
//            Writer writer = Gdx.files.external("saves/" + playerName + ".save").writer(false);
//            writer.write(mapName);
//            writer.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

    public static boolean createNewPlayer(String playerName) {
        if(!Gdx.files.external("saves/" + playerName + ".save").exists()) {
            try {
                return Gdx.files.external("saves/" + playerName + ".save").file().createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static ArrayList<String> mapListLoader() {
        File[] files = new File("maps/").listFiles();
        ArrayList<String> maps = new ArrayList<String>();
        for (int i = 0; i <files.length ; i++) {
            maps.add(files[i].getName());
        }
        Collections.sort(maps);
        return maps;
    }

    public static void saveCompletedRound(String playerName, String mapName) {
//        BufferedReader bufferedReader = Gdx.files.external("saves/"+playerName+".save").reader(8192);
//        String str = null;
//        try {
//            str = bufferedReader.readLine();
//            bufferedReader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        ArrayList<String> list = mapListLoader();
//
//        if(str==null) {
//            return list.get(0);
//        }
//        return list.get(list.indexOf(str)+1);

        Writer writer = Gdx.files.external("saves/"+playerName+".save").writer(true);
        try {
            writer.write("\n");
            writer.write(mapName);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String loadGame (String playerName) {
        BufferedReader bufferedReader = Gdx.files.external("saves/"+playerName+".save").reader(8192);
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
