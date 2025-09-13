package musicPlayer.ui.view.playlist;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import musicPlayer.models.Playlist;
import musicPlayer.ui.IView;
import musicPlayer.ui.UIView;

import java.util.List;

public class Menu implements IView {

    private int selectedPlaylistMenuItem = 0;

    private Playlist activePlaylist;

    private final List<String> playlistMenuItens = List.of("List Songs", "Play a Song", "Back to Main Menu");

    // TODO PASSAR LIBRARY OU PLAYLIST PRA PEGAR O NOME DA PLAYLIST SELECIONADA
    public Menu() {

    }

    @Override
    public void draw(Screen screen, TextGraphics graphics) {
        graphics.putString(2,1, "Selected Playlist: " + this.activePlaylist.getPlaylistName());
        drawSelectionList(graphics, playlistMenuItens, selectedPlaylistMenuItem, 2, 3);
    }

    @Override
    public UIView processInput(KeyStroke key) {
        if (key.getKeyType() == KeyType.ArrowUp || key.getKeyType() == KeyType.ArrowDown) {
            this.selectedPlaylistMenuItem = navigateList(this.selectedPlaylistMenuItem, this.playlistMenuItens.size(), key.getKeyType());
        } else if (key.getKeyType() == KeyType.Enter) {
            switch (selectedPlaylistMenuItem) {
                case 0:
                    return UIView.SHOWING_ALL_SONGS_ON_PLAYLIST;
                case 1:
                    return UIView.SELECTING_A_SONG_TO_PLAY;
                case 2:
                    return null;
            }
        }
        return UIView.PLAYLIST_MENU;
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
