package com.crudetech.tictactoe.client.jcurses;

import com.crudetech.event.EventListener;
import com.crudetech.tictactoe.game.Grid;
import jcurses.system.InputChar;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
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

    @Test
    public void userKeyPressSendsEvent() {
        GridWidget.Cursor cursor = new GridWidget.Cursor();
        GridWidget w = new StandAloneGridWidget(cursor);
        cursor.setLocation(Grid.Location.of(Grid.Row.First, Grid.Column.First));

        @SuppressWarnings("unchecked")
        EventListener<GridWidget.KeyDownEventObject> keyDown = mock(EventListener.class);
        w.keyDownEvent().addListener(keyDown);

        w.handleInput(new InputChar('X'));

        verify(keyDown).onEvent(new GridWidget.KeyDownEventObject(w, 'X'));
    }

    @Test
    public void userKeyPressReplacesCharacterUnderCursor() {
        GridWidget.Cursor cursor = new GridWidget.Cursor();
        GridWidget w = new StandAloneGridWidget(cursor);
        cursor.setLocation(Grid.Location.of(Grid.Row.First, Grid.Column.First));


        w.handleInput(new InputChar('X'));

        String expectedText =
                        " X |   |   " + "\n" +
                        "---+---+---" + "\n" +
                        "   |   |   " + "\n" +
                        "---+---+---" + "\n" +
                        "   |   |   ";

        assertThat(w.getText(), is(expectedText));

    }


    ////handle fails on there chars
    //set mark failswhenallready marked


}
