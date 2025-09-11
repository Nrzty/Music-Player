package musicPlayer.ui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import musicPlayer.models.Library;
import musicPlayer.models.Playlist;
import musicPlayer.player.PlayerStatus;
import musicPlayer.utils.UserInputs;
import musicPlayer.player.MusicPlayer;
import musicPlayer.models.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;

public class ConsoleUI {

    private int selectedMenuItem = 0;
    private int selectedPlaylistMenuItem = 0;
    private int selectedPlaylistIndex = 0;
    private int selectASongFromPlaylist = 0;

    private final UserInputs userInputs;

    private final Library library;
    private MusicPlayer musicPlayer;
    private Playlist activePlaylist;

    private DefaultTerminalFactory defaultTerminalFactory;
    private Terminal terminal;
    private Screen screen;
    private TextGraphics graphics;

    private UIView currentView = UIView.MAIN_MENU;
    private MultiWindowTextGUI gui;

    private Song playingSong;

    private final List<String> mainMenuItens = List.of("List all playlits", "Remove a Playlist", "Select a playlist", "Exit");

    private final List<String> playlistMenuItens = List.of("List all songs in the playlist", "Add a song to the playlist", "Remove a song from the playlist", "Play a song from the playlist", "Back to main menu");

    private final Set<String> playListNames;

    private Thread uiThreadUpdater;

    private Thread progressBarUpdaterThread;

    public ConsoleUI(Library library, MusicPlayer musicPlayer) {
        this.userInputs = new UserInputs();
        this.library = library;
        this.musicPlayer = musicPlayer;

        playListNames = library.getAllPlaylistsInLibrary();

        try {
            this.terminal = new DefaultTerminalFactory().createTerminal();
            this.screen = new TerminalScreen(this.terminal);
            this.gui = new MultiWindowTextGUI(this.screen);
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
            case SELECTING_A_PLAYLIST -> displayPlaylistsToSelect();
            case SELECTING_A_SONG_TO_PLAY -> displayAllSongsFromThePlaylistToSelect();
            case PLAYING_SELECTED_SONG -> displayCurrentPlayingSong();
        }
    }

    public void closeTerminal() throws IOException {
        this.screen.stopScreen();
        this.terminal.close();
    }

