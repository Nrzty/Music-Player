package musicPlayer.models;

import java.util.ArrayList;

public class Playlist {

    ArrayList<Song> playlist = new ArrayList<Song>();

    public void isSongNull(Song song) throws Exception {
        if (song == null) {
            throw new Exception("Song is null");
        }
    }

    public void addSong(Song song) throws Exception {
        isSongNull(song);

        playlist.add(song);
    }

    public void removeSong(Song song) throws Exception{
        isSongNull(song);

        if (playlist.contains(song)) {
            playlist.remove(song);
        } else {
            throw new Exception("Song not found in playlist");
        }
    }

    public void showAllSongs(){
        for (Song song : playlist){
            System.out.println(song.getSongTitle());
        }
    }
}
