package musicPlayer.ui.view.playlist;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import musicPlayer.models.Library;
import musicPlayer.models.Playlist;
import musicPlayer.ui.IView;
import musicPlayer.ui.UIView;
import musicPlayer.ui.UiContext;

import java.util.ArrayList;
import java.util.List;

public class SelectingAPlaylist implements IView {

    private int selectedPlaylist = 0;

    private final Library library;
    private final UiContext uiContext;
    private final List<String> playlistNames;

    public SelectingAPlaylist(Library library, UiContext uiContext){
        this.library = library;
        this.uiContext = uiContext;
        this.playlistNames = new ArrayList<>(library.getAllPlaylistsInLibrary());
    }

    @Override
    public void draw(Screen screen, TextGraphics graphics) {
        graphics.putString(2,1, "Select a Playlist: ");

        drawSelectionList(graphics, playlistNames, selectedPlaylist,2, 3);
    }

    @Override
    public UIView processInput(KeyStroke key) {
        if (key.getKeyType() == KeyType.ArrowUp || key.getKeyType() == KeyType.ArrowDown) {
            this.selectedPlaylist = navigateList(this.selectedPlaylist, this.playlistNames.size(), key.getKeyType());
        } else if (key.getKeyType() == KeyType.Enter) {
            String playlistName = playlistNames.get(this.selectedPlaylist);

            Playlist selectedPlaylist = library.getPlaylistByName(playlistName);
            uiContext.setActivePlaylist(selectedPlaylist);

            return UIView.PLAYLIST_MENU;
        } else if (key.getKeyType() == KeyType.Backspace) {
            return UIView.MAIN_MENU;
        }
        return UIView.SELECTING_A_PLAYLIST;
    }

    private int navigateList(int currentSelection, int listSize, KeyType key){
        if (key == KeyType.ArrowUp) {
            currentSelection = Math.max(0, currentSelection - 1);
        } else if (key == KeyType.ArrowDown) {
            currentSelection = Math.min(listSize - 1, currentSelection + 1);
        }
        return currentSelection;
    }

    private void drawSelectionList(TextGraphics graphics, List<String> items, int selectedIndex, int x, int y){
        for (int i = 0; i < items.size(); i++) {
            String prefix = (i == selectedIndex) ? "-> " : "   ";
            graphics.putString(x, y + i, prefix + items.get(i));
        }
    }
}
