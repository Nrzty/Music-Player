package musicPlayer.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilesUtils {

    public List<Path> readAllMP3FilesOnAFolder(String folderPath){
        try(Stream<Path> paths = Files.walk(Paths.get(folderPath))){
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().endsWith(".mp3"))
                    .collect(Collectors.toList());
        } catch (Exception e){
            System.err.println("Error reading files on folder: " + folderPath);
            e.printStackTrace();
            return List.of();
        }
    }
}
