package com.crudetech.tictactoe.client.swing.grid;

import com.crudetech.event.EventSupport;
import com.crudetech.tictactoe.game.Grid;
import org.junit.Before;
import org.junit.Test;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.crudetech.matcher.RangeIsEquivalent.equivalentTo;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class JTicTacToeGridModelChangedTest {

    private EventSupport<TicTacToeGridModel.CellsChangedEventObject> modelCellChanged;
    private List<Rectangle> repaintedRegions;
    private AtomicBoolean painted;
    private JTicTacToeGrid aGrid;


    @Before
    public void before() {
        modelCellChanged = new EventSupport<>();
        TicTacToeGridModel model = new TicTacToeGridModel() {
            @Override
            public EventSupport<CellsChangedEventObject> cellsChanged() {
                return modelCellChanged;
            }
        };
        repaintedRegions = new ArrayList<>();
        painted = new AtomicBoolean(false);
        aGrid = new JTicTacToeGrid(model) {
            @Override
            public void repaint(Rectangle repaintedRegion) {
                painted.getAndSet(true);
                repaintedRegions.add(repaintedRegion);
                super.repaint(repaintedRegion);
            }
        };
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

        assertThat(painted.get(), is(true));
        assertThat(repaintedRegions, is(equivalentTo(expectedRepaintedRegions())));
    }

    private static List<Rectangle> expectedRepaintedRegions() {
        return asList(
                new Rectangle(StyleStub.Width / 2, StyleStub.Height / 2 + 2 * StyleStub.GridCellWidth + 2 * StyleStub.GridCellDistance, StyleStub.GridCellWidth+1, StyleStub.GridCellHeight+1),
                new Rectangle(StyleStub.Width / 2, StyleStub.Height / 2 + StyleStub.GridCellWidth + StyleStub.GridCellDistance, StyleStub.GridCellWidth+1, StyleStub.GridCellHeight+1),
                new Rectangle(StyleStub.Width / 2+ StyleStub.GridCellWidth + StyleStub.GridCellDistance, StyleStub.Height / 2 + 2 * StyleStub.GridCellWidth + 2 * StyleStub.GridCellDistance, StyleStub.GridCellWidth+1, StyleStub.GridCellHeight+1)
        );
    }
}