    private void clearScreenAndDisplayGraphics(){
        this.screen.clear();
        this.graphics = screen.newTextGraphics();
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

    private void displayPlaylistsToSelect(){
        clearScreenAndDisplayGraphics();
        graphics.putString(0,0, "Select a Playlist:");

        List<String> orderedPlaylists = new ArrayList<>(this.playListNames);

        arrowSelectionToMenu(orderedPlaylists, selectedPlaylistIndex);

    }

    private void displayAllPlaylists() {
        clearScreenAndDisplayGraphics();
        graphics.putString(0, 0, "These are all your playlists:");

        Set<String> playListNames = library.getAllPlaylistsInLibrary();

        int index = 1;
        for (String playlistName : playListNames) {
            index++;
            graphics.putString(0, index, (index - 1) + " - " + playlistName);
        }

        graphics.putString(0, index + 2, "Press 'BackSpace' to go back to menu.");
    }

    private void displayMainMenu() throws IOException {
        clearScreenAndDisplayGraphics();
        graphics.putString(0, 0, "Main Menu");

        arrowSelectionToMenu(mainMenuItens, selectedMenuItem);
    }

    private void displayPlaylistMenu() {
        clearScreenAndDisplayGraphics();
        graphics.putString(0, 0, "Selected Playlist: " + this.activePlaylist.getPlaylistName());

        arrowSelectionToMenu(playlistMenuItens, selectedPlaylistMenuItem);
    }

    private void displayAllSongsFromThePlaylistToSelect(){
        clearScreenAndDisplayGraphics();
        graphics.putString(0,0, "Choose a song to play: ");

        ArrayList<Song> allSongsFound = this.activePlaylist.showAllSongs();

        ArrayList<String> songTitles = new ArrayList<>();

        for (Song song : allSongsFound) {
            songTitles.add(song.getSongTitle());
        }

        arrowSelectionToMenu(songTitles, selectASongFromPlaylist);
    }

    private String buildProgressBar(double progressPercentage){
        int barWidth = 20;

        int filledProgressBar = (int) (progressPercentage * barWidth);
        int emptyProgressBar = barWidth - filledProgressBar;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");

        for (int i = 0; i < filledProgressBar; i++){
            stringBuilder.append("█");
        }

        for (int i = 0; i < emptyProgressBar; i++) {
            stringBuilder.append("-");
        }

        stringBuilder.append("]").append("\n");
        return stringBuilder.toString();
    }

    private String formatTime(double totalSeconds) {
        int totalSecondsInt = (int) totalSeconds;

        int minutes = totalSecondsInt / 60;
        int seconds = totalSecondsInt % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }

    private void displayAlbumArt(Song song) throws IOException {
        if (song == null) {
            return;
        }
        byte[] imageData = song.getAlbumArtData();
        if (imageData == null) {
            graphics.putString(0, 2, "[No Art]");
            return;
        }

        String base64Image = Base64.getEncoder().encodeToString(imageData);

        this.screen.setCursorPosition(new TerminalPosition(40, 2));

        System.out.print("\033_Ga=T,f=100;" + base64Image + "\033\\");
        System.out.flush();
    }

    private void displayCurrentPlayingSong() throws IOException {
        clearScreenAndDisplayGraphics();

        if (this.playingSong != null) {
            displayAlbumArt(this.playingSong);
        }

        if (this.playingSong != null) {
            graphics.putString(0, 3, "Title: " + this.playingSong.getSongTitle());
            graphics.putString(0, 4, "Artist: " + this.playingSong.getSongArtist());

            double totalDuration = musicPlayer.getTotalDurationTime();
            double currentTime = musicPlayer.getActualDurationTime();

            double progress = (totalDuration > 0) ? (currentTime / totalDuration) : 0.0;

            String progressBar = buildProgressBar(progress);
            String timeDisplay = formatTime(currentTime) + " / " + formatTime(totalDuration);

            graphics.putString(0, 6, progressBar);
            graphics.putString(0, 7, timeDisplay);
            graphics.putString(0, 9, "(p)ause | (s)top | (Backspace) Go Back");

        } else {
            graphics.putString(0, 2, "Not a song to play.");
        }
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

    private List<String> getOrderedPlaylistNames() {
        Set<String> playListNames = library.getAllPlaylistsInLibrary();
        return new ArrayList<>(playListNames);
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
                            this.currentView = UIView.SELECTING_A_PLAYLIST;
                            //this.currentView = UIView.PLAYLIST_MENU;
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

            case SELECTING_A_PLAYLIST:
                if (key.getKeyType() == KeyType.ArrowUp || key.getKeyType() == KeyType.ArrowDown) {
                    this.selectedPlaylistIndex = navigateThroughMenu(this.selectedPlaylistIndex, this.playListNames.size(), key.getKeyType());
                }

                if (key.getKeyType() == KeyType.Enter) {
                    List<String> orderedPlaylists = getOrderedPlaylistNames();
                    String selectedPlaylistName = orderedPlaylists.get(selectedPlaylistIndex);
                    this.activePlaylist = library.getPlaylistByName(selectedPlaylistName);
                    this.currentView = UIView.PLAYLIST_MENU;
                    this.selectedPlaylistMenuItem = 0;
                }

                break;

            case PLAYLIST_MENU:
                if (key.getKeyType() == KeyType.ArrowUp || key.getKeyType() == KeyType.ArrowDown) {
                    this.selectedPlaylistMenuItem = navigateThroughMenu(this.selectedPlaylistMenuItem, this.playlistMenuItens.size(), key.getKeyType());
                } else if (key.getKeyType() == KeyType.Enter) {
                    switch (selectedPlaylistMenuItem) {
                        case 3:
                            this.currentView = UIView.SELECTING_A_SONG_TO_PLAY;
                            break;
                        case 4:
                            this.currentView = UIView.MAIN_MENU;
                            break;
                    }
                }

                if (key.getKeyType() == KeyType.Backspace){
                    this.currentView = UIView.MAIN_MENU;
                }

                break;

            case SELECTING_A_SONG_TO_PLAY:
                if (key.getKeyType() == KeyType.ArrowUp || key.getKeyType() == KeyType.ArrowDown) {
                    this.selectASongFromPlaylist = navigateThroughMenu(this.selectASongFromPlaylist, this.activePlaylist.showAllSongs().size(), key.getKeyType());
                } else if (key.getKeyType() == KeyType.Enter){
                    ArrayList<Song> songs = this.activePlaylist.showAllSongs();
                    playingSong = songs.get(this.selectASongFromPlaylist);
                    this.musicPlayer.playSong(playingSong);

                    Runnable progressBarUpdater = () -> {
                        try {
                            while (musicPlayer.getStatus() == PlayerStatus.PLAYING){
                                drawCurrentView();
                                screen.refresh();
                                Thread.sleep(1000);
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    };
                    this.currentView = UIView.PLAYING_SELECTED_SONG;
                    this.progressBarUpdaterThread = new Thread(progressBarUpdater);
                    this.progressBarUpdaterThread.start();
                }
                break;

            case PLAYING_SELECTED_SONG:
                if (key.getCharacter() != null && key.getCharacter().equals('p')) {
                    musicPlayer.pause();
                }   else if ((key.getCharacter() != null && key.getCharacter().equals('s')) || key.getKeyType() == KeyType.Backspace) {
                    musicPlayer.stop();

                    if(progressBarUpdaterThread != null && progressBarUpdaterThread.isAlive()){
                        progressBarUpdaterThread.interrupt();
                    }

                    this.currentView = UIView.PLAYLIST_MENU;
                }

                if (progressBarUpdaterThread != null && progressBarUpdaterThread.isAlive()){
                    progressBarUpdaterThread.interrupt();
                }

                this.currentView = UIView.PLAYLIST_MENU;
                break;
        }
        return true;
    }
}
