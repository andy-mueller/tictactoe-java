package com.crudetech.tictactoe.client.swing.grid;


import com.crudetech.collections.Iterables;
import com.crudetech.event.Event;
import com.crudetech.event.EventObject;
import com.crudetech.event.EventSupport;
import com.crudetech.functional.UnaryFunction;
import com.crudetech.lang.Compare;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;

import java.util.List;

import static com.crudetech.collections.Iterables.concat;
import static com.crudetech.collections.Iterables.emptyListOf;
import static com.crudetech.matcher.Verify.verifyThat;
import static com.crudetech.query.Query.from;
import static com.crudetech.tictactoe.game.GridCells.location;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class TicTacToeGridModel {
    private final EventSupport<ChangedEventObject> changedEvent = new EventSupport<>();
    private Grid.Location highlightedCell;
    private Grid.Triple highlightedTriple;
    private Grid grid;

    public TicTacToeGridModel(Grid grid) {
        this.grid = grid;
    }

    public TicTacToeGridModel() {
        this(LinearRandomAccessGrid.empty());
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        verifyThat(grid, is((notNullValue())));
        this.grid = grid;
        onChanged(allCells());
    }

    private Iterable<Grid.Location> allCells() {
        if (this.grid == null) {
            return emptyListOf(Grid.Location.class);
        } else {
            return from(grid.getCells()).select(location());
        }
    }

    private void onChanged(Iterable<Grid.Location> changed) {
        List<Grid.Location> changedLocations = from(changed).where(notNull()).toList();
        changedEvent.fireEvent(new ChangedEventObject(this, changedLocations));
    }

    private UnaryFunction<? super Grid.Location, Boolean> notNull() {
        return new UnaryFunction<Grid.Location, Boolean>() {
            @Override
            public Boolean execute(Grid.Location location) {
                return location != null;
            }
        };
    }

    public Event<ChangedEventObject> changed() {
        return changedEvent;
    }

    public static class ChangedEventObject extends EventObject<TicTacToeGridModel> {
        private final Iterable<Grid.Location> changedCells;

        public ChangedEventObject(TicTacToeGridModel model, Iterable<Grid.Location> changedCells) {
            super(model);
            this.changedCells = changedCells;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            ChangedEventObject that = (ChangedEventObject) o;

            return Compare.equals(changedCells, that.changedCells);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + Iterables.hashCode(changedCells);
            return result;
        }

        @Override
        public String toString() {
            return "ChangedEventObject{" +
                    "source=" + getSource() +
                    ",changedCells=" + changedCells +
                    '}';
        }
    }

    public void highlightCell(Grid.Location highlightedCell) {
        verifyThat(highlightedCell, is(notNullValue()));
        onChanged(asList(replaceHighlightedCell(highlightedCell), highlightedCell));
    }

    private Grid.Location replaceHighlightedCell(Grid.Location newCell) {
        Grid.Location old = highlightedCell;
        this.highlightedCell = newCell;
        return old;
    }

    public Grid.Location getHighlightedCell() {
        return highlightedCell;
    }

    public boolean hasHighlightedCell() {
        return highlightedCell != null;
    }

    public void unHighlightCell() {
        onChanged(asList(replaceHighlightedCell(null)));
    }

    public void highlightTriple(Grid.Triple highlightedTriple) {
        verifyThat(highlightedTriple, is(notNullValue()));

        Iterable<Grid.Location> oldLocations = cellsOf(replaceHighlightedTriple(highlightedTriple));
        onChanged(concat(oldLocations, highlightedTriple.getLocations()));
    }

    private Iterable<Grid.Location> cellsOf(Grid.Triple triple) {
        if (triple == null) {
            return emptyListOf(Grid.Location.class);
        } else {
            return triple.getLocations();
        }
    }

    private Grid.Triple replaceHighlightedTriple(Grid.Triple newTriple) {
        Grid.Triple old = this.highlightedTriple;
        this.highlightedTriple = newTriple;
        return old;
    }

    public Grid.Triple getHighlightedTriple() {
        return highlightedTriple;
    }

    public boolean hasHighlightedTriple() {
        return highlightedTriple != null;
    }

    public void unHighlightTriple() {
        onChanged(cellsOf(replaceHighlightedTriple(null)));
    }
}
