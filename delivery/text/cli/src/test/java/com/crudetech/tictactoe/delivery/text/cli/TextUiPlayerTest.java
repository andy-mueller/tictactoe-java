package com.crudetech.tictactoe.delivery.text.cli;

import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;
import com.crudetech.tictactoe.game.Player;
import com.crudetech.tictactoe.game.TicTacToeGame;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;

public class TextUiPlayerTest {
    @Test
    public void playerTransformsUserInputIntoMove() throws Exception {
        StringWriter actualOutput = new StringWriter();
        InputStream stopAfterMoveInput = new ByteArrayInputStream("quit\n".getBytes());
        TextGridLocationInput input = new TextGridLocationInput(stopAfterMoveInput) {
            @Override
            public Grid.Location nextLocation() {
                return Grid.Location.of(Grid.Row.Third, Grid.Column.First);
            }
        };

        Player textUiPlayer = spy(new TextUiPlayer(mock(TextGridWidgetUiView.class), mock(TextUiFeedbackChannel.class), new PrintWriter(actualOutput), input));
        Player player1 = mock(Player.class);
        TicTacToeGame game = new TicTacToeGame(player1, textUiPlayer);

        game.startWithPlayer(player1, Grid.Mark.Cross);
        game.addMark(player1, Grid.Location.of(Grid.Row.Second, Grid.Column.Third));

        LinearRandomAccessGrid expectedGrid = LinearRandomAccessGrid.of(
                Grid.Mark.None,Grid.Mark.None,Grid.Mark.None,
                Grid.Mark.None,Grid.Mark.None,Grid.Mark.Cross,
                Grid.Mark.Nought,Grid.Mark.None,Grid.Mark.None);

       verify(player1, times(2)).yourTurn(expectedGrid);
    }
}
