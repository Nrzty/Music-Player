package musicPlayer.utils;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class ReadMP3File {

    String filePath = "src/musicPlayer/songs/";

    private FilesUtils filesUtils;

    public ReadMP3File(){
        this.filesUtils = new FilesUtils();
    }

    public void readMP3File(){
        List<Path> mp3Files = filesUtils.readAllMP3FilesOnAFolder(filePath);

        for (Path mp3Path : mp3Files) {
            try {
                MP3File file = (MP3File) AudioFileIO.read(new File(mp3Path.toString()));

                MP3AudioHeader audioHeader = file.getMP3AudioHeader();

                System.out.println("Title: " + file.getTag().getFirst("TIT2"));
                System.out.println("Artist: " + file.getTag().getFirst("TPE1"));
                System.out.println("Album: " + file.getTag().getFirst("TALB"));
                System.out.println("Track Length: " + audioHeader.getTrackLength() / 60 + ":" + audioHeader.getTrackLength() % 60 + " minutes");
                System.out.println("Musical Genre: " + file.getTag().getFirst("TCON"));
                file.getTag().getFirst("");
                System.out.println("---------------------------");
            } catch (Exception e) {
                System.err.println("Error reading MP3 file: " + mp3Path);
                e.printStackTrace();
            }
        }
    }
}
