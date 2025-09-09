package musicPlayer;

import javafx.embed.swing.JFXPanel;
import musicPlayer.models.Library;
import musicPlayer.ui.ConsoleUI;
import musicPlayer.utils.LibraryLoader;
import musicPlayer.player.MusicPlayer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) throws Exception {
        Logger.getLogger("org.jaudiotagger").setLevel(Level.SEVERE);

        new JFXPanel();
        LibraryLoader libraryLoader = new LibraryLoader();
        
        MusicPlayer musicPlayer = new MusicPlayer();

        Library library = libraryLoader.loadAllPlaylistsFound();
        ConsoleUI consoleUI = new ConsoleUI(library, musicPlayer);
        consoleUI.startInicialMenu();
    }
}
