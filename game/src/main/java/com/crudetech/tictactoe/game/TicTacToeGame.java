package com.crudetech.tictactoe.game;

import com.crudetech.collections.Pair;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static com.crudetech.matcher.Verify.verifyThat;
import static org.hamcrest.Matchers.*;

public class TicTacToeGame {
    private LinearRandomAccessGrid grid = new LinearRandomAccessGrid();
    private final Player player1;
    private final Player player2;


    private Player startingPlayer;
    private Grid.Mark startingPlayersMark;

    private TicTacToeGameFsm fsm;
    private final TicTacToeGameFsm.Context contextFsmContext;

    public TicTacToeGame(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        setBackReferenceOnPlayer();

        contextFsmContext = new GameFsmContext();
        fsm = new TicTacToeGameFsm(contextFsmContext);
    }

    private void setBackReferenceOnPlayer() {
        this.player1.setGame(this);
        this.player2.setGame(this);
    }


    private Pair<Object, Grid.Mark> getOtherPlayer() {
        Pair<Object, Grid.Mark> startingPlayer = getStartingPlayer();
        Grid.Mark otherPlayersMark = startingPlayer.getSecond().getOpposite();
        return new Pair<Object, Grid.Mark>((startingPlayer.getFirst() == player1 ? player2 : player1), otherPlayersMark);
    }

    public Pair<Object, Grid.Mark> getStartingPlayer() {
        return new Pair<Object, Grid.Mark>(startingPlayer, startingPlayersMark);
    }

    public void startWithPlayer(Player startingPlayer, Grid.Mark startPlayersMark) {
        fsm.currentState().verifyNotStarted();

        this.startingPlayer = startingPlayer;
        this.startingPlayersMark = startPlayersMark;

        fsm.handleEvent(TicTacToeGameFsm.Event.Start);
    }

    public void makeMove(Player player, Grid.Location location) {
        makeMove(player, location.getRow(), location.getColumn());
    }

    public void makeMove(Player player, Grid.Row row, Grid.Column column) {
        verifyThat(grid, isNotMarkedAt(row, column));
        TicTacToeGameFsm.State state = fsm.currentState();
        state.verifyPlayersTurn(contextFsmContext, player);

        Grid.Mark activePlayersMark = state.activePlayersMark(contextFsmContext);
        placeMark(row, column, activePlayersMark);

        fsm.handleEvent(TicTacToeGameFsm.Event.Move);
    }

    private void placeMark(Grid.Row row, Grid.Column column, Grid.Mark mark) {
        grid = grid.setAt(row, column, mark);
    }

    private static Matcher<LinearRandomAccessGrid> isNotMarkedAt(final Grid.Row row, final Grid.Column column) {
        return new TypeSafeMatcher<LinearRandomAccessGrid>() {
            @Override
            protected boolean matchesSafely(LinearRandomAccessGrid grid) {
                return !grid.hasMarkAt(row, column);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(String.format("The grid was already marked at the specified location[%s, %s]", row, column));
            }
        };
    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {
        private Player startingPlayer;
        private Grid.Mark startingPlayersMark;
        private Player otherPlayer;
        private Grid grid = LinearRandomAccessGrid.empty();
        private TicTacToeGameFsm.State state = TicTacToeGameFsm.State.StartingPlayersTurn;

        public Builder withStartingPlayer(Player startingPlayer) {
            this.startingPlayer = startingPlayer;
            return this;
        }

        public Builder withStartingPlayersMark(Grid.Mark mark) {
            startingPlayersMark = mark;
            return this;
        }

        public Builder withOtherPlayer(Player otherPlayer) {
            this.otherPlayer = otherPlayer;
            return this;
        }

        public Builder withGrid(Grid grid) {
            this.grid = grid;
            return this;
        }


        public TicTacToeGame build() {
            TicTacToeGame game = new TicTacToeGame(startingPlayer, otherPlayer);
            game.startingPlayer = startingPlayer;
            game.startingPlayersMark = startingPlayersMark;
            game.grid = LinearRandomAccessGrid.of(grid);
            game.fsm = game.fsm.transferInto(state);
            return game;
        }

        public Builder withStartingPlayerWins() {
            this.state = TicTacToeGameFsm.State.OtherPlayerWins;
            return this;
        }
    }

    private class GameFsmContext implements TicTacToeGameFsm.Context {
        @Override
        public void starting() {
            verifyThat(startingPlayer(), is(anyOf(equalTo(player1), equalTo(player2))));
            verifyThat(getStartingPlayer().getSecond(), is(not(Grid.Mark.None)));

            startingPlayer().yourTurn(grid);
        }

        private Player startingPlayer() {
            return (Player) getStartingPlayer().getFirst();
        }

        @Override
        public void eval() {
            Grid.ThreeInARow triple = grid.getThreeInARow();
            if (didWin(triple)) {
                TicTacToeGameFsm.Event currentPlayerWins = fsm.previousState().currentPlayerWinsEvent();
                fsm.handleEvent(currentPlayerWins);
            } else if (grid.isTieForFirstPlayersMark(getStartingPlayer().getSecond())) {
                fsm.handleEvent(TicTacToeGameFsm.Event.Tie);
            } else {
                TicTacToeGameFsm.Event nextPlayersTurn = fsm.previousState().nextPlayersTurn();
                fsm.handleEvent(nextPlayersTurn);
            }
        }

        private boolean didWin(Grid.ThreeInARow triple) {
            return !triple.equals(Grid.ThreeInARow.Empty);
        }


        @Override
        public void switchTurnToStartingPlayer() {
            startingPlayer().yourTurn(grid);
        }

        @Override
        public void switchTurnToOtherPlayer() {
            otherPlayer().yourTurn(grid);
        }

        private Player otherPlayer() {
            return (Player) getOtherPlayer().getFirst();
        }

        @Override
        public void tie() {
            startingPlayer().tie(grid);
            otherPlayer().tie(grid);
        }

        @Override
        public void startingPlayerWins() {
            Grid.ThreeInARow triple = grid.getThreeInARow();
            startingPlayer().youWin(grid, triple);
            otherPlayer().youLoose(grid, triple);
        }

        @Override
        public void otherPlayerWins() {
            Grid.ThreeInARow triple = grid.getThreeInARow();
            startingPlayer().youLoose(grid, triple);
            otherPlayer().youWin(grid, triple);
        }

        @Override
        public void startingPlayerMoved() {
            startingPlayer().moveWasMade(grid);
        }

        @Override
        public void otherPlayerMoved() {
            otherPlayer().moveWasMade(grid);
        }

        @Override
        public Pair<Object, Grid.Mark> getStartingPlayer() {
            return TicTacToeGame.this.getStartingPlayer();
        }

        @Override
        public Pair<Object, Grid.Mark> getOtherPlayer() {
            return TicTacToeGame.this.getOtherPlayer();
        }
    }
}
