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
import musicPlayer.player.PlayerStatus;
import musicPlayer.player.MusicPlayer;
import musicPlayer.models.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConsoleUI {

    private int selectedMenuItem = 0;
    private int selectedPlaylistIndex = 0;
    private int selectedSongIndex = 0;
    private int selectedPlaylistMenuItem = 0;

    private final Library library;
    private final MusicPlayer musicPlayer;
    private Playlist activePlaylist;
    private Song playingSong;

    private final Terminal terminal;
    private final Screen screen;
    private TextGraphics graphics;

    private UIView currentView = UIView.MAIN_MENU;

    private final List<String> mainMenuItens = List.of("Show Playlists", "Select Playlist", "Exit");
    private final List<String> playlistMenuItens = List.of("List Songs", "Play a Song", "Back to Main Menu");

    public ConsoleUI(Library library, MusicPlayer musicPlayer) {
        this.library = library;
        this.musicPlayer = musicPlayer;

        try {
            this.terminal = new DefaultTerminalFactory().createTerminal();
            this.screen = new TerminalScreen(this.terminal);
            this.screen.startScreen();
            this.screen.setCursorPosition(null);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize terminal", e);
        }
    }

    public void start() throws IOException {
        boolean continueLooping = true;
        while (continueLooping) {
            drawCurrentView();
            screen.refresh();

            KeyStroke key = screen.pollInput();
            if (key != null) {
                continueLooping = processInputs(key);
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void close() throws IOException {
        if (this.screen != null) {
            this.screen.stopScreen();
        }
        if (this.terminal != null) {
            this.terminal.close();
        }
    }

    private void drawCurrentView() {
        this.graphics = screen.newTextGraphics();
        screen.clear();

        switch (currentView) {
            case MAIN_MENU -> displayMainMenu();
            case PLAYLIST_LIST_VIEW -> displayAllPlaylistsView();
            case SELECTING_A_PLAYLIST -> displayPlaylistSelectionView();
            case PLAYLIST_MENU -> displayPlaylistMenu();
            case SELECTING_A_SONG_TO_PLAY -> displaySongSelectionView();
            case PLAYING_SELECTED_SONG -> displayNowPlayingView();
            case SHOWING_ALL_SONGS_ON_PLAYLIST -> displayAllSongsOnPlaylist();
        }
    }

    private boolean processInputs(KeyStroke key) {
        switch (currentView) {
            case MAIN_MENU:
                return processMainMenuInputs(key);
            case PLAYLIST_LIST_VIEW:
                return processPlaylistListViewInputs(key);
            case SELECTING_A_PLAYLIST:
                return processPlaylistSelectionInputs(key);
            case PLAYLIST_MENU:
                return processPlaylistMenuInputs(key);
            case SELECTING_A_SONG_TO_PLAY:
                return processSongSelectionInputs(key);
            case PLAYING_SELECTED_SONG:
                return processNowPlayingInputs(key);
            case SHOWING_ALL_SONGS_ON_PLAYLIST:
                processPlaylistMenuInputs(key);
        }
        return true;
    }

    private void displayMainMenu() {
        graphics.putString(2, 1, "Main Menu");
        drawSelectionList(mainMenuItens, selectedMenuItem, 2, 3);
    }

    private void displayAllPlaylistsView() {
        graphics.putString(2, 1, "All Playlists");
        List<String> playlistNames = getOrderedPlaylistNames();
        for (int i = 0; i < playlistNames.size(); i++) {
            graphics.putString(2, 3 + i, "- " + playlistNames.get(i));
        }
        graphics.putString(2, 3 + playlistNames.size() + 2, "Press 'Backspace' to return.");
    }

    private void displayAllSongsOnPlaylist(){
        graphics.putString(2, 1, "All songs found on playlist: ");
        List<Song> songsOnPlaylist = getOrderedSongsOnPlaylist();
        for (int i = 0; i < songsOnPlaylist.size(); i++){
            graphics.putString(2,3 + i, "- " + songsOnPlaylist.get(i).getSongTitle());
        }
        graphics.putString(2, 3 + songsOnPlaylist.size() + 2, "Press 'Backspace' to return.");
    }

    private void displayPlaylistSelectionView() {
        graphics.putString(2, 1, "Select a Playlist");
        drawSelectionList(getOrderedPlaylistNames(), selectedPlaylistIndex, 2, 3);
    }

    private void displayPlaylistMenu() {
        graphics.putString(2, 1, "Selected Playlist: " + activePlaylist.getPlaylistName());
        drawSelectionList(playlistMenuItens, selectedPlaylistMenuItem, 2, 3);
    }

    private void displaySongSelectionView() {
        graphics.putString(2, 1, "Select a Song to Play");
        List<String> songTitles = new ArrayList<>();
        for (Song song : activePlaylist.showAllSongs()) {
            songTitles.add(song.getSongTitle());
        }
        drawSelectionList(songTitles, selectedSongIndex, 2, 3);
    }

    private void displayNowPlayingView() {
        Song currentSong = this.playingSong;
        if (currentSong != null) {
            graphics.putString(18, 2, "Now Playing");
            graphics.putString(13, 4, "Title: " + currentSong.getSongTitle());
            graphics.putString(13, 5, "Artist(s): " + currentSong.getSongArtist());

            double totalDuration = musicPlayer.getTotalDurationTime();
            double currentTime = musicPlayer.getActualDurationTime();
            double progress = (totalDuration > 0) ? (currentTime / totalDuration) : 0.0;

            String progressBar = buildProgressBar(progress);
            String timeDisplay = formatTime(currentTime) + " / " + formatTime(totalDuration);

            graphics.putString(13, 7, progressBar);
            graphics.putString(17, 8, timeDisplay);
            graphics.putString(2, 10, "(p)ause | (r)esume | (s)top | (Backspace) Back");
        } else {
            graphics.putString(2, 2, "No song is currently playing.");
        }
    }

    private boolean processMainMenuInputs(KeyStroke key) {
        if (key.getKeyType() == KeyType.ArrowUp || key.getKeyType() == KeyType.ArrowDown) {
            this.selectedMenuItem = navigateList(this.selectedMenuItem, this.mainMenuItens.size(), key.getKeyType());
        } else if (key.getKeyType() == KeyType.Enter) {
            switch (selectedMenuItem) {
                case 0 -> this.currentView = UIView.PLAYLIST_LIST_VIEW;
                case 1 -> {
                    this.currentView = UIView.SELECTING_A_PLAYLIST;
                    this.selectedPlaylistIndex = 0;
                }
                case 2 -> {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean processPlaylistListViewInputs(KeyStroke key) {
        if (key.getKeyType() == KeyType.Backspace) {
            this.currentView = UIView.MAIN_MENU;
        }
        return true;
    }

    private boolean processPlaylistSelectionInputs(KeyStroke key) {
        List<String> playlistNames = getOrderedPlaylistNames();
        if (key.getKeyType() == KeyType.ArrowUp || key.getKeyType() == KeyType.ArrowDown) {
            this.selectedPlaylistIndex = navigateList(this.selectedPlaylistIndex, playlistNames.size(), key.getKeyType());
        } else if (key.getKeyType() == KeyType.Backspace) {
            this.currentView = UIView.MAIN_MENU;
        } else if (key.getKeyType() == KeyType.Enter) {
            String selectedName = playlistNames.get(selectedPlaylistIndex);
            this.activePlaylist = library.getPlaylistByName(selectedName);
            this.currentView = UIView.PLAYLIST_MENU;
            this.selectedPlaylistMenuItem = 0;
        }
        return true;
    }

    private boolean processPlaylistMenuInputs(KeyStroke key) {
        if (key.getKeyType() == KeyType.ArrowUp || key.getKeyType() == KeyType.ArrowDown) {
            this.selectedPlaylistMenuItem = navigateList(this.selectedPlaylistMenuItem, this.playlistMenuItens.size(), key.getKeyType());
        } else if (key.getKeyType() == KeyType.Backspace) {
            this.currentView = UIView.SELECTING_A_PLAYLIST;
        } else if (key.getKeyType() == KeyType.Enter) {
            switch (selectedPlaylistMenuItem) {
                case 0 -> {
                    this.currentView = UIView.SHOWING_ALL_SONGS_ON_PLAYLIST;
                }
                case 1 -> {
                    this.currentView = UIView.SELECTING_A_SONG_TO_PLAY;
                    this.selectedSongIndex = 0;
                }
                case 2 -> this.currentView = UIView.MAIN_MENU;
            }
        }
        return true;
    }

    private boolean processSongSelectionInputs(KeyStroke key) {
        ArrayList<Song> songs = this.activePlaylist.showAllSongs();
        if (key.getKeyType() == KeyType.ArrowUp || key.getKeyType() == KeyType.ArrowDown) {
            this.selectedSongIndex = navigateList(this.selectedSongIndex, songs.size(), key.getKeyType());
        } else if (key.getKeyType() == KeyType.Backspace) {
            this.currentView = UIView.PLAYLIST_MENU;
        } else if (key.getKeyType() == KeyType.Enter) {
            this.playingSong = songs.get(this.selectedSongIndex);
            this.musicPlayer.playSong(this.playingSong);
            this.currentView = UIView.PLAYING_SELECTED_SONG;
        }
        return true;
    }

    private boolean processNowPlayingInputs(KeyStroke key) {
        if (key.getCharacter() != null) {
            if (key.getCharacter().equals('p')) musicPlayer.pause();
            if (key.getCharacter().equals('r')) musicPlayer.resume();
            if (key.getCharacter().equals('s')) musicPlayer.stop();
        }
        if (key.getKeyType() == KeyType.Backspace) {
            musicPlayer.stop();
            this.currentView = UIView.PLAYLIST_MENU;
        }
        if (musicPlayer.getStatus() == PlayerStatus.STOPPED) {
            this.currentView = UIView.PLAYLIST_MENU;
        }
        return true;
    }

    private int navigateList(int currentSelection, int listSize, KeyType key) {
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

    private void drawSelectionList(List<String> items, int selectedIndex, int x, int y) {
        for (int i = 0; i < items.size(); i++) {
            String prefix = (i == selectedIndex) ? "-> " : "   ";
            graphics.putString(x, y + i, prefix + items.get(i));
        }
    }

    private List<String> getOrderedPlaylistNames() {
        return new ArrayList<>(library.getAllPlaylistsInLibrary());
    }

    private List<Song> getOrderedSongsOnPlaylist(){
        return new ArrayList<>(library.getAllSongsInAPlaylist(this.activePlaylist.getPlaylistName()));
    }

    private String buildProgressBar(double progressPercentage) {
        int barWidth = 20;
        int filledWidth = (int) (progressPercentage * barWidth);
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < barWidth; i++) {
            sb.append(i < filledWidth ? '█' : '-');
        }
        sb.append("]");
        return sb.toString();
    }

    private String formatTime(double totalSeconds) {
        if (Double.isNaN(totalSeconds) || totalSeconds < 0) {
            return "00:00";
        }
        int totalSecondsInt = (int) totalSeconds;
        int minutes = totalSecondsInt / 60;
        int seconds = totalSecondsInt % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
