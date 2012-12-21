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
    private final EventHookingBean<? extends CellEventObject<?>> eventHooker;

    HumanVsComputerPlayerInteractor(ComputerPlayer computerPlayer, HumanPlayer humanPlayer) {
        this(computerPlayer, humanPlayer.makeMove(), humanPlayer);
    }

    HumanVsComputerPlayerInteractor(ComputerPlayer computerPlayer, Event<? extends CellEventObject<?>> humanPlayerMadeMove, Player humanPlayer) {
        this.humanUiPlayer = humanPlayer;
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ComputerPlayer computerPlayer;
        private Event<? extends CellEventObject<?>> madeMove;
        private Player partialHumanPlayer;
        private HumanPlayer humanPlayer;


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

        public Builder setPartialHumanPlayer(Player humanPlayer) {
            this.partialHumanPlayer = humanPlayer;
            return this;
        }

        public Builder setHumanPlayer(HumanPlayer humanPlayer) {
            this.humanPlayer = humanPlayer;
            return this;
        }

        public HumanVsComputerPlayerInteractor build() {
            if (madeMove != null) {
                return new HumanVsComputerPlayerInteractor(computerPlayer, madeMove, partialHumanPlayer);
            } else {
                return new HumanVsComputerPlayerInteractor(computerPlayer, humanPlayer);
            }
        }
    }
}
