package com.crudetech.tictactoe.ui;

import com.crudetech.event.Event;
import com.crudetech.event.EventHookingBean;
import com.crudetech.event.EventListener;
import com.crudetech.tictactoe.game.ComputerPlayer;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.Player;
import com.crudetech.tictactoe.game.TicTacToeGame;

import static java.util.Arrays.asList;

public class HumanVsComputerPlayerInteractor {
    private final TicTacToeGame game;
    private final Player humanUiPlayer;

    HumanVsComputerPlayerInteractor(ComputerPlayer computerPlayer, UiPlayer humanPlayer) {
        this.humanUiPlayer = humanPlayer;
        game = new TicTacToeGame(humanUiPlayer, computerPlayer);
    }

    public void startWithHumanPlayer(Grid.Mark mark) {
        game.startWithPlayer(humanUiPlayer, mark);
    }

    public void destroy() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ComputerPlayer computerPlayer;
        private Event<? extends CellEventObject<?>> madeMove;
        private UiPlayer humanPlayer;


        private Builder() {
        }

        public Builder setComputerPlayer(ComputerPlayer computerPlayer) {
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

    private static class EventDrivenHumanVsComputerPlayerInteractor extends HumanVsComputerPlayerInteractor{
        final EventHookingBean<? extends CellEventObject<?>> hookingBean;
        EventDrivenHumanVsComputerPlayerInteractor(ComputerPlayer computerPlayer, UiPlayer humanPlayer, Event<? extends CellEventObject<?>> madeMove) {
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
