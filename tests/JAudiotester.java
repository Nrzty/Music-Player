package musicPlayer.tests;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import java.io.File;

public class JAudiotester {

    static String testFilePath = "src/musicPlayer/songs/Sleep Token - Gethsemane.mp3";

    public static Exception readMp3File() throws Exception {
        try {
            MP3File file = (MP3File) AudioFileIO.read(new File(testFilePath));
            Tag audioTag = file.getTag();
            System.out.println(audioTag.getFields(FieldKey.TITLE));
        } catch (Exception e){
            return e;
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        readMp3File();
    }
}
