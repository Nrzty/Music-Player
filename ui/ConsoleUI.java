package musicPlayer.ui;

import musicPlayer.models.Library;
import musicPlayer.models.Playlist;
import musicPlayer.utils.UserInputs;
import musicPlayer.player.MusicPlayer;
import musicPlayer.models.Song;

public class ConsoleUI {

    private final UserInputs userInputs;
    private final Library library;
    private MusicPlayer musicPlayer;
    private Playlist activePlaylist;

    public ConsoleUI(Library library, MusicPlayer musicPlayer) {
        this.userInputs = new UserInputs();
        this.library = library;
        this.musicPlayer = musicPlayer;
    }

    public void clearDisplay(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void displayInitialOptions() {
        System.out.println(
                """     
                        Initial Menu  \n""" + """
                        1. List all playlists
                        2. Remove a specified playlist
                        3. Select a playlist
                        4. Exit
                        """);
    }

    public void displayPlaylistOptions(String playlistName) {
        System.out.println(
                """
                        Selected Playlist -> [""" + playlistName + "]\n" + """ 
                        1. List all songs in the playlist
                        2. Add a song to the playlist
                        3. Remove a song from the playlist
                        4. Play a song from the playlist
                        5. Back to main menu
                        """);
    }

    public void displayAllSongsInPlaylist(String playlistName) {
        library.getAllSongsInAPlaylist(playlistName)
                .forEach(
                        song -> System.out.println("Song Title: " + song.getSongTitle()));
    }

    public void displayAllPlaylistsInLibrary() {
        System.out.println("""
                All playlists in library:\s
                """);
        library.getAllPlaylistsInLibrary()
                .forEach(System.out::println);
    }

    public void selectAPlaylist(){
        String playListNameForDisplaySongs = userInputs.getStringInput("\nEnter the name of the playlist to display his songs: ");
        Playlist selectedPlaylist = this.library.getPlaylistByName(playListNameForDisplaySongs);
        startPlaylistMenu(selectedPlaylist);
    }

    public void removeAPlaylist(){
        System.out.println("All playlists found for removal: ");
        displayAllPlaylistsInLibrary();
        String playListNameForRemoval = userInputs.getStringInput("Enter the name of the playlist to remove: ");
        this.library.removePlaylistFromLibrary(playListNameForRemoval);
        System.out.println("Playlist " + playListNameForRemoval + " removed from library");
    }

    public void playASongByName(){
        String songNameToPlay = userInputs.getStringInput("Enter the name of the sound to play: ");
        Song songToPlay = activePlaylist.getSongByTitle(songNameToPlay);
        this.musicPlayer.playSong(songToPlay);
    }

    public void startPlaylistMenu(Playlist activePlaylist) {
        boolean continueLooping = true;
        while (continueLooping) {
            clearDisplay();
            displayPlaylistOptions(activePlaylist.getPlaylistName());
            String inputOption = userInputs.getStringInput("Select an option: ");

            if (inputOption.equals("5")) {
                continueLooping = false;
                System.out.println("Returning to main menu...");
            } else {
                switch (inputOption) {
                    case "1":
                        displayAllSongsInPlaylist(activePlaylist.getPlaylistName());
                        break;
                    case "2":
                        System.out.println("Add song to playlist - Not implemented yet");
                        break;
                    case "3":
                        System.out.println("Remove song from playlist - Not implemented yet");
                        break;
                    case "4":
                        playASongByName();
                        break;
                    default:
                        System.out.println("Invalid option");
                }
            }
        }
    }

    public void startInicialMenu() {
        boolean continueLooping = true;
        while (continueLooping) {
            clearDisplay();
            displayInitialOptions();
            String inputOption = userInputs.getStringInput("Select an option: ");

            if (inputOption.equals("4")) {
                continueLooping = false;
                System.out.println("Exiting program...");
                break;
            } else {
                switch (inputOption) {
                    case "1":
                        displayAllPlaylistsInLibrary();
                        userInputs.waitForEnterInput();
                        break;
                    case "2":
                        removeAPlaylist();
                        break;
                    case "3":
                        displayAllPlaylistsInLibrary();
                        selectAPlaylist();
                        userInputs.waitForEnterInput();
                        break;
                    default:
                        System.out.println("Invalid option");
                }
            }
        }
    }
}
