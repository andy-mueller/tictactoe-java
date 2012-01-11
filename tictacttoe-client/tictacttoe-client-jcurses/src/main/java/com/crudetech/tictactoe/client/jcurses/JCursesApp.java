package com.crudetech.tictactoe.client.jcurses;

import com.crudetech.event.EventListener;
import com.crudetech.tictactoe.game.*;
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
        private UiPlayer gridWidgetPlayer;
        EventListener<GridWidget.KeyDownEventObject> keyDownListener = new EventListener<GridWidget.KeyDownEventObject>() {
            @Override
            public void onEvent(GridWidget.KeyDownEventObject e) {}
        };

        MainWindow(int x, int y) {
            super(x, y, true, "Tic Tac Toe");
            layoutManager = createLayoutManager();
            this.gridWidget = createGridWidget(layoutManager);
            installMenu(layoutManager);
            hookGridWidgetsKeyDownEventToAddAMarkOfTheGame();
        }

        private void hookGridWidgetsKeyDownEventToAddAMarkOfTheGame() {
            gridWidget.keyDownEvent().addListener(keyDownListener);
        }

        private void installMenu(DefaultLayoutManager layoutManager) {
            final String NewEasyGame = "New Easy Game";
            final String NewAdvancedGame = "New Advanced Game";
            final String Exit = "Exit";

            MenuList menu = new MenuList();
            menu.add(NewEasyGame);
            menu.add(NewAdvancedGame);
            menu.add(Exit);
            menu.setSelectable(true);
            menu.setTitle("Games");
            menu.setTitleColors(new CharColor(menu.getTitleColors().getBackground(), CharColor.NORMAL));
            layoutManager.addWidget(menu, 0, 0, 50, 3, WidgetsConstants.ALIGNMENT_TOP, WidgetsConstants.ALIGNMENT_LEFT);
            menu.addListener(new ItemListener() {
                @SuppressWarnings({"CallToStringEqualsIgnoreCase"})
                @Override
                public void stateChanged(ItemEvent itemEvent) {
                    String item = (String) itemEvent.getItem();
                    if (NewEasyGame.equalsIgnoreCase(item)) {
                        onMnuNewEasyGame();
                    } else if (NewAdvancedGame.equalsIgnoreCase(item)) {
                        onMnuNewAdvancedGame();
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

        private void onMnuNewEasyGame() {
            startNewGameWithComputerOpponent(new NaiveTryAndErrorPlayer());
        }

        private void onMnuNewAdvancedGame() {
            startNewGameWithComputerOpponent(new AlphaBetaPruningPlayer(Grid.Mark.Nought));
        }

        private void startNewGameWithComputerOpponent(ComputerPlayer computerPlayer) {
            gridWidget.getFocus();
            gridWidget.keyDownEvent().removeListener(keyDownListener);
            UiFeedbackChannel uiFeedback = new JCursesMessageBoxFeedbackChannel();
            gridWidgetPlayer = new UiPlayer(gridWidget, uiFeedback);

            game = new TicTacToeGame(gridWidgetPlayer, computerPlayer);
            computerPlayer.setGame(game);
            keyDownListener = new EventListener<GridWidget.KeyDownEventObject>() {
                @Override
                public void onEvent(GridWidget.KeyDownEventObject e) {
                    game.addMark(gridWidgetPlayer, e.getLocation());
                }
            };
            gridWidget.keyDownEvent().addListener(keyDownListener);
            game.startWithPlayer(gridWidgetPlayer, Grid.Mark.Cross);
        }
    }


    public static void main(String[] args) {
        Window w = new MainWindow(50, 30);
        w.show();
    }
}
