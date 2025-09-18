package musicPlayer.ui.view.song;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import musicPlayer.models.Library;
import musicPlayer.models.Song;
import musicPlayer.player.MusicPlayer;
import musicPlayer.ui.IView;
import musicPlayer.ui.UIView;
import musicPlayer.ui.UiContext;

import java.util.ArrayList;
import java.util.List;

public class SelectingASong implements IView {

    private int selectedSong;
    private Library library;
    private UiContext uiContext;
    private MusicPlayer musicPlayer;
    private List<Song> songs;

    public SelectingASong(Library library, MusicPlayer musicPlayer, UiContext uiContext){
        this.library = library;
        this.musicPlayer = musicPlayer;
        this.uiContext = uiContext;
    }

    @Override
    public void draw(Screen screen, TextGraphics graphics) {
        playlistToDraw();

        graphics.putString(2, 1, "Select a Song: ");

        List<String> songTitles = new ArrayList<>();
        for (Song song : songs) {
            songTitles.add(song.getSongTitle());
        }

        drawSelectionList(graphics, songTitles, selectedSong, 2, 3);
    }

    @Override
    public UIView processInput(KeyStroke key) {
        if (key.getKeyType() == KeyType.Backspace) {
            return UIView.PLAYLIST_MENU;
        }

        if (key.getKeyType() == KeyType.ArrowUp || key.getKeyType() == KeyType.ArrowDown) {
            this.selectedSong = navigateList(this.selectedSong, songs.size(), key.getKeyType());
        } else if (key.getKeyType() == KeyType.Enter) {
            this.musicPlayer.playSong(songs.get(selectedSong));
            return UIView.PLAYING_SELECTED_SONG;
        }
        return UIView.SELECTING_A_SONG_TO_PLAY;
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
    
    private void  playlistToDraw(){
        if (this.songs == null) {
            this.songs = uiContext.getActivePlaylist().showAllSongs();
        }

        this.songs = uiContext.getActivePlaylist().showAllSongs();
    }
}
