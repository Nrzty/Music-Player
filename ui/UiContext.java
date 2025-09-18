package musicPlayer.ui;

import musicPlayer.models.Playlist;

public class UiContext {

    private Playlist activePlaylist;
    private String finalPrompt;

    public Playlist getActivePlaylist(){
        return activePlaylist;
    }

    public void setActivePlaylist(Playlist playlist){
        this.activePlaylist = playlist;
    }

    public String getFinalPrompt(){
        return finalPrompt;
    }

    public void setFinalPrompt(String finalPrompt){
        this.finalPrompt = finalPrompt;
    }
}
