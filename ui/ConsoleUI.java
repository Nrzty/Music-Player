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
        // TODO: Adicionar as outras views (PlaylistListView, SongSelectionView, etc.)

        this.currentView = views.get(UIView.MAIN_MENU);
    }

    public void start() throws IOException {
        screen.startScreen();
        screen.setCursorPosition(null);

        while (true) {
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