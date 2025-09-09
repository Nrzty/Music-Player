package musicPlayer.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.File;
import java.io.InputStream;
import java.util.Properties;
import java.lang.Exception;
import java.net.URI;
import java.net.URISyntaxException;

public class FilesUtils {

    private final String playlistFolderPath;

    public FilesUtils(){
        this.playlistFolderPath = loadPlaylistPathFromConfig();
        createBaseDirectoryIfNotExists();
    }

    public String convertAStringToURI(String textToConvert){
        if (textToConvert == null) {
            System.err.println("String to convert cannot be null.");
            return null;
        }
        
        File newFilePath = new File(textToConvert);
        
        return newFilePath.toURI().toString();
    }

    private String loadPlaylistPathFromConfig(){
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            String rawPath = prop.getProperty("playlist.base.path");
        
            return rawPath.replace("${user.home}", System.getProperty("user.home"));
        } catch (Exception e) {
            System.err.println("Erro loading config.properties");
            return System.getProperty("user.home") + "/MusicPlayerData/songs/";
        }
    }

    private void createBaseDirectoryIfNotExists(){
        Path basePath = Paths.get(playlistFolderPath);
        if (!Files.exists(basePath)) {
            try {
                Files.createDirectories(basePath);
                System.out.println("Base folder created on: " + playlistFolderPath);
            } catch (Exception e) {
                System.err.println("Error creating playlists base folder");
                e.printStackTrace();
            }
        }
    }

    public void createPlaylistFolderIfNotExists(String playlistName){
        Path folderPath = Paths.get(playlistFolderPath, playlistName);
        
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
        try(Stream<Path> paths = Files.walk(Path.of(playlistFolderPath), 1)){
            return paths
                    .filter(Files::isDirectory)
                    .filter(path -> !path.equals(Paths.get(playlistFolderPath)))
                    .collect(Collectors.toList());
        } catch (Exception e){
            System.err.println("Error searching folders on " + playlistFolderPath);
            e.printStackTrace();
            return List.of();
        }
    }

    public List<Path> readAllMP3FilesOnAFolder(String folderPath){
        try(Stream<Path> paths = Files.walk(Paths.get(folderPath))){
            return paths
                    .peek(path -> System.out.println("Found: " + path))
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
