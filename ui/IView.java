package musicPlayer.ui;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;

public interface IView {

    void draw (Screen screen, TextGraphics graphics) throws IOException, InterruptedException;

    UIView processInput(KeyStroke key) throws InterruptedException, IOException;
}
