package com.crudetech.tictactoe.client.jcurses;

import com.crudetech.tictactoe.game.Grid;
import org.junit.Test;

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

        Grid.Location firstCell = Grid.Location.of(Grid.Row.Second, Grid.Column.Second);
        verify(cursor).setLocation(firstCell);
    }





    // handle inpu makes cross
    //handleInput set nought
    ////handle fails on there chars
    //set mark failswhenallready marked



}
