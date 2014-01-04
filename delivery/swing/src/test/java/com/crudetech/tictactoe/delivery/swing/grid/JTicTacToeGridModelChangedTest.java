package com.crudetech.tictactoe.delivery.swing.grid;

import com.crudetech.event.EventSupport;
import com.crudetech.tictactoe.delivery.gui.widgets.EvenlyDistributedCellsStyleStub;
import com.crudetech.tictactoe.delivery.gui.widgets.TicTacToeGridModel;
import com.crudetech.tictactoe.game.Grid;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.crudetech.matcher.RangeIsEquivalent.equivalentTo;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class JTicTacToeGridModelChangedTest {
    private EventSupport<TicTacToeGridModel.CellsChangedEventObject> modelCellChanged;
    private PaintTrackingTicTacToeGridSpy aGrid;

    static class PaintTrackingTicTacToeGridSpy extends JTicTacToeGrid {
        private boolean areaRepainted = false;
        private List<Rectangle> repaintedRegions = new ArrayList<>();
        public boolean fullRepaint = false;

        PaintTrackingTicTacToeGridSpy(TicTacToeGridModel model, TicTacToeGridUI ui) {
            super(model, ui);
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

    static class TicTacToeGridUIStub extends TicTacToeGridUI {
        @Override
        Iterable<Rectangle> getRectanglesForCells(Iterable<Grid.Location> cells) {
            return asList(magicRectangle());
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

        EvenlyDistributedCellsStyleStub style = new EvenlyDistributedCellsStyleStub.Builder().build();
        TicTacToeGridUI ui = new TicTacToeGridUIStub();
        ui.setStyle(style);

        aGrid = new PaintTrackingTicTacToeGridSpy(model, ui);
        aGrid.setSize(style.backgroundImageWidth * 2, style.backgroundImageHeight * 2);
    }

    @Test
    public void modelCellChangedEvenTriggersRepaint() {
        // -XX
        // --X
        // ---
        Iterable<Grid.Location> changedCells = asList(
                Grid.Location.of(Grid.Row.First, Grid.Column.Third),
                Grid.Location.of(Grid.Row.First, Grid.Column.Second),
                Grid.Location.of(Grid.Row.Second, Grid.Column.Third)
        );

        modelCellChanged.fireEvent(new TicTacToeGridModel.CellsChangedEventObject(aGrid.getModel(), changedCells));

        assertThat(aGrid.areaRepainted, is(true));
        assertThat(aGrid.repaintedRegions, is(equivalentTo(expectedRepaintedRegions())));
    }

    private List<Rectangle> expectedRepaintedRegions() {
        return asList(magicRectangle());
    }

    private static Rectangle magicRectangle() {
        return new Rectangle(-1, -1, -1, -1);
    }


    @Test
    public void changingModelTriggersFullRepaint() {
        aGrid.setModel(new TicTacToeGridModel());

        assertThat(aGrid.fullRepaint, is(true));
    }
}
