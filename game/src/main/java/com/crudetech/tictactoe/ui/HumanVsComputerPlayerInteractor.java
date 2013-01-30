package com.crudetech.tictactoe.ui;

import com.crudetech.event.Event;
import com.crudetech.event.EventHookingBean;
import com.crudetech.event.EventListener;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.Player;
import com.crudetech.tictactoe.game.TicTacToeGame;

import static java.util.Arrays.asList;

public class HumanVsComputerPlayerInteractor {
    private final TicTacToeGame game;
    private final Player humanPlayer;
    private final Player computerPlayer;

    HumanVsComputerPlayerInteractor(Player computerPlayer, UiPlayer humanPlayer) {
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
        game.addMark(humanPlayer, move);
    }

    public void makeComputerPlayerMove(Grid.Location move) {
           game.addMark(computerPlayer, move);
    }

    public static class Builder {
        private Player computerPlayer;
        private Event<? extends CellEventObject<?>> madeMove;
        private UiPlayer humanPlayer;


        private Builder() {
        }

        public Builder setComputerPlayer(Player computerPlayer) {
            this.computerPlayer = computerPlayer;
            return this;
        }

        public Builder setMadeMove(Event<? extends CellEventObject<?>> humanPlayerMadeMove) {
            this.madeMove = humanPlayerMadeMove;
            return this;
        }


        public Builder setHumanPlayer(UiPlayer humanPlayer) {
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

        EventDrivenHumanVsComputerPlayerInteractor(Player computerPlayer, UiPlayer humanPlayer, Event<? extends CellEventObject<?>> madeMove) {
            super(computerPlayer, humanPlayer);
            hookingBean = connectHumanPlayerMove(madeMove, humanPlayer);
        }

        private EventHookingBean<? extends CellEventObject<?>>
        connectHumanPlayerMove(com.crudetech.event.Event<? extends CellEventObject<?>> cellClickedEvent, final UiPlayer player) {
            EventListener<CellEventObject<?>> cellClickedListener = new EventListener<CellEventObject<?>>() {
                @Override
                public void onEvent(CellEventObject<?> e) {
                    player.makeMove(e.getCellLocation());
                }
            };

            return new EventHookingBean<CellEventObject<?>>(covariant_cast(cellClickedEvent), asList(cellClickedListener));
        }

        @SuppressWarnings("unchecked")
        private com.crudetech.event.Event<CellEventObject<?>> covariant_cast(com.crudetech.event.Event<? extends CellEventObject<?>> cellClickedEvent) {
            return (com.crudetech.event.Event<CellEventObject<?>>) cellClickedEvent;
        }

        @Override
        public void destroy() {
            hookingBean.destroy();
        }
    }
}
