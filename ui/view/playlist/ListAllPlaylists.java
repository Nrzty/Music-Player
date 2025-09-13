package musicPlayer.ui.view.playlist;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import musicPlayer.models.Library;
import musicPlayer.ui.IView;
import musicPlayer.ui.UIView;

import java.util.ArrayList;
import java.util.List;

public class ListAllPlaylists implements IView {

    private Library library;

    public ListAllPlaylists(Library library) {
        this.library = library;
    }

    private List<String> getOrderedPlaylistsName(){
        return new ArrayList<>(library.getAllPlaylistsInLibrary());
    }

    @Override
    public void draw(Screen screen, TextGraphics graphics) {
        graphics.putString(2,1, "All Playlists: ");
        List<String> playlistsNames = getOrderedPlaylistsName();

        for (int i = 0; i < playlistsNames.size(); i++) {
            graphics.putString(2, 2 + i, playlistsNames.get(i));
        }

        graphics.putString(2, 3 + playlistsNames.size() + 1,"Press 'Backspace' to return.");
    }

    @Override
    public UIView processInput(KeyStroke key) {
        if (key.getKeyType() == KeyType.Backspace) {
            return UIView.MAIN_MENU;
        }
        return UIView.PLAYLIST_LIST_VIEW;
    }
}
