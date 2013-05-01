package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.gui.widgets.Rectangle;
import com.crudetech.gui.widgets.Widget;
import com.crudetech.tictactoe.game.Grid;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TicTacToeGridHighlightedCellWidgetTest {
    class HighlightStateStub implements TicTacToeGridHighlightedCellWidget.HighlightState {
        public void highlight() {

        }
    }

    @Test
    public void givenHighlightStateIsOn_WidgetDisplaysRectangleAtCorrectLocation() throws Exception {
        HighlightStateStub state = new HighlightStateStub();
        state.highlight();
        Style style = StyleStub.builder()
                .withBackgroundImageSize(500, 400)
                .withCellSize(100, 100)
                .build();
        Grid.Location location = Grid.Location.of(Grid.Row.First, Grid.Column.Second);
        TicTacToeGridHighlightedCellWidget widget = new TicTacToeGridHighlightedCellWidget(state, style, location);

        Widget expectedRectangleWidget = new RectangleWidget(new Rectangle(200, 25, 100, 100), style.getHighlightColor());
        assertThat(widget.getDecorated(), is(expectedRectangleWidget));
    }
    //highlight single cell
    //no highlighted cell is empty widget
}
