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

    public void setTitle(String title){
        this.title = title;
    }

    public void setArtist(String artist){
        this.artist = artist;
    }

    public void setAlbum(String album){
        this.album = album;
    }

    public void setMusicalGenre(String musicalGenre){
        this.musicalGenre = musicalGenre;
    }

    public void setDuration(int duration){
        this.duration = duration;
    }
}
