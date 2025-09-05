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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Title: ").append(title).append("\n");
        stringBuilder.append("Artist: ").append(artist).append("\n");
        stringBuilder.append("Album: ").append(album).append("\n");
        stringBuilder.append("Genre: ").append(musicalGenre).append("\n");
        stringBuilder.append("Duration: ").append(duration / 60).append(":").append(duration % 60).append(" minutes").append("\n");
        return stringBuilder.toString();
    }
}
