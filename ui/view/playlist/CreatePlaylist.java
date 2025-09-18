package musicPlayer.ui.view.playlist;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import musicPlayer.ui.IView;
import musicPlayer.ui.UIView;
import musicPlayer.utils.FilesUtils;

import java.io.IOException;

public class CreatePlaylist implements IView {

    private StringBuilder inputText = new StringBuilder();
    private Screen screen;
    private FilesUtils filesUtils;

    public CreatePlaylist(Screen screen, FilesUtils filesUtils){
        this.screen = screen;
        this.filesUtils = filesUtils;
    }

    @Override
    public void draw(Screen screen, TextGraphics graphics) throws IOException, InterruptedException {
        graphics.putString(2,1, "Create Playlist Folder");
        graphics.putString(2,3,"Insert Playlist Name: " + inputText.toString() + "_");
    }

    @Override
    public UIView processInput(KeyStroke key) throws InterruptedException, IOException {
        if (key.getKeyType() == KeyType.Character) {
            inputText.append(key.getCharacter());
        } else if (key.getKeyType() == KeyType.Backspace) {
            if (!inputText.isEmpty()) {
                inputText.deleteCharAt(inputText.length() -1);
            } else {
                inputText.setLength(0);
                return UIView.MAIN_MENU;
            }
        }
        else if (key.getKeyType() == KeyType.Enter) {
            String finalPrompt = inputText.toString();
            this.screen.stopScreen();

            filesUtils.createAPlaylistDirectory(finalPrompt);

            System.out.println("Press 'Enter' to continue");
            System.in.read();
            screen.startScreen();
            inputText.setLength(0);
            return UIView.MAIN_MENU;
        }
        return UIView.CREATING_A_PLAYLIST;
    }
}
