package com.crudetech.tictactoe.delivery.text.jcurses;


import com.crudetech.tictactoe.delivery.text.cli.TextGridWidget;
import com.crudetech.tictactoe.game.Grid;
import jcurses.system.InputChar;
import org.junit.Test;

import static jcurses.system.InputChar.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class GridWidgetCursorMoveTest {
    @Test
    public void arrowDownMovesCursorOneDown() {
        TextGridWidget.Cursor cursor = mock(TextGridWidget.Cursor.class);
        GridWidget w = new StandAloneGridWidget(cursor);

        boolean handled = w.handleInput(new InputChar(KEY_DOWN));
        assertThat(handled, is(true));

        verify(cursor).moveDown();
    }

    @Test
    public void arrowUpMovesCursorOneUp() {
        TextGridWidget.Cursor cursor = mock(TextGridWidget.Cursor.class);
        GridWidget w = new StandAloneGridWidget(cursor);

        boolean handled = w.handleInput(new InputChar(KEY_UP));
        assertThat(handled, is(true));

        verify(cursor).moveUp();
    }

    @Test
    public void arrowLeftMovesCursorOneLeft() {
        TextGridWidget.Cursor cursor = mock(TextGridWidget.Cursor.class);
        GridWidget w = new StandAloneGridWidget(cursor);

        boolean handled = w.handleInput(new InputChar(KEY_LEFT));
        assertThat(handled, is(true));

        verify(cursor).moveLeft();
    }

    @Test
    public void arrowRightMovesCursorOneRight() {
        TextGridWidget.Cursor cursor = mock(TextGridWidget.Cursor.class);
        GridWidget w = new StandAloneGridWidget(cursor);

        boolean handled = w.handleInput(new InputChar(KEY_RIGHT));
        assertThat(handled, is(true));

        verify(cursor).moveRight();
    }

    @Test
    public void cursorMoveRightSetsWidgetCursorPosition() {
        TextGridWidget.Cursor cursor = new TextGridWidget.Cursor();
        cursor.setLocation(Grid.Location.of(Grid.Row.First, Grid.Column.Second));

        cursor.moveRight();

        assertThat(cursor.getLocation(), is(Grid.Location.of(Grid.Row.First, Grid.Column.Third)));
    }

    @Test
    public void cursorMoveLeftSetsWidgetCursorPosition() {
        TextGridWidget.Cursor cursor = new TextGridWidget.Cursor();
        cursor.setLocation(Grid.Location.of(Grid.Row.First, Grid.Column.Second));

        cursor.moveLeft();

        assertThat(cursor.getLocation(), is(Grid.Location.of(Grid.Row.First, Grid.Column.First)));
    }

    @Test
    public void cursorMoveLeftFlipsOverCursorPosition() {
        TextGridWidget.Cursor cursor = new TextGridWidget.Cursor();
        cursor.setLocation(Grid.Location.of(Grid.Row.First, Grid.Column.First));

        cursor.moveLeft();

        assertThat(cursor.getLocation(), is(Grid.Location.of(Grid.Row.First, Grid.Column.Third)));
    }

    @Test
    public void cursorMoveRightFlipsOverCursorPosition() {
        TextGridWidget.Cursor cursor = new TextGridWidget.Cursor();
        cursor.setLocation(Grid.Location.of(Grid.Row.First, Grid.Column.Third));

        cursor.moveRight();

        assertThat(cursor.getLocation(), is(Grid.Location.of(Grid.Row.First, Grid.Column.First)));
    }

    @Test
    public void cursorMoveUpSetsWidgetCursorPosition() {
        TextGridWidget.Cursor cursor = new TextGridWidget.Cursor();
        cursor.setLocation(Grid.Location.of(Grid.Row.Second, Grid.Column.Second));

        cursor.moveUp();

        assertThat(cursor.getLocation(), is(Grid.Location.of(Grid.Row.First, Grid.Column.Second)));
    }

    @Test
    public void cursorMoveDownSetsWidgetCursorPosition() {
        TextGridWidget.Cursor cursor = new TextGridWidget.Cursor();
        cursor.setLocation(Grid.Location.of(Grid.Row.Second, Grid.Column.Second));

        cursor.moveDown();

        assertThat(cursor.getLocation(), is(Grid.Location.of(Grid.Row.Third, Grid.Column.Second)));
    }

    @Test
    public void cursorMoveDownFlipsOverCursorPosition() {
        TextGridWidget.Cursor cursor = new TextGridWidget.Cursor();
        cursor.setLocation(Grid.Location.of(Grid.Row.Third, Grid.Column.Second));

        cursor.moveDown();

        assertThat(cursor.getLocation(), is(Grid.Location.of(Grid.Row.First, Grid.Column.Second)));
    }

    @Test
    public void cursorMoveUpFlipsOverCursorPosition() {
        TextGridWidget.Cursor cursor = new TextGridWidget.Cursor();
        cursor.setLocation(Grid.Location.of(Grid.Row.First, Grid.Column.Second));

        cursor.moveUp();

        assertThat(cursor.getLocation(), is(Grid.Location.of(Grid.Row.Third, Grid.Column.Second)));
    }
}
