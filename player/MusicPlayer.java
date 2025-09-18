package musicPlayer.player;

import musicPlayer.models.Song;
import musicPlayer.ui.UiContext;
import musicPlayer.utils.FilesUtils;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import musicPlayer.utils.LibraryLoader;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MusicPlayer{

    private MediaPlayer mediaPlayer;

    PlayerStatus playerStatus;

    private Song currentSong;

    private LibraryLoader libraryLoader;
    private UiContext uiContext;
    private Queue<Song> songQueue = new LinkedList<>();

    public MusicPlayer(LibraryLoader libraryLoader, UiContext uiContext) {
        this.libraryLoader = libraryLoader;
        this.uiContext = uiContext;
    }

    public void playSong(Song songToPlay){
        if (songToPlay == null) {
            System.err.println("Song cannot be null");
	    }
        
	    if (mediaPlayer != null){
            stop(); 
        }
    
        FilesUtils filesUtils = new FilesUtils();
        String uriString = filesUtils.convertAStringToURI(songToPlay.getFilePath());
        Media media = new Media(uriString);

        this.currentSong = songToPlay; 
        this.mediaPlayer = new MediaPlayer(media);

        this.mediaPlayer.setOnEndOfMedia(() -> {
            if (!songQueue.isEmpty()) {
                Song nextSong = songQueue.poll();
                playSong(nextSong);
            } else {
                this.playerStatus = PlayerStatus.STOPPED;
            }
        });

        this.mediaPlayer.play();
        this.playerStatus = PlayerStatus.PLAYING;
    }

    public void playAllSongsOnPlaylist(UiContext uiContext){
        ArrayList<Song> allSongs = new ArrayList<>(libraryLoader.showAllSongsFound(uiContext.getActivePlaylist().getFolderPath()));

        songQueue.clear();
        songQueue.addAll(allSongs);

        if (!songQueue.isEmpty()) {
            playSong(songQueue.poll());
        }
    }

    public void stop(){
        if (this.playerStatus == PlayerStatus.PLAYING || this.playerStatus == PlayerStatus.PAUSED) {
            mediaPlayer.stop();
            playerStatus = PlayerStatus.STOPPED;
        }
    }

    public void pause(){
        if (this.playerStatus == PlayerStatus.PLAYING) {
            mediaPlayer.pause();
            playerStatus = PlayerStatus.PAUSED;
        }
    }

    public void resume(){
        if (this.playerStatus == PlayerStatus.PAUSED) {
            mediaPlayer.play();
            playerStatus = PlayerStatus.PLAYING;
        }
    }

    public String getCurrentlyPlayingSong(){
        if (this.playerStatus == PlayerStatus.PLAYING) {
            return this.currentSong.getSongTitle();
        }
        return "There's no song playing right now";
    }

    public String getSongArtist(){
        if (this.playerStatus == PlayerStatus.PLAYING){
            return this.currentSong.getSongArtist();
        }
        return "There's no song playing right now";
    }

    public double getTotalDurationTime(){
        return this.mediaPlayer.getTotalDuration().toSeconds();
    }

    public double getActualDurationTime(){
        return this.mediaPlayer.getCurrentTime().toSeconds();
    }

    public void setDependencies(LibraryLoader libraryLoader, UiContext uiContext) {
    }
}
