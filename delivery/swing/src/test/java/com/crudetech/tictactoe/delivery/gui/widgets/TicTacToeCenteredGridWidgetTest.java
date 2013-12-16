package com.crudetech.tictactoe.delivery.gui.widgets;


import com.crudetech.functional.UnaryFunction;
import com.crudetech.gui.widgets.CoordinateSystem;
import com.crudetech.gui.widgets.Point;
import com.crudetech.gui.widgets.Rectangle;
import com.crudetech.gui.widgets.Widget;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.crudetech.matcher.RangeIsEqual.equalTo;
import static com.crudetech.query.Query.from;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TicTacToeCenteredGridWidgetTest {
    private Style style;
    private TicTacToeGridModel model;
    private static final Rectangle widgetBoundary = new Rectangle(1, 1, 750, 500);

    @Before
    public void createStyleStub() throws Exception {
        style = EvenlyDistributedCellsStyleStub.builder()
                .withBackgroundImageSize(500, 400)
                .withCellSize(100, 100)
                .build();

    }

    @Before
    public void createModelStub() throws Exception {
        model = new TicTacToeGridModel(LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.Cross
        ));

    }

    @Test
    public void subPaintListIsPaintedInOrder() {
        TicTacToeCenteredGridWidget widget = new TicTacToeCenteredGridWidget(widgetBoundary, style, model);

        List<Class<? extends Widget>> widgetClasses = from(widget.subWidgets()).select(toClass()).toList();

        List<Class<? extends Widget>> expectedList = new ArrayList<>();
        expectedList.add(StatefulTransparencyImageWidget.class);
        expectedList.add(TicTacToeGridCellsWidget.class);
        expectedList.add(TicTacToeGridHighlightedCellWidget.class);

        assertThat(widgetClasses, is(equalTo(expectedList)));
    }

    private UnaryFunction<Widget, Class<? extends Widget>> toClass() {
        return new UnaryFunction<Widget, Class<? extends Widget>>() {
            @Override
            public Class<? extends Widget> execute(Widget widget) {
                return widget.getClass();
            }
        };
    }

    @Test
    public void givenBackgroundImageThatFitsIntoBoundary_widgetIsCentered() throws Exception {
        TicTacToeCenteredGridWidget widget = new TicTacToeCenteredGridWidget(widgetBoundary, style, model);

        assertThat(widget.coordinateSystem(), is(new CoordinateSystem(Point.of(125, 50), CoordinateSystem.NoScale)));
    }
}
