package musicPlayer.player;

import musicPlayer.models.Song;
import musicPlayer.utils.FilesUtils;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;

public class MusicPlayer{

    private MediaPlayer mediaPlayer;

    PlayerStatus playerStatus;

    private Song currentSong;

    private FilesUtils filesUtils;

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
        this.mediaPlayer.play();
        this.playerStatus = PlayerStatus.PLAYING;
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

    public PlayerStatus getStatus(){
        return this.playerStatus;
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
}
