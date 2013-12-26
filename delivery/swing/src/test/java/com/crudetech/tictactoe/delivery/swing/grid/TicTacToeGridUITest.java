package com.crudetech.tictactoe.delivery.swing.grid;

import com.crudetech.tictactoe.delivery.gui.widgets.EvenlyDistributedCellsStyleStub;
import com.crudetech.tictactoe.delivery.gui.widgets.Style;
import com.crudetech.tictactoe.delivery.gui.widgets.TicTacToeGridModel;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TicTacToeGridUITest {
    private JTicTacToeGrid grid;
    private TicTacToeGridUI ui;
    private Style style;

    @Before
    public void setUp() throws Exception {
        TicTacToeGridModel model = new TicTacToeGridModel(
                LinearRandomAccessGrid.of(
                        Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.None,
                        Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.None,
                        Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.Cross
                ));

        grid = new JTicTacToeGrid(model);
        grid.setSize(1000, 2000);

        ui = grid.getUI();

        style = new EvenlyDistributedCellsStyleStub.Builder().build();
        ui.setStyle(style);
        ui.buildGraphic();
    }

    @Test
    public void defaultStyleIsBrush() {
        grid = new JTicTacToeGrid();
        assertThat(Styles.Brush, is(grid.getUI().getStyle()));
    }

    @Test
    public void preferredSizeIsStyleSize() throws Exception {
        Dimension expected = WidgetAwtConverter.dimension(style.getMinimumSize());

        assertThat(grid.getUI().getPreferredSize(grid), is(expected));
    }
}
