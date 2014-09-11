package com.crudetech.tictactoe.delivery.text.jcurses;

import com.crudetech.tictactoe.game.AlphaBetaPruningPlayer;
import com.crudetech.tictactoe.game.ComputerPlayer;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.NaiveTryAndErrorPlayer;
import com.crudetech.tictactoe.ui.HumanVsComputerPlayerInteractor;
import com.crudetech.tictactoe.ui.UiPlayer;
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
        private GridWidget gridWidget;
        private HumanVsComputerPlayerInteractor humanVsComputerPlayerInteractor;

        MainWindow(int x, int y) {
            super(x, y, true, "Tic Tac Toe");
            layoutManager = createLayoutManager();
            this.gridWidget = createGridWidget(layoutManager);
            installMenu(layoutManager);
            humanVsComputerPlayerInteractor = createInteractor(new NaiveTryAndErrorPlayer());
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
            startNewGameWithComputerOpponent(AlphaBetaPruningPlayer.builder().withMark(Grid.Mark.Nought).asSecondPlayer());
        }

        private void startNewGameWithComputerOpponent(ComputerPlayer computerPlayer) {
            gridWidget.getFocus();
            humanVsComputerPlayerInteractor.destroy();
            humanVsComputerPlayerInteractor = createInteractor(computerPlayer);

            humanVsComputerPlayerInteractor.startWithHumanPlayer(Grid.Mark.Cross);
        }

        private HumanVsComputerPlayerInteractor createInteractor(ComputerPlayer computerPlayer) {
            UiPlayer humanPlayer =
                    new UiPlayer(gridWidget,
                            new JCursesMessageBoxFeedbackChannel());

            return HumanVsComputerPlayerInteractor.builder()
                    .setComputerPlayer(computerPlayer)
                    .setHumanPlayer(humanPlayer)
                    .setHumanPlayerMadeMove(gridWidget.keyDownEvent())
                    .build();
        }
    }


    public static void main(String[] args) {
        Window w = new MainWindow(50, 30);
        w.show();
    }
}
