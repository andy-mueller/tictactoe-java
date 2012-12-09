package com.crudetech.tictactoe.ui;

import com.crudetech.event.Event;
import com.crudetech.event.EventHookingBean;
import com.crudetech.event.EventListener;
import com.crudetech.tictactoe.game.ComputerPlayer;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.Player;
import com.crudetech.tictactoe.game.TicTacToeGame;

import static java.util.Arrays.asList;

public abstract class HumanVsComputerPlayerInteractor {
    private final TicTacToeGame game;
    private final Player humanUiPlayer;
    private final EventHookingBean<? extends CellEventObject<?>> eventHooker;

    public HumanVsComputerPlayerInteractor(ComputerPlayer computerPlayer, Event<? extends CellEventObject<?>> humanPlayerMadeMove) {
        humanUiPlayer = createHumanUiPlayer();
        eventHooker = connectHumanPlayerMove(humanPlayerMadeMove);

        game = new TicTacToeGame(humanUiPlayer, computerPlayer);
        computerPlayer.setGame(game);
    }

    private EventHookingBean<? extends CellEventObject<?>>
            connectHumanPlayerMove(Event<? extends CellEventObject<?>> cellClickedEvent) {
        EventListener<CellEventObject<?>> cellClickedListener = new EventListener<CellEventObject<?>>() {
            @Override
            public void onEvent(CellEventObject<?> e) {
                makeHumanPlayerMove(e.getCellLocation());
            }
        };

        return new EventHookingBean<CellEventObject<?>>(covariant_cast(cellClickedEvent), asList(cellClickedListener));
    }

    @SuppressWarnings("unchecked")
    private Event<CellEventObject<?>> covariant_cast(Event<? extends CellEventObject<?>> cellClickedEvent) {
        return (Event<CellEventObject<?>>) cellClickedEvent;
    }

    public void startWithHumanPlayer(Grid.Mark mark) {
        game.startWithPlayer(humanUiPlayer, mark);
    }

    private void makeMove(Player player, Grid.Location location) {
        game.addMark(player, location);
    }

    private void makeHumanPlayerMove(Grid.Location location) {
        makeMove(humanUiPlayer, location);
    }


    public void destroy() {
        eventHooker.destroy();
    }

    private Player createHumanUiPlayer() {
        UiFeedbackChannel uiFeedback = createUiFeedback();
        UiView view = createUiView();
        return new UiPlayer(view, uiFeedback);
    }

    protected abstract UiView createUiView();

    protected abstract UiFeedbackChannel createUiFeedback();
}
