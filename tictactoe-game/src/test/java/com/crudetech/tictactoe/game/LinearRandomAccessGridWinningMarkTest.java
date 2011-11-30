package com.crudetech.tictactoe.game;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LinearRandomAccessGridWinningMarkTest {
    @Test
    public void emptyGridHasNoWinningMark() {
        LinearRandomAccessGrid emptyGrid = new LinearRandomAccessGrid();

        assertThat(emptyGrid.winningTriple(), is(Grid.Triple.Empty));
    }

    @Test
    public void unfinishedGridHasNoWinningMark() {
        LinearRandomAccessGrid grid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.Nought,
                Grid.Mark.Nought, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.Cross,
        });

        assertThat(grid.winningTriple(), is(Grid.Triple.Empty));
    }

    @Test
    public void hasWinOnFirstRow() {
        LinearRandomAccessGrid grid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Cross,
                Grid.Mark.Nought, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.Cross,
        });


        Grid.Triple expected = Grid.Triple.of(
                Grid.Mark.Cross,
                Grid.Location.of(Grid.Row.First, Grid.Column.First),
                Grid.Location.of(Grid.Row.First, Grid.Column.Second),
                Grid.Location.of(Grid.Row.First, Grid.Column.Third)
        );

        LinearRandomAccessGrid.Triple triple = grid.winningTriple();
        assertThat(triple, is(expected));
    }

    @Test
    public void hasWinOnSecondRow() {
        LinearRandomAccessGrid grid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.Nought, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Cross,
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.Cross,
        });

        Grid.Triple expected = Grid.Triple.of(
                Grid.Mark.Cross,
                Grid.Location.of(Grid.Row.Second, Grid.Column.First),
                Grid.Location.of(Grid.Row.Second, Grid.Column.Second),
                Grid.Location.of(Grid.Row.Second, Grid.Column.Third)
        );

        LinearRandomAccessGrid.Triple triple = grid.winningTriple();
        assertThat(triple, is(expected));
    }

    @Test
    public void hasWinOnThirdRow() {
        LinearRandomAccessGrid grid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.Nought, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.Cross,
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Cross,
        });

        Grid.Triple expected = Grid.Triple.of(
                Grid.Mark.Cross,
                Grid.Location.of(Grid.Row.Third, Grid.Column.First),
                Grid.Location.of(Grid.Row.Third, Grid.Column.Second),
                Grid.Location.of(Grid.Row.Third, Grid.Column.Third)
        );

        LinearRandomAccessGrid.Triple triple = grid.winningTriple();
        assertThat(triple, is(expected));
    }

    @Test
    public void hasWinOnFirstColumn() {
        LinearRandomAccessGrid grid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.Nought,
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.None,
        });

        Grid.Triple expected = Grid.Triple.of(
                Grid.Mark.Cross,
                Grid.Location.of(Grid.Row.First, Grid.Column.First),
                Grid.Location.of(Grid.Row.Second, Grid.Column.First),
                Grid.Location.of(Grid.Row.Third, Grid.Column.First)
        );

        LinearRandomAccessGrid.Triple triple = grid.winningTriple();
        assertThat(triple, is(expected));
    }

    @Test
    public void hasWinOnSecondColumn() {
        LinearRandomAccessGrid grid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.None, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.Nought, Grid.Mark.Nought,
                Grid.Mark.None, Grid.Mark.Nought, Grid.Mark.None,
        });

        Grid.Triple expected = Grid.Triple.of(
                Grid.Mark.Nought,
                Grid.Location.of(Grid.Row.First, Grid.Column.Second),
                Grid.Location.of(Grid.Row.Second, Grid.Column.Second),
                Grid.Location.of(Grid.Row.Third, Grid.Column.Second)
        );

        LinearRandomAccessGrid.Triple triple = grid.winningTriple();
        assertThat(triple, is(expected));
    }

    @Test
    public void hasWinOnThirdColumn() {
        LinearRandomAccessGrid grid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.None, Grid.Mark.Nought, Grid.Mark.Nought,
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.Nought,
                Grid.Mark.None, Grid.Mark.Nought, Grid.Mark.Nought,
        });

        Grid.Triple expected = Grid.Triple.of(
                Grid.Mark.Nought,
                Grid.Location.of(Grid.Row.First, Grid.Column.Third),
                Grid.Location.of(Grid.Row.Second, Grid.Column.Third),
                Grid.Location.of(Grid.Row.Third, Grid.Column.Third)
        );

        LinearRandomAccessGrid.Triple triple = grid.winningTriple();
        assertThat(triple, is(expected));
    }

    @Test
    public void hasWinOnLeftDownDiagonal() {
        LinearRandomAccessGrid grid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.Nought,
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.Nought,
                Grid.Mark.None, Grid.Mark.Nought, Grid.Mark.Cross,
        });

        Grid.Triple expected = Grid.Triple.of(
                Grid.Mark.Cross,
                Grid.Location.of(Grid.Row.First, Grid.Column.First),
                Grid.Location.of(Grid.Row.Second, Grid.Column.Second),
                Grid.Location.of(Grid.Row.Third, Grid.Column.Third)
        );

        LinearRandomAccessGrid.Triple triple = grid.winningTriple();
        assertThat(triple, is(expected));
    }

    @Test
    public void hasWinOnLeftUpDiagonal() {
        LinearRandomAccessGrid grid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.Cross,
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.Nought,
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.Cross,
        });

        Grid.Triple expected = Grid.Triple.of(
                Grid.Mark.Cross,
                Grid.Location.of(Grid.Row.Third, Grid.Column.First),
                Grid.Location.of(Grid.Row.Second, Grid.Column.Second),
                Grid.Location.of(Grid.Row.First, Grid.Column.Third)
        );

        LinearRandomAccessGrid.Triple triple = grid.winningTriple();
        assertThat(triple, is(expected));
    }
    @Test
    public void isWinForCrossPlayerIsFirst(){
        LinearRandomAccessGrid grid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.Cross,
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.Nought,
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Cross,
        });

        assertThat(grid.isWinForMark(Grid.Mark.Cross), is(true));
        assertThat(grid.isWinForMark(Grid.Mark.Nought), is(false));
    }
    @Test
    public void isWinForNoughtPlayerIsFirst(){
        LinearRandomAccessGrid grid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.Nought,
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.Nought,
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.Cross,
        });

        assertThat(grid.isWinForMark(Grid.Mark.Nought), is(true));
        assertThat(grid.isWinForMark(Grid.Mark.Cross), is(false));
    }
    @Test
    public void isWinForIsFalseWhenGameIsOpen(){
        LinearRandomAccessGrid grid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.Nought, Grid.Mark.None, Grid.Mark.Nought,
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.Nought,
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.Cross,
        });

        assertThat(grid.isWinForMark(Grid.Mark.Cross), is(false));
        assertThat(grid.isWinForMark(Grid.Mark.Nought), is(false));
    }
}
