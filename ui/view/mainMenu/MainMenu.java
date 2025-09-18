package musicPlayer.ui.view.mainMenu;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import musicPlayer.ui.IView;
import musicPlayer.ui.UIView;

import java.util.List;

public class MainMenu implements IView {

    private int selectedMenuItem = 0;
    private final List<String> mainMenuItens = List.of("Show Playlists", "Select Playlist", "Create a Playlist", "Exit");

    @Override
    public void draw(Screen screen, TextGraphics graphics){
        graphics.putString(2,1, "Main Menu");
        drawSelectionList(graphics, mainMenuItens, selectedMenuItem, 2, 3);
    }

    @Override
    public UIView processInput(KeyStroke key) {
        if (key.getKeyType() == KeyType.ArrowUp || key.getKeyType() == KeyType.ArrowDown) {
            this.selectedMenuItem = navigateList(this.selectedMenuItem, this.mainMenuItens.size(), key.getKeyType());
        } else if (key.getKeyType() == KeyType.Enter) {
            switch (selectedMenuItem) {
                case 0:
                    return UIView.PLAYLIST_LIST_VIEW;
                case 1:
                    return UIView.SELECTING_A_PLAYLIST;
                case 2:
                    return UIView.CREATING_A_PLAYLIST;
                case 3:
                    return null;
            }
        }
        return UIView.MAIN_MENU;
    }

    private int navigateList(int currentSelection, int listSize, KeyType key){
        if (key == KeyType.ArrowUp) {
            currentSelection = Math.max(0, currentSelection - 1);
        } else if (key == KeyType.ArrowDown) {
            currentSelection = Math.min(listSize - 1, currentSelection + 1);
        }
        return currentSelection;
    }

    private void drawSelectionList(TextGraphics graphics, List<String> items, int selectedIndex, int x, int y) {
        for (int i = 0; i < items.size(); i++) {
            String prefix = (i == selectedIndex) ? "-> " : "   ";
            graphics.putString(x, y + i, prefix + items.get(i));
        }
    }
}
