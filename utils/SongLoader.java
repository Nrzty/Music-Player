package musicPlayer.utils;

import musicPlayer.models.Song;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SongLoader {

    String filePath = "src/musicPlayer/songs/";

    private FilesUtils filesUtils;

    public SongLoader(){
        this.filesUtils = new FilesUtils();
    }

    public List<Song> readMP3File(){
        List<Path> mp3Files = filesUtils.readAllMP3FilesOnAFolder(filePath);

        List<Song> songsFound = new ArrayList<>();

        for (Path mp3Path : mp3Files) {
            try {
                MP3File file = (MP3File) AudioFileIO.read(new File(mp3Path.toString()));

                MP3AudioHeader audioHeader = file.getMP3AudioHeader();

                songsFound.add(new Song(
                        file.getTag().getFirst("TIT2"),
                        file.getTag().getFirst("TPE1"),
                        file.getTag().getFirst("TALB"),
                        file.getTag().getFirst("TCON"),
                        audioHeader.getTrackLength()
                ));

                file.getTag().getFirst("");

            } catch (Exception e) {
                System.err.println("Error reading MP3 file: " + mp3Path);
                e.printStackTrace();
            }
        }
        return songsFound;
    }
}
