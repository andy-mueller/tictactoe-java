package com.crudetech.tictactoe.client.swing.grid;

import com.crudetech.event.EventListener;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;
import org.junit.Test;

import static com.crudetech.matcher.ThrowsException.doesThrow;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TicTacToeGridModelTest {
    @Test
    public void ctorSetsModelData() {
        Grid grid = LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Cross,
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Cross,
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Cross
        );
        TicTacToeGridModel model = new TicTacToeGridModel(grid);

        assertThat(model.getModelObject(), is(grid));
    }

    @Test
    public void setModelRaisesEvent() {
        TicTacToeGridModel model = new TicTacToeGridModel();
        @SuppressWarnings("unchecked")
        EventListener<Model.ChangedEventObject<Model<Grid>>> changedListener = mock(EventListener.class);
        model.changed().addListener(changedListener);
        Grid grid = LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Cross,
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Cross,
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Cross
        );

        model.setModelObject(grid);

        verify(changedListener).onEvent(new Model.ChangedEventObject<Model<Grid>>(model));
    }

    @Test
    public void setModelSetsModelObject() {
        TicTacToeGridModel model = new TicTacToeGridModel();
        Grid grid = LinearRandomAccessGrid.empty();

        model.setModelObject(grid);

        assertThat(model.getModelObject(), is(grid));
    }

    @Test
    public void setModelThrowsOnNull() {
        final TicTacToeGridModel model = new TicTacToeGridModel();

        Runnable setNullValue = new Runnable() {
            @Override
            public void run() {
                model.setModelObject(null);
            }
        };

        assertThat(setNullValue, doesThrow(IllegalArgumentException.class));
    }

    @Test
    public void highlight() {
        TicTacToeGridModel model = new TicTacToeGridModel();

        model.highlightCell(Grid.Location.of(Grid.Row.Second, Grid.Column.Third));

        assertThat(model.getHighlightedCell(), is(Grid.Location.of(Grid.Row.Second, Grid.Column.Third)));
        assertThat(model.hasHighlightedCell(), is(true));
    }

    @Test
    public void highlightDoesNotAcceptNull() {
        final TicTacToeGridModel model = new TicTacToeGridModel();

        Runnable highlightWithNull = new Runnable() {
            @Override
            public void run() {
                model.highlightCell(null);
            }
        };

        assertThat(highlightWithNull, doesThrow(IllegalArgumentException.class));
    }
    @Test
    public void unHighlight() {
        TicTacToeGridModel model = new TicTacToeGridModel();

        model.highlightCell(Grid.Location.of(Grid.Row.Second, Grid.Column.Third));
        model.unHighlight();

        assertThat(model.hasHighlightedCell(), is(false));
    }

    private static Grid.Triple diagonal = Grid.Triple.of(Grid.Mark.Nought,
            Grid.Location.of(Grid.Row.First, Grid.Column.First),
            Grid.Location.of(Grid.Row.Second, Grid.Column.Second),
            Grid.Location.of(Grid.Row.Third, Grid.Column.Third));

    @Test
    public void highlightWinningTriple(){
        TicTacToeGridModel model = new TicTacToeGridModel();

        model.highlightTriple(diagonal);

        assertThat(model.getHighlightedTriple(), is(diagonal));
    }

    @Test
    public void hasHighlightedTripleShowsTrueWhenTripleIsSet(){
        TicTacToeGridModel model = new TicTacToeGridModel();

        assertThat(model.hasHighlightedTriple(), is(false));

        model.highlightTriple(diagonal);

        assertThat(model.hasHighlightedTriple(), is(true));
    }
    @Test
    public void highlIghtTripleThrowsOnNull(){
        final TicTacToeGridModel model = new TicTacToeGridModel();

        Runnable highlightTripleWithNull = new Runnable() {
            @Override
            public void run() {
                model.highlightTriple(null);
            }
        };

        assertThat(highlightTripleWithNull, doesThrow(IllegalArgumentException.class));
    }
}
