package com.crudetech.tictactoe.client.swing.grid;

import com.crudetech.event.EventListener;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;
import org.junit.Test;

import static com.crudetech.collections.Iterables.emptyListOf;
import static com.crudetech.matcher.ThrowsException.doesThrow;
import static java.util.Arrays.asList;
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

        assertThat(model.getGrid(), is(grid));
    }

    @Test
    public void setGridRaisesEvent() {
        TicTacToeGridModel model = new TicTacToeGridModel();
        @SuppressWarnings("unchecked")
        EventListener<TicTacToeGridModel.ChangedEventObject> changedListener = createListenerStub();
        model.changed().addListener(changedListener);
        Grid grid = LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Cross,
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Cross,
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Cross
        );

        model.setGrid(grid);

        Iterable<Grid.Location> changedCells = asList(
                Grid.Location.of(Grid.Row.First, Grid.Column.First), Grid.Location.of(Grid.Row.First, Grid.Column.Second), Grid.Location.of(Grid.Row.First, Grid.Column.Third),
                Grid.Location.of(Grid.Row.Second, Grid.Column.First), Grid.Location.of(Grid.Row.Second, Grid.Column.Second), Grid.Location.of(Grid.Row.Second, Grid.Column.Third),
                Grid.Location.of(Grid.Row.Third, Grid.Column.First), Grid.Location.of(Grid.Row.Third, Grid.Column.Second), Grid.Location.of(Grid.Row.Third, Grid.Column.Third)
        );
        verify(changedListener).onEvent(new TicTacToeGridModel.ChangedEventObject(model, changedCells));
    }


    @Test
    public void setGridSetsTheGrid() {
        TicTacToeGridModel model = new TicTacToeGridModel();
        Grid grid = LinearRandomAccessGrid.empty();

        model.setGrid(grid);

        assertThat(model.getGrid(), is(grid));
    }

    @Test
    public void setGridThrowsOnNull() {
        final TicTacToeGridModel model = new TicTacToeGridModel();

        Runnable setNullValue = new Runnable() {
            @Override
            public void run() {
                model.setGrid(null);
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
    public void highlightRaisesChangedEvent() {
        TicTacToeGridModel model = new TicTacToeGridModel();
        EventListener<TicTacToeGridModel.ChangedEventObject> changedListener = createListenerStub();
        model.changed().addListener(changedListener);

        model.highlightCell(Grid.Location.of(Grid.Row.Second, Grid.Column.Third));

        verify(changedListener).onEvent(new TicTacToeGridModel.ChangedEventObject(model, asList(Grid.Location.of(Grid.Row.Second, Grid.Column.Third))));
    }

    @Test
    public void changingHighlightRaisesChangedEvent() {
        TicTacToeGridModel model = new TicTacToeGridModel();
        EventListener<TicTacToeGridModel.ChangedEventObject> changedListener = createListenerStub();
        model.changed().addListener(changedListener);

        model.highlightCell(Grid.Location.of(Grid.Row.Second, Grid.Column.Third));
        model.highlightCell(Grid.Location.of(Grid.Row.Second, Grid.Column.First));

        verify(changedListener).onEvent(new TicTacToeGridModel.ChangedEventObject(model, asList(Grid.Location.of(Grid.Row.Second, Grid.Column.Third), Grid.Location.of(Grid.Row.Second, Grid.Column.First))));
    }

    @Test
    public void unhighlightRaisesChangedEvent() {
        TicTacToeGridModel model = new TicTacToeGridModel();
        EventListener<TicTacToeGridModel.ChangedEventObject> changedListener = createListenerStub();
        model.changed().addListener(changedListener);

        model.unHighlightCell();

        verify(changedListener).onEvent(new TicTacToeGridModel.ChangedEventObject(model, emptyListOf(Grid.Location.class)));
    }

    @SuppressWarnings("unchecked")
    private EventListener<TicTacToeGridModel.ChangedEventObject> createListenerStub() {
        return mock(EventListener.class);
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
        model.unHighlightCell();

        assertThat(model.hasHighlightedCell(), is(false));
    }

    private static Grid.Triple diagonal = Grid.Triple.of(Grid.Mark.Nought,
            Grid.Location.of(Grid.Row.First, Grid.Column.First),
            Grid.Location.of(Grid.Row.Second, Grid.Column.Second),
            Grid.Location.of(Grid.Row.Third, Grid.Column.Third));

    @Test
    public void highlightWinningTriple() {
        TicTacToeGridModel model = new TicTacToeGridModel();

        model.highlightTriple(diagonal);

        assertThat(model.getHighlightedTriple(), is(diagonal));
    }

    @Test
    public void hasHighlightedTripleShowsTrueWhenTripleIsSet() {
        TicTacToeGridModel model = new TicTacToeGridModel();

        assertThat(model.hasHighlightedTriple(), is(false));

        model.highlightTriple(diagonal);

        assertThat(model.hasHighlightedTriple(), is(true));
    }

    @Test
    public void highlightTripleThrowsOnNull() {
        final TicTacToeGridModel model = new TicTacToeGridModel();

        Runnable highlightTripleWithNull = new Runnable() {
            @Override
            public void run() {
                model.highlightTriple(null);
            }
        };

        assertThat(highlightTripleWithNull, doesThrow(IllegalArgumentException.class));
    }

    @Test
    public void highlightTripleRaisesChangedEvent() {
        TicTacToeGridModel model = new TicTacToeGridModel();
        EventListener<TicTacToeGridModel.ChangedEventObject> changedListener = createListenerStub();
        model.changed().addListener(changedListener);

        model.highlightTriple(diagonal);

        verify(changedListener).onEvent(new TicTacToeGridModel.ChangedEventObject(model, diagonal.getLocations()));
    }

    @Test
    public void unhighlightTripleRaisesChangedEvent() {
        TicTacToeGridModel model = new TicTacToeGridModel();
        EventListener<TicTacToeGridModel.ChangedEventObject> changedListener = createListenerStub();
        model.highlightTriple(diagonal);
        model.changed().addListener(changedListener);

        model.unHighlightTriple();

        verify(changedListener).onEvent(new TicTacToeGridModel.ChangedEventObject(model, diagonal.getLocations()));
    }
}
