package com.crudetech.tictactoe.ui;

import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class UiPlayerTest {
    private static final Grid.ThreeInARow DiagonalTriple = Grid.ThreeInARow.of(
            Grid.Mark.Nought,
            Grid.Location.of(Grid.Row.First, Grid.Column.First),
            Grid.Location.of(Grid.Row.Second, Grid.Column.Second),
            Grid.Location.of(Grid.Row.Third, Grid.Column.Third));
    private UiFeedbackChannel uiFeedback;
    private UiPlayer player;
    private UiView widget;

    @Before
    public void setUp() throws Exception {
        widget = mock(UiView.class);
        uiFeedback = mock(UiFeedbackChannel.class);
        player = new UiPlayer(widget, uiFeedback);
    }

    @Test
    public void yourTurnSetsPassedGridOnWidget() {
        Grid currentGrid = LinearRandomAccessGrid.of(
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Cross);

        player.yourTurn(currentGrid);

        verify(widget).setModel(currentGrid);
    }


    @Test
    public void youWinGivesUserFeedback() {
        Grid winningGrid = LinearRandomAccessGrid.of(
                Grid.Mark.Nought, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought);

        player.youWin(winningGrid, DiagonalTriple);

        verify(uiFeedback).showMessage("You win!");
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
    public void youWinUpdatesModel() {
        Grid winningGrid = LinearRandomAccessGrid.of(
                Grid.Mark.Nought, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought);

        player.youWin(winningGrid, DiagonalTriple);

        verify(widget).setModel(winningGrid);
    }

    @Test
    public void youLoosGivesUserFeedback() {
        Grid winningGrid = LinearRandomAccessGrid.of(
                Grid.Mark.Nought, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought);

        player.youLoose(winningGrid, DiagonalTriple);

        verify(uiFeedback).showMessage("You loose!");
    }

    @Test
    public void youLooseHighlightsWinningTriple() {
        Grid loosingGrid = LinearRandomAccessGrid.of(
                Grid.Mark.Nought, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought);

        player.youLoose(loosingGrid, DiagonalTriple);

        verify(widget).highlight(DiagonalTriple);
    }


    @Test
    public void youLooseUpdatesModel() {
        Grid loosingGrid = LinearRandomAccessGrid.of(
                Grid.Mark.Nought, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought);

        player.youLoose(loosingGrid, DiagonalTriple);

        verify(widget).setModel(loosingGrid);
    }

    @Test
    public void tieGivesUserFeedback() {
        Grid winningGrid = LinearRandomAccessGrid.of(
                Grid.Mark.Nought, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought);


        player.tie(winningGrid);

        verify(uiFeedback).showMessage("Tie!");
    }

    @Test
    public void tieUpdatesModel() {
        Grid tieGrid = LinearRandomAccessGrid.of(
                Grid.Mark.Nought, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought);


        player.tie(tieGrid);

        verify(widget).setModel(tieGrid);
    }
}
