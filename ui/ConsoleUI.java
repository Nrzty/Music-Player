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

    private final String devName = """
                                                        ___          \s
                          /\\ \\ \\_ __ ___| |_ _   _     /   \\_____   __
                         /  \\/ / '__|_  / __| | | |   / /\\ / _ \\ \\ / /
                        / /\\  /| |   / /| |_| |_| |  / /_//  __/\\ V /\s
                        \\_\\ \\/ |_|  /___|\\__|\\__, | /___,' \\___| \\_/ \s
                                             |___/                   \s
            """;

    public ConsoleUI(Library library, MusicPlayer musicPlayer) {
        this.userInputs = new UserInputs();
        this.library = library;
        this.musicPlayer = musicPlayer;
    }

    public void displayInitialOptions() {
        System.out.println(
                """
                        """ + devName + """
                        --------------------------------------------
                        1. List all playlists
                        2. Remove a specified playlist
                        3. Select a playlist
                        4. Exit
                        --------------------------------------------
                        """);
    }

    public void displayPlaylistOptions() {
        System.out.println(
                """
                        """ + devName + """
                        --------------------------------------------
                        1. List all songs in the playlist
                        2. Add a song to the playlist
                        3. Remove a song from the playlist
                        4. Play a song from the playlist
                        5. Back to main menu
                        --------------------------------------------
                        """);
    }

    public void displayAllSongsInPlaylist(String playlistName) {
        library.getAllSongsInAPlaylist(playlistName)
                .stream()
                .forEach(
                        song -> {
                            System.out.println("Song Title: " + song.getSongTitle());
                        });
    }

    public void displayAllPlaylistsInLibrary() {
        library.getAllPlaylistsInLibrary()
                .stream()
                .forEach(System.out::println);
    }

    public void startPlaylistMenu(Playlist activePlaylist) {
        boolean continueLooping = true;
        while (continueLooping) {
            displayPlaylistOptions();
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
                        String songNameToPlay = userInputs.getStringInput("Enter the name of the sound to play: ");
                        Song songToPlay = activePlaylist.getSongByTitle(songNameToPlay);
                        this.musicPlayer.playSong(songToPlay);
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
                        break;
                    case "2":
                        String playListNameForRemoval = userInputs.getStringInput("Enter the name of the playlist to remove: ");
                        System.out.println("All playlists found for removal: ");
                        displayAllPlaylistsInLibrary();
                        this.library.removePlaylistFromLibrary(playListNameForRemoval);
                        System.out.println("Playlist " + playListNameForRemoval + " removed from library");
                        break;
                    case "3":
                        String playListNameForDisplaySongs = userInputs.getStringInput("Enter the name of the playlist to display his songs: "); 
                        Playlist selectedPlaylist = this.library.getPlaylistByName(playListNameForDisplaySongs);
                        startPlaylistMenu(selectedPlaylist);
                        break;
                    default:
                        System.out.println("Invalid option");
                }
            }
        }
    }
}
