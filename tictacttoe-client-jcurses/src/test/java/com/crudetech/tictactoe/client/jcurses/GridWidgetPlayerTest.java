package com.crudetech.tictactoe.client.jcurses;

import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class GridWidgetPlayerTest {
    private static final Grid.Triple DiagonalTriple = Grid.Triple.of(
            Grid.Mark.Nought,
            Grid.Location.of(Grid.Row.First, Grid.Column.First),
            Grid.Location.of(Grid.Row.Second, Grid.Column.Second),
            Grid.Location.of(Grid.Row.Third, Grid.Column.Third));
    private UserFeedbackChannel userFeedback;
    private GridWidgetPlayer player;
    private GridWidgetView widget;

    @Before
    public void setUp() throws Exception {
        widget = mock(GridWidgetView.class);
        userFeedback = mock(UserFeedbackChannel.class);
        player = new GridWidgetPlayer(widget, userFeedback);
    }

    @Test
    public void yourTurnSetsPassedGridOnWidget() {
        Grid currentGrid = LinearRandomAccessGrid.of(
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Cross);

        player.yourTurn(currentGrid);

        verify(widget).setGrid(currentGrid);
    }

    @Test
    public void yourTurnMovesCursorToNextOpenSpot() {
        Grid currentGrid = LinearRandomAccessGrid.of(
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Cross);

        player.yourTurn(currentGrid);

        verify(widget).moveCursorToFirstMarkedCell(currentGrid);
    }

    @Test
    public void youWinGivesUserFeedback() {
        Grid winningGrid = LinearRandomAccessGrid.of(
                Grid.Mark.Nought, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought);

        player.youWin(winningGrid, DiagonalTriple);

        verify(userFeedback).showMessage("You win!");
    }

    @Test
    public void youWinHighlightsWinningTriple() {
        Grid winningGrid = LinearRandomAccessGrid.of(
                Grid.Mark.Nought, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought);

        player.youWin(winningGrid, DiagonalTriple);

        verify(widget).highlight(DiagonalTriple);
    }

    @Test
    public void youLoosGivesUserFeedback() {
        Grid winningGrid = LinearRandomAccessGrid.of(
                Grid.Mark.Nought, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought);

        player.youLoose(winningGrid, DiagonalTriple);

        verify(userFeedback).showMessage("You loose!");
    }

    @Test
    public void youLooseHighlightsWinningTriple() {
        Grid winningGrid = LinearRandomAccessGrid.of(
                Grid.Mark.Nought, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought);

        player.youLoose(winningGrid, DiagonalTriple);

        verify(widget).highlight(DiagonalTriple);
    }

    @Test
    public void tieGivesUserFeedback() {
        Grid winningGrid = LinearRandomAccessGrid.of(
                Grid.Mark.Nought, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought);


        player.tie(winningGrid);

        verify(userFeedback).showMessage("Tie!");
    }
}
