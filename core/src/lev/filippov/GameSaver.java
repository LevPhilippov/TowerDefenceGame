package lev.filippov;

import com.badlogic.gdx.Gdx;

import java.io.*;

public class GameSaver {

    public static void saveProgress(String playerName, String mapName) {
        File file = new File("saves/" + playerName + ".save");
        try {
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.write(mapName);
            printWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static File[] mapListLoader() {
        File[] files = new File("maps/").listFiles();
        return files;
    }

    public static String loadSavedGameRound(String playerName) {
        BufferedReader bufferedReader = Gdx.files.internal("saves/"+playerName+".save").reader(8192);
        try {
            return bufferedReader.readLine().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "NoSuchFile";
    }
}
