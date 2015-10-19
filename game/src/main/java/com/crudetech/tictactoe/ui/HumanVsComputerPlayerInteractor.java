package com.crudetech.tictactoe.ui;

import com.crudetech.event.Event;
import com.crudetech.event.EventHookingBean;
import com.crudetech.event.EventListener;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.Player;
import com.crudetech.tictactoe.game.TicTacToeGame;

import java.util.Arrays;
import java.util.List;


public class HumanVsComputerPlayerInteractor {
    private final TicTacToeGame game;
    private final Player humanPlayer;
    private final Player computerPlayer;

    HumanVsComputerPlayerInteractor(Player computerPlayer, Player humanPlayer) {
        this.humanPlayer = humanPlayer;
        this.computerPlayer = computerPlayer;
        game = new TicTacToeGame(this.humanPlayer, this.computerPlayer);
    }

    public void startWithHumanPlayer(Grid.Mark mark) {
        game.startWithPlayer(humanPlayer, mark);
    }

    public void startWithComputerPlayer(Grid.Mark mark) {
        game.startWithPlayer(computerPlayer, mark);
    }

    public void destroy() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public void makeHumanPlayerMove(Grid.Location move) {
        game.makeMove(humanPlayer, move);
    }

    public void makeComputerPlayerMove(Grid.Location move) {
        game.makeMove(computerPlayer, move);
    }

    public static class Builder {
        private Player computerPlayer;
        private Event<? extends CellEventObject<?>> madeMove;
        private Player humanPlayer;


        private Builder() {
        }

        public Builder setComputerPlayer(Player computerPlayer) {
            this.computerPlayer = computerPlayer;
            return this;
        }

        public Builder setHumanPlayerMadeMove(Event<? extends CellEventObject<?>> humanPlayerMadeMove) {
            this.madeMove = humanPlayerMadeMove;
            return this;
        }


        public Builder setHumanPlayer(Player humanPlayer) {
            this.humanPlayer = humanPlayer;
            return this;
        }

        public HumanVsComputerPlayerInteractor build() {
            if (madeMove != null) {
                return new EventDrivenHumanVsComputerPlayerInteractor(computerPlayer, humanPlayer, madeMove);
            }
            return new HumanVsComputerPlayerInteractor(computerPlayer, humanPlayer);
        }
    }

    private static class EventDrivenHumanVsComputerPlayerInteractor extends HumanVsComputerPlayerInteractor {
        final EventHookingBean<? extends CellEventObject<?>> hookingBean;

        EventDrivenHumanVsComputerPlayerInteractor(Player computerPlayer, Player humanPlayer, Event<? extends CellEventObject<?>> madeMove) {
            super(computerPlayer, humanPlayer);
            hookingBean = connectHumanPlayerMove(madeMove);
        }

        private EventHookingBean<? extends CellEventObject<?>>
        connectHumanPlayerMove(com.crudetech.event.Event<? extends CellEventObject<?>> cellClickedEvent) {
            EventListener<CellEventObject<?>> cellClickedListener = new EventListener<CellEventObject<?>>() {
                @Override
                public void onEvent(CellEventObject<?> e) {
                    makeHumanPlayerMove(e.getCellLocation());
                }
            };
            return new EventHookingBean<>(cellClickedEvent, oneToList(cellClickedListener));
        }

        private List<EventListener<? super CellEventObject<?>>> oneToList(EventListener<CellEventObject<?>> cellClickedListener) {
            return Arrays.<EventListener<? super CellEventObject<?>>>asList(cellClickedListener);
        }

        @Override
        public void destroy() {
            hookingBean.destroy();
        }
    }
}
