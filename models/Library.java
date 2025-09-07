package musicPlayer.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Library {

    HashMap<String, Playlist> allPlaylists = new HashMap<>();

    public void addPlaylistIntoLibrary(Playlist playlist){
        if (playlist == null) {
            System.err.println("Playlist is null");
            return;
        }

        if (allPlaylists.containsKey(playlist.getPlaylistName())){
            System.err.println("Playlist already exists in library");
            return;
        }

        allPlaylists.put(playlist.getPlaylistName(), playlist);
    }

    public void removePlaylistFromLibrary(String playListName){
        if (playListName == null || playListName.isEmpty()){
            System.err.println("Playlist name is null or empty");
            return;
        }

        if (!allPlaylists.containsKey(playListName)){
            System.err.println("Playlist not found in library");
            return;
        }

        allPlaylists.remove(playListName);
    }

    public void showAllSongsInAPlaylist(String playlistName){
        if (playlistName == null || playlistName.isEmpty()){
            System.err.println("Playlist name is null or empty");
            return;
        }

        if (!allPlaylists.containsKey(playlistName)){
            System.err.println("Playlist not found in library");
            return;
        }

        Playlist playlist = allPlaylists.get(playlistName);
        playlist.showAllSongs();
    }

    public ArrayList<Song> getAllSongsInAPlaylist(String playlistName){
        if (playlistName == null || playlistName.isEmpty()){
            throw new RuntimeException("Playlist name is null or empty");
        }

        if (!allPlaylists.containsKey(playlistName)){
            throw new RuntimeException("Playlist not found in library");
        }

        Playlist playlist = allPlaylists.get(playlistName);
        return playlist.showAllSongs();
    }

    public Set<String> getAllPlaylistsInLibrary(){
        if (allPlaylists.isEmpty()){
            throw new RuntimeException("No playlists in library");
        }

        return allPlaylists.keySet();
    }
}
