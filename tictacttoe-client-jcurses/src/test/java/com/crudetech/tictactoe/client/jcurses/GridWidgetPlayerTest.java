package com.crudetech.tictactoe.client.jcurses;

import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class GridWidgetPlayerTest {
    private static final Grid.Triple DiagonalTriple = Grid.Triple.of(
            Grid.Mark.Nought,
            Grid.Location.of(Grid.Row.First, Grid.Column.First),
            Grid.Location.of(Grid.Row.Second, Grid.Column.Second),
            Grid.Location.of(Grid.Row.Third, Grid.Column.Third));

    @Test
    public void yourTurnSetsPassedGridOnWidget() {
        GridWidgetView widget = mock(GridWidgetView.class);

        Grid currentGrid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Cross
        });

        UserFeedbackChannel userFeedback = mock(UserFeedbackChannel.class);
        GridWidgetPlayer player = new GridWidgetPlayer(widget, userFeedback);
        player.yourTurn(currentGrid);

        verify(widget).setGrid(currentGrid);
    }

    @Test
    public void yourTurnMovesCursorToNextOpenSpot() {
        GridWidgetView widget = mock(GridWidgetView.class);

        Grid currentGrid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Cross
        });

        UserFeedbackChannel userFeedback = mock(UserFeedbackChannel.class);
        GridWidgetPlayer player = new GridWidgetPlayer(widget, userFeedback);
        player.yourTurn(currentGrid);

        verify(widget).moveCursorToFirstMarkedCell(currentGrid);
    }

    @Test
    public void youWinGivesUserFeedback(){
        GridWidgetView widget = mock(GridWidgetView.class);
        Grid winningGrid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.Nought, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought
        });

        UserFeedbackChannel userFeedback = mock(UserFeedbackChannel.class);
        GridWidgetPlayer player = new GridWidgetPlayer(widget, userFeedback);

        player.youWin(winningGrid, DiagonalTriple);

        verify(userFeedback).showMessage("You win!");
    }
    @Test
    public void youWinHighLightsWinningTriple(){
        GridWidgetView widget = mock(GridWidgetView.class);
        Grid winningGrid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.Nought, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought
        });

        UserFeedbackChannel userFeedback = mock(UserFeedbackChannel.class);
        GridWidgetPlayer player = new GridWidgetPlayer(widget, userFeedback);

        player.youWin(winningGrid, DiagonalTriple);

        verify(widget).highlight(DiagonalTriple);
    }
}
