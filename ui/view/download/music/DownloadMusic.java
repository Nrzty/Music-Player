package musicPlayer.ui.view.download.music;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import musicPlayer.ui.IView;
import musicPlayer.ui.UIView;
import musicPlayer.ui.UiContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class DownloadMusic implements IView {

    private StringBuilder inputText = new StringBuilder();
    private UiContext uiContext;
    private Screen screen;

    public DownloadMusic(UiContext uiContext, Screen screen){
        this.uiContext = uiContext;
        this.screen = screen;
    }

    private void downloadMusic(String finalPrompt) throws IOException, InterruptedException {
        if (uiContext.getFinalPrompt() != null) {

            List<String> downloadMusicPrompt = List.of("spotdl", "download", finalPrompt, "--output", uiContext.getActivePlaylist().getFolderPath());
            ProcessBuilder processBuilder = new ProcessBuilder(downloadMusicPrompt);

            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println(formatErroCode(exitCode));
        }
    }

    @Override
    public void draw(Screen screen, TextGraphics graphics) throws IOException, InterruptedException {
        graphics.putString(2, 1 , "Download Music");
        graphics.putString(2, 3, "Song Name Or URL: " + inputText.toString() + "_");
    }


    @Override
    public UIView processInput(KeyStroke key) throws InterruptedException, IOException {
        if (key.getKeyType() == KeyType.Character) {
            inputText.append(key.getCharacter());
        } else if (key.getKeyType() == KeyType.Backspace) {
               if (!inputText.isEmpty()) {
                   inputText.deleteCharAt(inputText.length() - 1);
               } else {
                   inputText.setLength(0);
                   return UIView.MAIN_MENU;
               }
        }
        else if (key.getKeyType() == KeyType.Enter) {
            String finalPrompt = inputText.toString();
            this.screen.stopScreen();

            System.out.println("Starting the search of: " + finalPrompt);

            uiContext.setFinalPrompt(finalPrompt);
            downloadMusic(uiContext.getFinalPrompt());
            System.out.println("Press 'Enter' to continue");
            System.in.read();
            screen.startScreen();

            inputText.setLength(0);
            return UIView.MAIN_MENU;
        }
        return UIView.DOWNLOADING_MUSIC;
    }

    public String formatErroCode(int exitCode){
        if (exitCode == 0) {
            return "Music Downloaded with Success";
        }
        return "An error Occurred";
    }
}
