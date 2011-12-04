package com.crudetech.tictactoe.client.jcurses;

import com.crudetech.event.EventListener;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.NaiveTryAndErrorPlayer;
import com.crudetech.tictactoe.game.TicTacToeGame;
import jcurses.event.ItemEvent;
import jcurses.event.ItemListener;
import jcurses.system.CharColor;
import jcurses.widgets.DefaultLayoutManager;
import jcurses.widgets.MenuList;
import jcurses.widgets.WidgetsConstants;
import jcurses.widgets.Window;

public class JCursesApp {
    static class MainWindow extends Window {
        private final DefaultLayoutManager layoutManager;
        private TicTacToeGame game;
        private GridWidget gridWidget;
        private GridWidgetPlayer gridWidgetPlayer;
        private NaiveTryAndErrorPlayer computerPlayer;

        MainWindow(int x, int y) {
            super(x, y, true, "Tic Tac Toe");
            layoutManager = createLayoutManager();
            this.gridWidget = createGridWidget(layoutManager);
            installMenu(layoutManager);
            hookGridWidgetsKeyDownEventToAddAMarkOfTheGame();
        }

        private void hookGridWidgetsKeyDownEventToAddAMarkOfTheGame() {
            gridWidget.keyDownEvent().addListener(new EventListener<GridWidget.KeyDownEventObject>() {
                @Override
                public void onEvent(GridWidget.KeyDownEventObject e) {
                    game.addMark(gridWidgetPlayer, e.getLocation());
                }
            });
        }

        private void installMenu(DefaultLayoutManager layoutManager) {
            final String NewGame = "New Game";
            final String Exit = "Exit";

            MenuList menu = new MenuList();
            menu.add(NewGame);
            menu.add(Exit);
            menu.setSelectable(true);
            menu.setTitle("File");
            menu.setTitleColors(new CharColor(menu.getTitleColors().getBackground(), CharColor.NORMAL));
            layoutManager.addWidget(menu, 0, 0, 50, 3, WidgetsConstants.ALIGNMENT_TOP, WidgetsConstants.ALIGNMENT_LEFT);
            menu.addListener(new ItemListener() {
                @SuppressWarnings({"CallToStringEqualsIgnoreCase"})
                @Override
                public void stateChanged(ItemEvent itemEvent) {
                    String item = (String) itemEvent.getItem();
                    if (NewGame.equalsIgnoreCase(item)) {
                        onMnuNewGame();
                    } else if (Exit.equalsIgnoreCase(item)) {
                    }
                }
            });
        }

        private static GridWidget createGridWidget(DefaultLayoutManager layoutManager) {
            GridWidget w = new GridWidget();
            w.setCursorColors(new CharColor(CharColor.RED, CharColor.WHITE));
            layoutManager.addWidget(w, 1, 5, 12, 5, WidgetsConstants.ALIGNMENT_TOP, WidgetsConstants.ALIGNMENT_CENTER);
            return w;
        }

        private DefaultLayoutManager createLayoutManager() {
            DefaultLayoutManager mgr = new DefaultLayoutManager();
            mgr.bindToContainer(getRootPanel());
            return mgr;
        }

        private void onMnuNewGame() {
            gridWidget.getFocus();
            setupNewGame();
            game.startWithPlayer(gridWidgetPlayer, Grid.Mark.Cross);
        }

        private void setupNewGame() {
            UserFeedbackChannel userFeedback = new JCursesMessageBoxFeedbackChannel();
            gridWidgetPlayer = new GridWidgetPlayer(gridWidget, userFeedback);
            computerPlayer = new NaiveTryAndErrorPlayer();
            game = new TicTacToeGame(gridWidgetPlayer, computerPlayer);
            computerPlayer.setGame(game);
        }

    }


    public static void main(String[] args) {
        Window w = new MainWindow(50, 30);
        w.show();
    }
}
