package musicPlayer.ui;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import musicPlayer.models.Library;
import musicPlayer.models.Playlist;
import musicPlayer.utils.UserInputs;
import musicPlayer.player.MusicPlayer;
import musicPlayer.models.Song;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class ConsoleUI {

    private int selectedMenuItem = 0;
    private int selectedPlaylistMenuItem = 0;

    private final UserInputs userInputs;

    private final Library library;
    private MusicPlayer musicPlayer;
    private Playlist activePlaylist;

    private DefaultTerminalFactory defaultTerminalFactory;
    private Terminal terminal;
    private Screen screen;
    private TextGraphics graphics;

    private UIView currentView = UIView.MAIN_MENU;

    private final List<String> mainMenuItens = List.of("List all playlits", "Remove a Playlist", "Select a playlist", "Exit");

    private final List<String> playlistMenuItens = List.of("List all songs in the playlist", "Add a song to the playlist", "Remove a song from the playlist", "Play a song from the playlist", "Back to main menu");

    public ConsoleUI(Library library, MusicPlayer musicPlayer) {
        this.userInputs = new UserInputs();
        this.library = library;
        this.musicPlayer = musicPlayer;

        try {
            this.terminal = new DefaultTerminalFactory().createTerminal();
            this.screen = new TerminalScreen(this.terminal);
            this.screen.startScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawCurrentView() throws IOException {
        switch (currentView) {
            case MAIN_MENU -> displayMainMenu();
            case PLAYLIST_MENU -> displayPlaylistMenu();
            case PLAYLIST_LIST_VIEW -> displayAllPlaylists();
        }
    }

    public void closeTerminal() throws IOException {
        this.screen.stopScreen();
    }

    private void arrowSelectionToMenu(List<String> itensList, int meuItem) {
        for (int i = 0; i < itensList.size(); i++) {
            if (i == meuItem) {
                graphics.putString(0, 1 + i, i + "-> " + itensList.get(i));
            } else {
                graphics.putString(0, 1 + i, i + "  " + itensList.get(i));
            }
        }
    }

    private void displayMainMenu() throws IOException {
        this.screen.clear();
        this.graphics = screen.newTextGraphics();
        graphics.putString(0, 0, "Main Menu");

        arrowSelectionToMenu(mainMenuItens, selectedMenuItem);
    }

    private void displayPlaylistMenu() {
        this.screen.clear();
        this.graphics = screen.newTextGraphics();
        graphics.putString(0, 0, "Selected Playlist Main Menu");

        arrowSelectionToMenu(playlistMenuItens, selectedPlaylistMenuItem);
    }

    private void displayAllPlaylists() {
        this.screen.clear();
        this.graphics = screen.newTextGraphics();
        graphics.putString(0, 0, "These are all your playlists:");

        Set<String> playListNames = library.getAllPlaylistsInLibrary();

        int index = 1;
        for (String playlistName : playListNames) {
            index++;
            graphics.putString(0, index, (index - 1) + " - " + playlistName);
        }

        graphics.putString(0, index + 2, "Press 'BackSpace' to go back to menu.");
    }

    public void startInitialMenu() throws IOException {
        boolean continueLooping = true;
        while (continueLooping) {
            drawCurrentView();
            this.screen.refresh();
            KeyStroke key = screen.readInput();
            continueLooping = processInputs(key);
        }
    }

    private int navigateThroughMenu(int currentSelection, int listSize, KeyType key) {
        if (key == KeyType.ArrowUp) {
            currentSelection--;
            if (currentSelection < 0) {
                currentSelection = 0;
            }
        } else if (key == KeyType.ArrowDown) {
            currentSelection++;
            if (currentSelection >= listSize) {
                currentSelection = listSize - 1;
            }
        }
        return currentSelection;
    }

    private boolean processInputs(KeyStroke key) throws IOException {
        switch (currentView) {
            case MAIN_MENU:
                if (key.getKeyType() == KeyType.ArrowUp || key.getKeyType() == KeyType.ArrowDown) {
                    this.selectedMenuItem = navigateThroughMenu(this.selectedMenuItem, this.mainMenuItens.size(), key.getKeyType());
                } else if (key.getKeyType().equals(KeyType.Enter)) {
                    switch (selectedMenuItem) {
                        case 0:
                            this.currentView = UIView.PLAYLIST_LIST_VIEW;
                            break;
                        case 1:

                            break;
                        case 2:
                            this.currentView = UIView.PLAYLIST_MENU;
                            break;
                        case 3:
                            closeTerminal();
                            return false;
                    }
                }
                break;

            case PLAYLIST_LIST_VIEW:
                if (key.getKeyType().equals(KeyType.Backspace)) {
                    this.currentView = UIView.MAIN_MENU;
                }
                break;

            case PLAYLIST_MENU:
                if (key.getKeyType() == KeyType.ArrowUp || key.getKeyType() == KeyType.ArrowDown) {
                    this.selectedPlaylistMenuItem = navigateThroughMenu(this.selectedPlaylistMenuItem, this.playlistMenuItens.size(), key.getKeyType());
                }

                switch (selectedPlaylistMenuItem){
                    case 0:

                        break;
                    case 4:
                        this.currentView = UIView.MAIN_MENU;
                        break;
                }

                if (key.getKeyType() == KeyType.Backspace){
                    this.currentView = UIView.MAIN_MENU;
                }
                break;
        }
        return true;
    }
}
