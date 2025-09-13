package musicPlayer.models;

import java.util.ArrayList;
import java.util.List;

public class Playlist {

    private String playlistName;
    public ArrayList<Song> playlist = new ArrayList<Song>();
    private Song song;

    public Playlist(String playlistName){
        this.playlistName = playlistName;
    }

    public Song getSongByTitle(String songTitle){
        for (Song song : playlist) {
           if (song.getSongTitle().equalsIgnoreCase(songTitle)) {
               return song;
           }
        }   
        return null;
    }

    public List<Song> getAllSongsOnPlaylist() {
        return playlist;
    }

    public String getPlaylistName(){
        return this.playlistName;
    }

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

    public ArrayList<Song> showAllSongs(){
        return this.playlist;
    }
}
