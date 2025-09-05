package musicPlayer;

import musicPlayer.models.Song;
import musicPlayer.ui.ConsoleUI;
import musicPlayer.utils.FilesUtils;
import musicPlayer.utils.SongLoader;
import java.util.List;

public class Main {
    public static void main(String[] args){
        SongLoader songLoader = new SongLoader();

        List<Song> allSongs = songLoader.loadSongsFound();

        FilesUtils filesUtils = new FilesUtils();
        allSongs.stream().forEach(System.out::println);

        // ConsoleUI consoleUI = new ConsoleUI();
        // consoleUI.startProgram();
    }
}
