package musicPlayer.ui.view.song;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import musicPlayer.models.Song;
import musicPlayer.ui.IView;
import musicPlayer.ui.UIView;
import musicPlayer.ui.UiContext;

import java.util.ArrayList;
import java.util.List;

public class ListAllSongs implements IView {

    private UiContext uiContext;

    public ListAllSongs(UiContext uiContext){
        this.uiContext = uiContext;
    }

    private List<Song> getAllSongsOnPlaylist(){
        return new ArrayList<Song>(uiContext.getActivePlaylist().getAllSongsOnPlaylist());
    }

    @Override
    public void draw(Screen screen, TextGraphics graphics) {
        graphics.putString(0, 1, "All Songs on " + uiContext.getActivePlaylist().getPlaylistName());
        List<Song> songs = getAllSongsOnPlaylist();

        for (int i = 0; i < songs.size(); i ++) {
            String songTitle = songs.get(i).getSongTitle();
            graphics.putString(2, 3 + i, songTitle);
        }

        graphics.putString(2, 4 + songs.size() + 1, "Press 'Backspace' to return.");
    }

    @Override
    public UIView processInput(KeyStroke key) {
        if (key.getKeyType() == KeyType.Backspace) {
            return UIView.PLAYLIST_MENU;
        }
        return UIView.SHOWING_ALL_SONGS_ON_PLAYLIST;
    }
}
