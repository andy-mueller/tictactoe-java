package com.crudetech.tictactoe.client.jcurses;

import com.crudetech.event.EventListener;
import com.crudetech.junit.feature.Equivalent;
import com.crudetech.junit.feature.Feature;
import com.crudetech.junit.feature.Features;
import com.crudetech.tictactoe.game.Grid;
import jcurses.system.InputChar;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static com.crudetech.matcher.ThrowsException.doesThrow;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@RunWith(Features.class)
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

    @Test
    public void userKeyPressSendsEvent() {
        GridWidget.Cursor cursor = new GridWidget.Cursor();
        GridWidget w = new StandAloneGridWidget(cursor);
        cursor.setLocation(Grid.Location.of(Grid.Row.First, Grid.Column.First));

        final String originalContent = w.getText();

        @SuppressWarnings("unchecked")
        EventListener<GridWidget.KeyDownEventObject> keyDown = mock(EventListener.class);
        w.keyDownEvent().addListener(keyDown);

        w.handleInput(new InputChar('X'));

        verify(keyDown).onEvent(new GridWidget.KeyDownEventObject(w, 'X'));
        assertThat("Grid content was not modified", originalContent, is(w.getText()));
    }

    @Test
    public void setCrossOnCursorPosIn1stRow() {
        GridWidget.Cursor cursor = new GridWidget.Cursor();
        GridWidget w = new StandAloneGridWidget(cursor);
        cursor.setLocation(Grid.Location.of(Grid.Row.First, Grid.Column.First));


        w.setMarkAtCursor(Grid.Mark.Cross);

        String expectedText =
                " X |   |   " + "\n" +
                        "---+---+---" + "\n" +
                        "   |   |   " + "\n" +
                        "---+---+---" + "\n" +
                        "   |   |   ";

        assertThat(w.getText(), is(expectedText));
    }

    @Test
    public void setMarkTriggersRepaint() {
        GridWidget.Cursor cursor = new GridWidget.Cursor();
        StandAloneGridWidget w = new StandAloneGridWidget(cursor);
        cursor.setLocation(Grid.Location.of(Grid.Row.First, Grid.Column.First));

        assertThat(w.getRepaints(), is(0));

        w.setMarkAtCursor(Grid.Mark.Cross);

        assertThat(w.getRepaints(), is(1));
    }

    @Test
    public void setNoughtOnCursorPos() {
        GridWidget.Cursor cursor = new GridWidget.Cursor();
        GridWidget w = new StandAloneGridWidget(cursor);
        cursor.setLocation(Grid.Location.of(Grid.Row.First, Grid.Column.First));


        w.setMarkAtCursor(Grid.Mark.Nought);

        String expectedText =
                " O |   |   " + "\n" +
                        "---+---+---" + "\n" +
                        "   |   |   " + "\n" +
                        "---+---+---" + "\n" +
                        "   |   |   ";

        assertThat(w.getText(), is(expectedText));
    }

    @Test
    public void setNoneMArkOnCursorPosThrows() {
        GridWidget.Cursor cursor = new GridWidget.Cursor();
        final GridWidget w = new StandAloneGridWidget(cursor);
        cursor.setLocation(Grid.Location.of(Grid.Row.First, Grid.Column.First));
        final String originalContent = w.getText();

        Runnable setNoMarkOnCursor = new Runnable() {
            @Override
            public void run() {
                w.setMarkAtCursor(Grid.Mark.None);
            }
        };


        assertThat(setNoMarkOnCursor, doesThrow(IllegalArgumentException.class));
        assertThat(w.getText(), is(originalContent));
    }


    @Test
    public void setCrossOnCursorPosIn2ndRowChangesContent() {
        GridWidget.Cursor cursor = new GridWidget.Cursor();
        GridWidget w = new StandAloneGridWidget(cursor);
        cursor.setLocation(Grid.Location.of(Grid.Row.Second, Grid.Column.Second));


        w.setMarkAtCursor(Grid.Mark.Cross);

        String expectedText =
                "   |   |   " + "\n" +
                        "---+---+---" + "\n" +
                        "   | X |   " + "\n" +
                        "---+---+---" + "\n" +
                        "   |   |   ";

        assertThat(w.getText(), is(expectedText));
    }

    @Test
    public void setCrossOnCursorPosIn3rdRowChangesContent() {
        GridWidget.Cursor cursor = new GridWidget.Cursor();
        GridWidget w = new StandAloneGridWidget(cursor);
        cursor.setLocation(Grid.Location.of(Grid.Row.Third, Grid.Column.Third));


        w.setMarkAtCursor(Grid.Mark.Cross);

        String expectedText =
                "   |   |   " + "\n" +
                        "---+---+---" + "\n" +
                        "   |   |   " + "\n" +
                        "---+---+---" + "\n" +
                        "   |   | X ";

        assertThat(w.getText(), is(expectedText));
    }

    @Feature(Equivalent.class)
    public static Equivalent.Factory<GridWidget.KeyDownEventObject> keyDownEventObjectIsEqual() {
        return new Equivalent.Factory<GridWidget.KeyDownEventObject>() {
            public GridWidget widget = new GridWidget();

            @Override
            public GridWidget.KeyDownEventObject createItem() {
                return new GridWidget.KeyDownEventObject(widget, 'P');
            }

            @Override
            public List<GridWidget.KeyDownEventObject> createOtherItems() {
                return asList(
                        new GridWidget.KeyDownEventObject(widget, 'Z'),
                        new GridWidget.KeyDownEventObject(new GridWidget(), 'P')
                );
            }
        };
    }

    ////handle fails on there chars
    //set mark failswhenallready marked


}
