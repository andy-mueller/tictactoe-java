package com.crudetech.tictactoe.delivery.swing.grid;


import com.crudetech.gui.widgets.GraphicsStream;
import com.crudetech.gui.widgets.Point;
import com.crudetech.gui.widgets.Rectangle;
import com.crudetech.gui.widgets.Widget;
import com.crudetech.tictactoe.delivery.gui.widgets.Style;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

public class TicTacToeGridWidgetTest {
    private Style style;
    private Grid model;

    @Before
    public void setUp() throws Exception {
        style = new StyleStub();

        model = LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.Cross
        );

    }


    private TicTacToeGridWidget createWidgetWithBounds(Rectangle bounds) {
        return new TicTacToeGridWidget(bounds, style, Grid.ThreeInARow.Empty, model.getCells(), null, true, AwtColor.ORANGE);
    }

    @Test
    public void backgroundIsPositionedAtOriginIfComponentIsSmaller() {
        TicTacToeGridWidget widget = createWidgetWithBounds(new Rectangle(1, 1, 10, 10));

        Widget backgroundImage = widget.buildPaintList().get(1);
        assertThat(backgroundImage.getLocation(), is(new Point(0, 0))) ;
    }

    @Test
    public void backGroundIsInvalidated() {
        TicTacToeGridWidget widget = createWidgetWithBounds(new Rectangle(0, 0, 500, 600));
        GraphicsStream stream = mock(GraphicsStream.class);

        List<Widget> widgets = widget.buildPaintList();
        Widget background = widgets.get(0);

        Widget expectedBackground = getExpectedBackground(500, 600);
        assertThat(background, is(expectedBackground));
    }

    private Widget getExpectedBackground(int width, int height) {
        return new FilledRectangleWidget(new Rectangle(0, 0, width, height + 500), style.getBackgroundColor());
    }
}
