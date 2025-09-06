package musicPlayer.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilesUtils {

    private String playlistFolderPath = "src/musicPlayer/songs/";

    public void createPlaylistFolderIfNotExists(String playlistName){
        Path path = Paths.get(playlistFolderPath);
        Path folderPath = path.resolve(playlistName);

        if (!Files.exists(folderPath)){
            try {
                Files.createDirectories(folderPath);
            } catch (Exception e){
                System.err.println("Error creating playlist" + playlistName + "folder on: " + playlistFolderPath);
                e.printStackTrace();
            }
        } else {
            System.out.println(playlistName + " Playlist" + " already exists!");
        }
    }

    public List<Path> readAllPlaylistFolders(){
        try(Stream<Path> paths = Files.walk(Path.of(playlistFolderPath))){
            return paths
                    .filter(Files::isDirectory)
                    .filter(name -> name != null && !name.toString().equals("songs"))
                    .collect(Collectors.toList());
        } catch (Exception e){
            System.err.println("Error searching folders on " + playlistFolderPath);
            return List.of();
        }
    }

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
