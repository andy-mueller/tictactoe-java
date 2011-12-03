package com.crudetech.tictactoe.game;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AlphaBetaPruningPlayerTest {
    @Test
    public void playerMakesBestNextMove() {
        TicTacToeGame game = mock(TicTacToeGame.class);
        AlphaBetaPruningPlayer player = new AlphaBetaPruningPlayer(Grid.Mark.Cross);
        player.setGame(game);
        Grid currentGrid = LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.Nought,
                Grid.Mark.Nought, Grid.Mark.Cross, Grid.Mark.Cross,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought
        );

        player.yourTurn(currentGrid);

        verify(game).addMark(player, Grid.Location.of(Grid.Row.First, Grid.Column.Second));
    }
}
