package musicPlayer.ui;

import musicPlayer.models.Playlist;

public class UiContext {

    private Playlist activePlaylist;

    public Playlist getActivePlaylist(){
        return activePlaylist;
    }

    public void setActivePlaylist(Playlist playlist){
        this.activePlaylist = playlist;
    }
}
