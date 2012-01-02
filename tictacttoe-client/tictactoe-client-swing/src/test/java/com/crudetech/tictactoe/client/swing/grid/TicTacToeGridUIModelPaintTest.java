package com.crudetech.tictactoe.client.swing.grid;

import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static com.crudetech.matcher.RangeIsEquivalent.equivalentTo;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TicTacToeGridUIModelPaintTest {

    @Test
    public void gridMarksArePaintedFromModel() {
        TicTacToeGridModel model = new TicTacToeGridModel(
                LinearRandomAccessGrid.of(
                        Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.None,
                        Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.None,
                        Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.Cross
                ));

        JTicTacToeGrid grid = new JTicTacToeGrid(model);

        TicTacToeGridUI ui = grid.getUI();


        Style style = new StyleStub();
        ui.setStyle(style);

        final BufferedImage cross = style.getCrossImage();
        final BufferedImage nought = style.getNoughtImage();
        final Color backGroundColor = style.getBackgroundColor();
        Rectangle[][] locations = style.getGridMarkLocations();


        List<Widget> widgets = ui.buildGridMarkWidgetList();


        List<Widget> expected = asList(
                new ImageWidget(locations[0][0].getLocation(), cross),
                new ImageWidget(locations[0][1].getLocation(), nought),
                new FilledRectangleWidget(locations[0][2], backGroundColor),

                new ImageWidget(locations[1][0].getLocation(), cross),
                new FilledRectangleWidget(locations[1][1], backGroundColor),
                new FilledRectangleWidget(locations[1][2], backGroundColor),

                new ImageWidget(locations[2][0].getLocation(), nought),
                new ImageWidget(locations[2][1].getLocation(), nought),
                new ImageWidget(locations[2][2].getLocation(), cross)
        );

        assertThat(widgets, is(equivalentTo(expected)));
    }
}
