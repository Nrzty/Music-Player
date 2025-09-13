package musicPlayer.ui;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;

public interface IView {

    void draw (Screen screen, TextGraphics graphics);

    UIView processInput(KeyStroke key);
}
