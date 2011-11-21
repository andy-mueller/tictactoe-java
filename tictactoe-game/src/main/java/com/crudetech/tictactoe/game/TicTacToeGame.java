package com.crudetech.tictactoe.game;

import static com.crudetech.matcher.Verify.verifyThat;
import static org.hamcrest.Matchers.*;

public class TicTacToeGame {

    private LinearRandomAccessGrid grid = new LinearRandomAccessGrid();
    private final Player player1;
    private final Player player2;
    private Player currentPlayer;
    private Grid.Mark currentPlayersMark;
    private boolean finished = false;
    private Grid.Mark startingPlayersMark;

    public TicTacToeGame(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    static class GameIsFinishedException extends IllegalStateException {
        private GameIsFinishedException() {
            super("The game is finished!");
        }
    }

    static class GameIsAlreadyStartedException extends IllegalStateException {
        private GameIsAlreadyStartedException() {
            super("The was already started!");
        }
    }

    Grid getGrid() {
        return grid;
    }

    public void addMark(Player player, Grid.Location location) {
        addMark(player, location.getRow(), location.getColumn());
    }

    public void addMark(Player player, Grid.Row row, Grid.Column column) {
        verifyThat(row, is(notNullValue()));
        verifyThat(column, is(notNullValue()));

        if (finished) {
            throw new GameIsFinishedException();
        }
        verifyThat(player, sameInstance(currentPlayer));
        if (grid.hasMarkAt(row, column)) {
            throw new IllegalArgumentException(String.format("The grid was already marked at the specified location[%s, %s]", row, column));
        }

        grid.setAt(row, column, currentPlayersMark);
        Grid.Triple triple = grid.winningTriple();
        if (didWin(triple)) {
            currentPlayer.youWin(grid, triple);
            getOtherPlayer().youLoose(grid, triple);
            finished = true;
        } else if (grid.isTieForFirstPlayersMark(startingPlayersMark)) {
            player1.tie(grid);
            player2.tie(grid);
            finished = true;
        } else {
            currentPlayer = currentPlayer == player1 ? player2 : player1;
            currentPlayersMark = currentPlayersMark == Grid.Mark.Cross ? Grid.Mark.Nought : Grid.Mark.Cross;
            currentPlayer.yourTurn(grid);
        }
    }


    private Player getOtherPlayer() {
        return currentPlayer == player1 ? player2 : player1;
    }

    private boolean didWin(LinearRandomAccessGrid.Triple triple) {
        return !triple.equals(Grid.Triple.Empty);
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
}
