package com.crudetech.tictactoe.game;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static com.crudetech.matcher.Verify.verifyThat;
import static org.hamcrest.Matchers.*;

public class TicTacToeGame {

    private final LinearRandomAccessGrid grid = new LinearRandomAccessGrid();
    private final Player player1;
    private final Player player2;

    private Player currentPlayer;
    private Grid.Mark currentPlayersMark;

    private boolean finished = false;
    private Grid.Mark startingPlayersMark;

    public TicTacToeGame(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        setBackReferenceOnPlayer();
    }

    private void setBackReferenceOnPlayer() {
        this.player1.setGame(this);
        this.player2.setGame(this);
    }

    static class GameIsFinishedException extends IllegalStateException {
        private GameIsFinishedException() {
            super("The game is finished!");
        }
    }

    static class GameIsAlreadyStartedException extends IllegalStateException {
        private GameIsAlreadyStartedException() {
            super("The game was already started!");
        }
    }

    static class NotThisPlayersTurnException extends IllegalStateException {
        private NotThisPlayersTurnException() {
            super("It is not the passed in players turn!");
        }
    }

    public void startWithPlayer(Player player, Grid.Mark playersMark) {
        verifyThat(player, is(anyOf(equalTo(player1), equalTo(player2))));
        verifyThat(playersMark, is(not(Grid.Mark.None)));

        if (currentPlayer != null) {
            throw new GameIsAlreadyStartedException();
        }
        currentPlayer = player;
        startingPlayersMark = playersMark;
        currentPlayersMark = playersMark;
        currentPlayer.yourTurn(grid);
    }

    public void makeMove(Player player, Grid.Location location) {
        makeMove(player, location.getRow(), location.getColumn());
    }

    public void makeMove(Player player, Grid.Row row, Grid.Column column) {
        verifyGameIsNotFinished();
        verifyThatItIsPlayersTurn(player);
        verifyThat(grid, isNotMarkedAt(row, column));

        grid.setAt(row, column, currentPlayersMark);
        Grid.ThreeInARow triple = grid.getThreeInARow();
        if (didWin(triple)) {
            currentPlayer.youWin(grid, triple);
            getOtherPlayer().youLoose(grid, triple);
            finished = true;
        } else if (grid.isTieForFirstPlayersMark(startingPlayersMark)) {
            player1.tie(grid);
            player2.tie(grid);
            finished = true;
        } else {
            flipCurrentPlayer();
            currentPlayer.yourTurn(grid);
        }
    }

    private void flipCurrentPlayer() {
        currentPlayer = currentPlayer == player1 ? player2 : player1;
        currentPlayersMark = currentPlayersMark.getOpposite();
    }

    private void verifyThatItIsPlayersTurn(Player player) {
        if (currentPlayer != player) {
            throw new NotThisPlayersTurnException();
        }
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

    private void verifyGameIsNotFinished() {
        if (finished) {
            throw new GameIsFinishedException();
        }
    }

    private Player getOtherPlayer() {
        return currentPlayer == player1 ? player2 : player1;
    }

    private boolean didWin(Grid.ThreeInARow triple) {
        return !triple.equals(Grid.ThreeInARow.Empty);
    }
}
