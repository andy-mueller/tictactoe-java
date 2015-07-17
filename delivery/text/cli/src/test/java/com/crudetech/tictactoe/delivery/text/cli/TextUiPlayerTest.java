package com.crudetech.tictactoe.delivery.text.cli;

import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;
import com.crudetech.tictactoe.game.Player;
import com.crudetech.tictactoe.ui.HumanVsComputerPlayerInteractor;
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

        TextUiPlayer textUiPlayer = new TextUiPlayer(mock(TextGridWidgetUiView.class), mock(TextUiFeedbackChannel.class), new PrintWriter(actualOutput), input);
        Player computerPlayer = mock(Player.class);

        HumanVsComputerPlayerInteractor interactor =
                HumanVsComputerPlayerInteractor.builder()
                        .setComputerPlayer(computerPlayer)
                        .setHumanPlayer(textUiPlayer)
                        .build();

        interactor.startWithComputerPlayer(Grid.Mark.Cross);
        interactor.makeComputerPlayerMove(Grid.Location.of(Grid.Row.Second, Grid.Column.Third));

        LinearRandomAccessGrid expectedGridAfterFirstMove = LinearRandomAccessGrid.of(
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Cross,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None);

        verify(computerPlayer, times(1)).moveWasMade(expectedGridAfterFirstMove);

        LinearRandomAccessGrid expectedGrid = LinearRandomAccessGrid.of(
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Cross,
                Grid.Mark.Nought, Grid.Mark.None, Grid.Mark.None);

        verify(computerPlayer, times(1)).yourTurn(expectedGrid);
    }
}
