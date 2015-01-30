package com.crudetech.tictactoe.game;

/**
 * Creates a test games
 */
class TicTacToeGameMother {
    void setupAlmostFinishedGame(Player first, Player second, TicTacToeGame game) {
        game.makeMove(first, Grid.Row.Second, Grid.Column.First);
        game.makeMove(second, Grid.Row.First, Grid.Column.Third);

        game.makeMove(first, Grid.Row.Third, Grid.Column.First);
        game.makeMove(second, Grid.Row.Second, Grid.Column.Third);

        game.makeMove(first, Grid.Row.Second, Grid.Column.Second);
        game.makeMove(second, Grid.Row.Third, Grid.Column.Second);
    }

    /**
     * Creates a game that is still open for both players to win or to produce a tie:
     * |   | O
     * ---+---+---
     * X | X | O
     * ---+---+---
     * X | O |
     * <p/>
     * It is the first players turn now, using the 'X' mark
     */
    TicTacToeGame createOpenAndAlmostFinishedGame(Player first, Player second) {
        TicTacToeGame game = new TicTacToeGame(first, second);
        game.startWithPlayer(first, Grid.Mark.Cross);

        game.makeMove(first, Grid.Row.Second, Grid.Column.First);
        game.makeMove(second, Grid.Row.First, Grid.Column.Third);

        game.makeMove(first, Grid.Row.Third, Grid.Column.First);
        game.makeMove(second, Grid.Row.Second, Grid.Column.Third);

        game.makeMove(first, Grid.Row.Second, Grid.Column.Second);
        game.makeMove(second, Grid.Row.Third, Grid.Column.Second);

        return game;
    }
}
