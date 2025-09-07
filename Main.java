package musicPlayer;

import musicPlayer.models.Library;
import musicPlayer.models.Song;
import musicPlayer.ui.ConsoleUI;
import musicPlayer.utils.FilesUtils;
import musicPlayer.utils.LibraryLoader;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        LibraryLoader libraryLoader = new LibraryLoader();

        Library library = libraryLoader.loadAllPlaylistsFound();
        ConsoleUI consoleUI = new ConsoleUI(library);
        consoleUI.startInicialMenu();
    }
}
