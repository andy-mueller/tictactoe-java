package com.crudetech.tictactoe.client.jcurses;

import com.crudetech.tictactoe.game.Grid;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class GridWidgetTest {
//   O |   | X    <0
//  ---+---+---    1
//   X | O | O    <2
//  ---+---+---    3
//   X | O |      <4
//   ^   ^   ^
//  01234567890

    @Test
    public void ctorSetsCursorOnFirstCell() {
        GridWidget.Cursor cursor = spy(new GridWidget.Cursor());
        GridWidget w = new GridWidget(cursor);

        Grid.Location firstCell = Grid.Location.of(Grid.Row.First, Grid.Column.First);
        verify(cursor).setLocation(firstCell);
    }





    // handle inpu makes cross
    //handleInput set nought
    ////handle fails on there chars
    //set mark failswhenallready marked


    @Test
    public void cursorMoveRightSetsWidgetCursorPosition() {
        GridWidget.Cursor cursor = new GridWidget.Cursor();
        cursor.setLocation(Grid.Location.of(Grid.Row.First, Grid.Column.Second));

        cursor.moveRight();

        assertThat(cursor.getLocation(), is(Grid.Location.of(Grid.Row.First, Grid.Column.Third)));
    }
    // move left
    //flip over right
    //flip over left

    // move up
    // move down
    //flip over bottom
    //flip over top
}
