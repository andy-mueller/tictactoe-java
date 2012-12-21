package com.crudetech.tictactoe.delivery.text.cli;

import com.crudetech.tictactoe.game.Grid;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TextGridWidgetCursorTest {
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
