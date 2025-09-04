package musicPlayer;

import musicPlayer.ui.ConsoleUI;
import musicPlayer.utils.ReadMP3File;

public class Main {
    public static void main(String[] args){
        ReadMP3File readMP3File = new ReadMP3File();
        readMP3File.readMP3File();
        // ConsoleUI consoleUI = new ConsoleUI();
        // consoleUI.startProgram();
    }
}
