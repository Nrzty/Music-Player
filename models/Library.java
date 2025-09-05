package musicPlayer.models;

import java.util.HashMap;

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

    public void showAllPlaylistsInLibrary(){
        if (allPlaylists.isEmpty()){
            System.out.println("No playlists in library");
            return;
        }

        for (String playlistName : allPlaylists.keySet()){
            System.out.println(playlistName);
        }
    }
}
