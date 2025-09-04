package musicPlayer.models;

public class Song {

    String title;
    String artist;
    String album;
    String musicalGenre;

    int duration;

    public Song(String title, String artist, String album, String musicalGenre, int duration) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.musicalGenre = musicalGenre;
        this.duration = duration;
    }

    public String getSongTitle(){
        return this.title;
    }
}
