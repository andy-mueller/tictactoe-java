package com.crudetech.tictactoe.delivery.text.jcurses;

import com.crudetech.tictactoe.delivery.text.jcurses.GridWidget;
import com.crudetech.tictactoe.game.Grid;
import jcurses.widgets.TextComponent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(Parameterized.class)
public class GridWidgetCursorMapsOnTextComponentCursorTest {
    private final Grid.Location location;
    private final int expectedTextLocationX;
    private final int expectedTextLocationY;

    public GridWidgetCursorMapsOnTextComponentCursorTest(Grid.Location location, int expectedTextLocationX, int expectedTextLocationY) {
        this.location = location;
        this.expectedTextLocationX = expectedTextLocationX;
        this.expectedTextLocationY = expectedTextLocationY;
    }

//   O |   | X    <0
//  ---+---+---    1
//   X | O | O    <2
//  ---+---+---    3
//   X | O |      <4
//   ^   ^   ^
//  01234567890

    @Parameterized.Parameters
    public static Collection<Object[]> createParameters() {
        return asList(new Object[][]{
                {Grid.Location.of(Grid.Row.First, Grid.Column.First), 1, 0},
                {Grid.Location.of(Grid.Row.First, Grid.Column.Second), 5, 0},
                {Grid.Location.of(Grid.Row.First, Grid.Column.Third), 9, 0},

                {Grid.Location.of(Grid.Row.Second, Grid.Column.First), 1, 2},
                {Grid.Location.of(Grid.Row.Second, Grid.Column.Second), 5, 2},
                {Grid.Location.of(Grid.Row.Second, Grid.Column.Third), 9, 2},

                {Grid.Location.of(Grid.Row.Third, Grid.Column.First), 1, 4},
                {Grid.Location.of(Grid.Row.Third, Grid.Column.Second), 5, 4},
                {Grid.Location.of(Grid.Row.Third, Grid.Column.Third), 9, 4},
        });
    }


    @Test
    public void setLocationSetsCursorOnTextComponent() {
        GridWidget.Cursor cursor = new GridWidget.Cursor();
        cursor.setLocation(location);

        TextComponent textWidget = spy(new TextComponent(10, 10, "XoX") {
            @Override
            public void setCursorLocation(int x, int y) {
            }
        });

        cursor.setOn(textWidget);

        verify(textWidget).setCursorLocation(expectedTextLocationX, expectedTextLocationY);
    }
}
