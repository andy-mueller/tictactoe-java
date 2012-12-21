package com.crudetech.tictactoe.delivery.swing.grid;

import com.crudetech.event.EventSupport;
import com.crudetech.tictactoe.game.Grid;
import org.junit.Before;
import org.junit.Test;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import static com.crudetech.matcher.RangeIsEquivalent.equivalentTo;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class JTicTacToeGridModelChangedTest {
    private EventSupport<TicTacToeGridModel.CellsChangedEventObject> modelCellChanged;
    private PaintTrackingTicTacToeGrid aGrid;

    static class PaintTrackingTicTacToeGrid extends JTicTacToeGrid {
        private boolean areaRepainted = false;
        private List<Rectangle> repaintedRegions = new ArrayList<>();
        public boolean fullRepaint = false;

        PaintTrackingTicTacToeGrid(TicTacToeGridModel model) {
            super(model);
        }

        @Override
        public void repaint(Rectangle repaintedRegion) {
            areaRepainted = true;
            repaintedRegions.add(repaintedRegion);
            super.repaint(repaintedRegion);
        }

        @Override
        public void repaint() {
            fullRepaint = true;
        }
    }

    @Before
    public void before() {
        modelCellChanged = new EventSupport<>();
        TicTacToeGridModel model = new TicTacToeGridModel() {
            @Override
            public EventSupport<CellsChangedEventObject> cellsChanged() {
                return modelCellChanged;
            }
        };
        aGrid = new PaintTrackingTicTacToeGrid(model);
        aGrid.setSize(StyleStub.Width * 2, StyleStub.Height * 2);
        Style style = new StyleStub();
        aGrid.getUI().setStyle(style);
    }

    @Test
    public void modelCellChangedEvenTriggersRepaint() {
        Iterable<Grid.Location> changedCells = asList(
                Grid.Location.of(Grid.Row.First, Grid.Column.Third),
                Grid.Location.of(Grid.Row.First, Grid.Column.Second),
                Grid.Location.of(Grid.Row.Second, Grid.Column.Third)
        );

        modelCellChanged.fireEvent(new TicTacToeGridModel.CellsChangedEventObject(aGrid.getModel(), changedCells));

        assertThat(aGrid.areaRepainted, is(true));
        assertThat(aGrid.repaintedRegions, is(equivalentTo(expectedRepaintedRegions())));
    }

    private static List<Rectangle> expectedRepaintedRegions() {
        return asList(
                new Rectangle(StyleStub.Width / 2, StyleStub.Height / 2 + 2 * StyleStub.GridCellWidth + 2 * StyleStub.GridCellDistance, StyleStub.GridCellWidth + 1, StyleStub.GridCellHeight + 1),
                new Rectangle(StyleStub.Width / 2, StyleStub.Height / 2 + StyleStub.GridCellWidth + StyleStub.GridCellDistance, StyleStub.GridCellWidth + 1, StyleStub.GridCellHeight + 1),
                new Rectangle(StyleStub.Width / 2 + StyleStub.GridCellWidth + StyleStub.GridCellDistance, StyleStub.Height / 2 + 2 * StyleStub.GridCellWidth + 2 * StyleStub.GridCellDistance, StyleStub.GridCellWidth + 1, StyleStub.GridCellHeight + 1)
        );
    }

    @Test
    public void changingModelTriggersFullRepaint() {
        aGrid.setModel(new TicTacToeGridModel());

        assertThat(aGrid.fullRepaint, is(true));
    }
}
