package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.gui.widgets.Rectangle;
import com.crudetech.gui.widgets.Widget;
import com.crudetech.tictactoe.game.Grid;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class TicTacToeGridHighlightedCellWidgetTest {

    private HighlightStateStub state;
    private Style style;

    class HighlightStateStub implements TicTacToeGridHighlightedCellWidget.HighlightState {
        private boolean isHighlighted;

        public void highlight() {
            isHighlighted = true;
        }

        public void unHighlight() {
            isHighlighted = false;
        }

        @Override
        public boolean isHighlighted() {
            return isHighlighted;
        }

        @Override
        public Grid.Location getLocation() {
            return Grid.Location.of(Grid.Row.First, Grid.Column.Second);
        }
    }

    @Before
    public void setUp() throws Exception {
        state = new HighlightStateStub();
        style = StyleStub.builder()
                .withBackgroundImageSize(500, 400)
                .withCellSize(100, 100)
                .build();
    }

    @Test
    public void givenHighlightStateIsOn_WidgetDisplaysRectangleAtCorrectLocation() throws Exception {
        state.highlight();
        TicTacToeGridHighlightedCellWidget widget = new TicTacToeGridHighlightedCellWidget(state, style);

        Widget expectedRectangleWidget = new RectangleWidget(new Rectangle(200, 25, 100, 100), style.getHighlightColor());
        assertThat(widget.getDecorated(), is(expectedRectangleWidget));
    }

    @Test
    public void givenHighlightStateIsOff_WidgetDisplaysNoRectangle() throws Exception {
        state.unHighlight();

        TicTacToeGridHighlightedCellWidget widget = new TicTacToeGridHighlightedCellWidget(state, style);

        assertThat(widget.getDecorated(), is(instanceOf(EmptyWidget.class)));
    }
}
