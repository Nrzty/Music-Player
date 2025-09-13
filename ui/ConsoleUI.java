package musicPlayer.ui;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import musicPlayer.models.Library;
import musicPlayer.player.MusicPlayer;
import musicPlayer.ui.view.mainMenu.MainMenu;
import musicPlayer.ui.view.playlist.ListAllPlaylists;
import musicPlayer.ui.view.playlist.Menu;
import musicPlayer.ui.view.playlist.SelectingAPlaylist;
import musicPlayer.ui.view.song.ListAllSongs;
import musicPlayer.ui.view.song.PlayingSong;
import musicPlayer.ui.view.song.SelectingASong;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConsoleUI {

    private final Screen screen;
    private final Terminal terminal;
    private final Map<UIView, IView> views = new HashMap<>();
    private IView currentView;

    private final UiContext uiContext;

    public ConsoleUI(Library library, MusicPlayer musicPlayer) throws IOException {
        this.terminal = new DefaultTerminalFactory().createTerminal();
        this.screen = new TerminalScreen(this.terminal);
        this.uiContext = new UiContext();

        views.put(UIView.MAIN_MENU, new MainMenu());

        views.put(UIView.PLAYLIST_LIST_VIEW, new ListAllPlaylists(library));

        views.put(UIView.SELECTING_A_PLAYLIST, new SelectingAPlaylist(library, uiContext));

        views.put(UIView.PLAYLIST_MENU, new Menu(uiContext));

        views.put(UIView.SHOWING_ALL_SONGS_ON_PLAYLIST, new ListAllSongs(uiContext));

        views.put(UIView.SELECTING_A_SONG_TO_PLAY, new SelectingASong(library, musicPlayer, uiContext));

        views.put(UIView.PLAYING_SELECTED_SONG, new PlayingSong(musicPlayer));


        this.currentView = views.get(UIView.MAIN_MENU);
    }

    public void start() throws IOException {
        screen.startScreen();
        screen.setCursorPosition(null);

        while (true) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

            screen.clear();
            TextGraphics graphics = screen.newTextGraphics();

            currentView.draw(screen, graphics);
            screen.refresh();

            KeyStroke key = screen.pollInput();
            if (key != null) {
                UIView nextViewEnum = currentView.processInput(key);

                if (nextViewEnum == null) {
                    break;
                }

                IView nextView = views.get(nextViewEnum);
                if (nextView != null) {
                    currentView = nextView;
                }
            }
        }
        close();
    }

    public void close() throws IOException {
        if (this.screen != null) {
            screen.stopScreen();
        }
        if (this.terminal != null) {
            terminal.close();
        }
    }
}