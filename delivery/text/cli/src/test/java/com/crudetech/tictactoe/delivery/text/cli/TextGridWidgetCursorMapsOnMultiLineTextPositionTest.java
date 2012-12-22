package com.crudetech.tictactoe.delivery.text.cli;

import com.crudetech.tictactoe.game.Grid;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class TextGridWidgetCursorMapsOnMultiLineTextPositionTest {
    private final Grid.Location location;
    private final int expectedTextLocationX;
    private final int expectedTextLocationY;

    public TextGridWidgetCursorMapsOnMultiLineTextPositionTest(Grid.Location location, int expectedTextLocationX, int expectedTextLocationY) {
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
    public void givenLocationCorrectXandYCoordinatesAreComputed() throws Exception {
        TextGridWidget.Cursor cursor = new TextGridWidget.Cursor(location);
        assertThat(cursor.getTextPositionX(), is(expectedTextLocationX));
        assertThat(cursor.getTextPositionY(), is(expectedTextLocationY));
    }
}
