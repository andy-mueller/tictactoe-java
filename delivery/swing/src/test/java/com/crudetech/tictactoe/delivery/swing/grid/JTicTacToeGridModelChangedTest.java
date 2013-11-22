package com.crudetech.tictactoe.delivery.swing.grid;

import com.crudetech.event.EventSupport;
import com.crudetech.tictactoe.delivery.gui.widgets.StyleStub;
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
        StyleStub style = new StyleStub.Builder().build();
        aGrid.setSize(style.getBackgroundImage().getWidth() * 2, style.getBackgroundImage().getHeight() * 2);

        TicTacToeGridUI ui = new TicTacToeGridUI() {
            @Override
            Iterable<Rectangle> getRectanglesForCells(Iterable<Grid.Location> cells) {
                return asList(magicRectangle());
            }
        };
        aGrid.setUI(ui);
        aGrid.getUI().setStyle(style);

        aGrid.getUI().buildGraphic();
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

    private Rectangle magicRectangle() {
        return new Rectangle(-1, -1, -1, -1);
    }


    @Test
    public void changingModelTriggersFullRepaint() {
        aGrid.setModel(new TicTacToeGridModel());

        assertThat(aGrid.fullRepaint, is(true));
    }
}
