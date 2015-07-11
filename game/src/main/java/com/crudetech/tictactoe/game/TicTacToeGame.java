package com.crudetech.tictactoe.game;

import com.crudetech.collections.Pair;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static com.crudetech.matcher.Verify.verifyThat;
import static org.hamcrest.Matchers.*;

public class TicTacToeGame {
    private final TicTacToeGameFsm.Context contextFsmContext;
    private LinearRandomAccessGrid grid = new LinearRandomAccessGrid();
    private final Player player1;
    private final Player player2;


    private Player startingPlayer;
    private Grid.Mark startingPlayersMark;

    private final TicTacToeGameFsm fsm;


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


    private Player getOtherPlayer() {
        return getStartingPlayer() == player1 ? player2 : player1;
    }
    private Grid.Mark getOtherPlayersMark() {
        return getStartingPlayersMark().getOpposite();
    }

    public  Player getStartingPlayer() {
        return startingPlayer;
    }
    public Grid.Mark getStartingPlayersMark() {
        return startingPlayersMark;
    }


    private boolean wasFirstPlayersTurn() {
        return fsm.previousState() == TicTacToeGameFsm.State.StartingPlayersTurn;
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
        TicTacToeGameFsm.State state = fsm.currentState();
        verifyThat(grid, isNotMarkedAt(row, column));
        state.verifyPlayersTurn(contextFsmContext, player);

        placeMark(row, column, state.activePlayersMark(contextFsmContext));

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

    private boolean didWin(Grid.ThreeInARow triple) {
        return !triple.equals(Grid.ThreeInARow.Empty);
    }

    private class GameFsmContext implements TicTacToeGameFsm.Context {
        @Override
        public void starting() {
            verifyThat(TicTacToeGame.this.getStartingPlayer(), is(anyOf(equalTo(player1), equalTo(player2))));
            verifyThat(getStartingPlayersMark(), is(not(Grid.Mark.None)));

            TicTacToeGame.this.getStartingPlayer().yourTurn(grid);
        }

        @Override
        public void eval() {
            Grid.ThreeInARow triple = grid.getThreeInARow();
            if (didWin(triple)) {
                if (wasFirstPlayersTurn())
                    fsm.handleEvent(TicTacToeGameFsm.Event.StartingPlayerWins);
                else
                    fsm.handleEvent(TicTacToeGameFsm.Event.OtherPlayerWins);
            } else if (grid.isTieForFirstPlayersMark(getStartingPlayersMark())) {
                fsm.handleEvent(TicTacToeGameFsm.Event.Tie);
            } else {
                if (wasFirstPlayersTurn())
                    fsm.handleEvent(TicTacToeGameFsm.Event.SwitchToOtherPlayer);
                else
                    fsm.handleEvent(TicTacToeGameFsm.Event.SwitchToStartingPlayer);
            }
        }

        @Override
        public void switchTurnToStartingPlayer() {
            TicTacToeGame.this.getOtherPlayer().moveWasMade(grid);
            TicTacToeGame.this.getStartingPlayer().yourTurn(grid);
        }

        @Override
        public void switchTurnToOtherPlayer() {
            TicTacToeGame.this.getStartingPlayer().moveWasMade(grid);
            TicTacToeGame.this.getOtherPlayer().yourTurn(grid);
        }

        @Override
        public void tie() {
            TicTacToeGame.this.getStartingPlayer().tie(grid);
            TicTacToeGame.this.getOtherPlayer().tie(grid);
        }

        @Override
        public void startingPlayerWins() {
            Grid.ThreeInARow triple = grid.getThreeInARow();
            TicTacToeGame.this.getStartingPlayer().youWin(grid, triple);
            TicTacToeGame.this.getOtherPlayer().youLoose(grid, triple);
        }

        @Override
        public void otherPlayerWins() {
            Grid.ThreeInARow triple = grid.getThreeInARow();
            TicTacToeGame.this.getStartingPlayer().youLoose(grid, triple);
            TicTacToeGame.this.getOtherPlayer().youWin(grid, triple);
        }

        @Override
        public Pair<Player, Grid.Mark> getStartingPlayer() {
            return new Pair<>(TicTacToeGame.this.getStartingPlayer(), getStartingPlayersMark());
        }

        @Override
        public Pair<Player, Grid.Mark> getOtherPlayer() {
            return new Pair<>(TicTacToeGame.this.getOtherPlayer(), getOtherPlayersMark());
        }

    }
}
