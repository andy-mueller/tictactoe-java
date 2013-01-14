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
                final EventHookingBean<? extends CellEventObject<?>> hookingBean = connectHumanPlayerMove(madeMove, humanPlayer);
                return new HumanVsComputerPlayerInteractor(computerPlayer, humanPlayer){
                    @Override
                    public void destroy() {
                        hookingBean.destroy();
                    }
                };
            }
            return new HumanVsComputerPlayerInteractor(computerPlayer, humanPlayer);
        }
        private EventHookingBean<? extends CellEventObject<?>>
        connectHumanPlayerMove(Event<? extends CellEventObject<?>> cellClickedEvent, final UiPlayer player) {
            EventListener<CellEventObject<?>> cellClickedListener = new EventListener<CellEventObject<?>>() {
                @Override
                public void onEvent(CellEventObject<?> e) {
                    player.makeMove(e.getCellLocation());
                }
            };

            return new EventHookingBean<CellEventObject<?>>(covariant_cast(cellClickedEvent), asList(cellClickedListener));
        }

        @SuppressWarnings("unchecked")
        private Event<CellEventObject<?>> covariant_cast(Event<? extends CellEventObject<?>> cellClickedEvent) {
            return (Event<CellEventObject<?>>) cellClickedEvent;
        }
    }
}
