package musicPlayer.ui;

import musicPlayer.models.Library;
import musicPlayer.utils.UserInputs;

public class ConsoleUI {

    private UserInputs userInputs;
    private Library library;

    public ConsoleUI(Library library){
        this.userInputs = new UserInputs();
        this.library = library;
    }

    public void displayOptions(){
        System.out.println(
                """
                             __          _              ___          \s
                          /\\ \\ \\_ __ ___| |_ _   _     /   \\_____   __
                         /  \\/ / '__|_  / __| | | |   / /\\ / _ \\ \\ / /
                        / /\\  /| |   / /| |_| |_| |  / /_//  __/\\ V /\s
                        \\_\\ \\/ |_|  /___|\\__|\\__, | /___,' \\___| \\_/ \s
                                             |___/                   \s
                        --------------------------------------------
                        1. List all playlists
                        2. Remove a specified playlist
                        3. Select a playlist
                        4. Exit
                        --------------------------------------------
                        """
        );
    }

    public void startProgram(){
        boolean continueLooping = true;
        while(continueLooping){
            displayOptions();
            String inputOption = userInputs.getStringInput("Select an option: ");

            if (inputOption.equals("4")){
                continueLooping = false;
                System.out.println("Exiting program...");
            } else {
                switch (inputOption){
                    case "1":
                        this.library.showAllPlaylistsInLibrary();
                        break;
                    case "2":
                        String playlistNameForRemoval = userInputs.getStringInput("Enter the name of the playlist to remove: ");
                        System.out.println("All playlists found for removal: ");
                        this.library.showAllPlaylistsInLibrary();
                        this.library.removePlaylistFromLibrary(playlistNameForRemoval);
                        System.out.println("Playlist " + playlistNameForRemoval + " removed from library");
                        break;
                    case "3":
                        String playlistNameForListAllSongs = userInputs.getStringInput("Enter the name of the playlist to list his songs: ");
                        System.out.println("All songs found in " + playlistNameForListAllSongs + " playlist: ");
                        this.library.showAllSongsInAPlaylist(playlistNameForListAllSongs);
                        break;
                    default:
                        System.out.println("Invalid option");
                }
            }
        }
    }
}
