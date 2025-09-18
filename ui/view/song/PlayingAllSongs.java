package musicPlayer.ui.view.song;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import musicPlayer.player.MusicPlayer;
import musicPlayer.ui.IView;
import musicPlayer.ui.UIView;
import musicPlayer.ui.UiContext;
import musicPlayer.utils.LibraryLoader;

import java.io.IOException;

public class PlayingAllSongs implements IView {

    private MusicPlayer musicPlayer;
    private UiContext uiContext;

    public PlayingAllSongs(MusicPlayer musicPlayer, UiContext uiContext) {
        this.musicPlayer = musicPlayer;
        this.uiContext = uiContext;
    }

    @Override
    public void draw(Screen screen, TextGraphics graphics) throws IOException, InterruptedException {

        graphics.putString(18,2,"Now Playing");
        graphics.putString(10, 4, "Title: " + musicPlayer.getCurrentlyPlayingSong());

        String artist = musicPlayer.getSongArtist();
        String formatArtist = artist.length() > 20 ? artist.substring(0, 17) + "..." : artist;
        graphics.putString(10,5, "Artist(s): " + formatArtist);

        double totalDuration = musicPlayer.getTotalDurationTime();
        double currentTime = musicPlayer.getActualDurationTime();
        double progress = (totalDuration > 0) ? (currentTime / totalDuration) : 0.0;

        String progressBar = buildProgressBar(progress);
        String timeDisplay = formatTime(currentTime) + " / " + formatTime(totalDuration);

        graphics.putString(13, 7, progressBar);
        graphics.putString(17, 8, timeDisplay);

        graphics.putString(2, 10, "(p)ause | (r)esume | (s)top | (Backspace) Back");
    }

    @Override
    public UIView processInput(KeyStroke key) {
        if (key.getCharacter().equals('s') || key.getKeyType() == KeyType.Backspace ) {
            musicPlayer.stop();
            return UIView.PLAYLIST_MENU;
        }

        if (key.getCharacter() != null) {
            switch (key.getCharacter()) {
                case 'p':
                    musicPlayer.pause();
                    break;
                case 'r':
                    musicPlayer.resume();
                    break;
            }
        }
        return UIView.PLAYING_SELECTED_SONG;
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
