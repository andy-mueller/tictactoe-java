package com.crudetech.tictactoe.client.jcurses;


import com.crudetech.tictactoe.game.Grid;
import jcurses.system.InputChar;
import org.junit.Test;

import static jcurses.system.InputChar.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class GridWidgetCursorMoveTest {
    @Test
    public void arrowDownMovesCursorOneDown() {
        GridWidget.Cursor cursor = spy(new GridWidget.Cursor());
        GridWidget w = new StandAloneGridWidget(cursor);

        boolean handled = w.handleInput(new InputChar(KEY_DOWN));
        assertThat(handled, is(true));

        verify(cursor).moveDown();
    }

    @Test
    public void arrowUpMovesCursorOneUp() {
        GridWidget.Cursor cursor = spy(new GridWidget.Cursor());
        GridWidget w = new StandAloneGridWidget(cursor);

        boolean handled = w.handleInput(new InputChar(KEY_UP));
        assertThat(handled, is(true));

        verify(cursor).moveUp();
    }

    @Test
    public void arrowLeftMovesCursorOneLeft() {
        GridWidget.Cursor cursor = spy(new GridWidget.Cursor());
        GridWidget w = new StandAloneGridWidget(cursor);

        boolean handled = w.handleInput(new InputChar(KEY_LEFT));
        assertThat(handled, is(true));

        verify(cursor).moveLeft();
    }

    @Test
    public void arrowRightMovesCursorOneRight() {
        GridWidget.Cursor cursor = spy(new GridWidget.Cursor());
        GridWidget w = new StandAloneGridWidget(cursor);

        boolean handled = w.handleInput(new InputChar(KEY_RIGHT));
        assertThat(handled, is(true));

        verify(cursor).moveRight();
    }
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
