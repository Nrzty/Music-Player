package musicPlayer.ui.view.song;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import musicPlayer.player.MusicPlayer;
import musicPlayer.ui.IView;
import musicPlayer.ui.UIView;

import java.security.Key;

public class PlayingSong implements IView {

    private MusicPlayer musicPlayer;

    public PlayingSong(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    @Override
    public void draw(Screen screen, TextGraphics graphics) {
        graphics.putString(18, 2, "Now Playing");
        graphics.putString(9,4 ,"Title: "  + musicPlayer.getCurrentlyPlayingSong());
        graphics.putString(9,5, "Artist: " + musicPlayer.getSongArtist());

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
        if (key.getCharacter().equals('s')) {
            musicPlayer.stop();
            return UIView.PLAYLIST_MENU;
        }

        if (key.getCharacter() != null) {
            switch (key.getCharacter()) {
                case 'p':
                    musicPlayer.pause();
                case 'r':
                    musicPlayer.resume();
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
