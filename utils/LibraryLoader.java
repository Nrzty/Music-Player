package musicPlayer.utils;

import musicPlayer.models.Library;
import musicPlayer.models.Playlist;
import musicPlayer.models.Song;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LibraryLoader {
  
    private FilesUtils filesUtils;

    public LibraryLoader(){
        this.filesUtils = new FilesUtils();
    }

    public Library loadAllPlaylistsFound() throws Exception {
        Library library = new Library();
        List<Path> playlistFolders = filesUtils.readAllPlaylistFolders();

        for (Path playlistFolder : playlistFolders) {
            String playlistName = playlistFolder.getFileName().toString();
            Playlist actualPlaylist = new Playlist(playlistName);

            List<Song> songs = this.loadSongsFound(playlistFolder.toString());

            for (Song song : songs){
                actualPlaylist.addSong(song);
            }
            library.addPlaylistIntoLibrary(actualPlaylist);
        }
        return library;
    }

    public List<Song> showAllSongsFound(String folderPath){
        return this.loadSongsFound(folderPath);
    }

    public List<Song> loadSongsFound(String folderPath){
        List<Path> mp3Files = filesUtils.readAllMP3FilesOnAFolder(folderPath);

        List<Song> songsFound = new ArrayList<>();

        for (Path mp3Path : mp3Files) {
            try {
                MP3File file = (MP3File) AudioFileIO.read(mp3Path.toFile());

                MP3AudioHeader audioHeader = file.getMP3AudioHeader();

                songsFound.add(new Song(
                        file.getTag().getFirst("TIT2"),
                        file.getTag().getFirst("TPE1"),
                        file.getTag().getFirst("TALB"),
                        file.getTag().getFirst("TCON"),
                        audioHeader.getTrackLength()
                ));

            } catch (Exception e) {
                System.err.println("Error reading MP3 file: " + mp3Path);
                e.printStackTrace();
            }
        }
        return songsFound;
    }
}
