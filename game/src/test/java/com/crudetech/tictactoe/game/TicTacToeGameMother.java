package com.crudetech.tictactoe.game;

/**
 * Creates a test games
 */
public class TicTacToeGameMother {
    public void setupAlmostFinishedGame(Player first, Player second, TicTacToeGame game) {
        game.makeMove(first, Grid.Row.Second, Grid.Column.First);
        game.makeMove(second, Grid.Row.First, Grid.Column.Third);

        game.makeMove(first, Grid.Row.Third, Grid.Column.First);
        game.makeMove(second, Grid.Row.Second, Grid.Column.Third);

        game.makeMove(first, Grid.Row.Second, Grid.Column.Second);
        game.makeMove(second, Grid.Row.Third, Grid.Column.Second);
    }

    /**
     * Creates a game that is still open for both players to win or to produce a tie:
     * <pre>
     *    |   | O
     * ---+---+---
     *  X | X | O
     * ---+---+---
     *  X | O |
     * </pre>
     * <p/>
     * It is the first players turn now, using the 'X' mark
     */
    public TicTacToeGame createOpenAndAlmostFinishedGame(Player first, Player second) {
        TicTacToeGame game = new TicTacToeGame(first, second);
        game.startWithPlayer(first, Grid.Mark.Cross);

        setupAlmostFinishedGame(first, second, game);

        return game;
    }

    public Grid almostFinishedGrid() {
        return LinearRandomAccessGrid.of(
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought,
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Nought,
                Grid.Mark.Cross,Grid.Mark.Nought,Grid.Mark.None);
    }

    /**
     * Creates a game is finished nd won by the O player:
     * <pre>
     *    |   | O
     * ---+---+---
     *  X | X | O
     * ---+---+---
     *  X | O | O
     * </pre>
     */
    public Grid finishedGridWitNoughtsWinning() {
        return LinearRandomAccessGrid.of(
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought,
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Nought,
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.Nought);
    }
}
