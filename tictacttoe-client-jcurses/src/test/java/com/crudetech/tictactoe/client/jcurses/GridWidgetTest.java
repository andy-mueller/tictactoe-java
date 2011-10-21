package com.crudetech.tictactoe.client.jcurses;

import com.crudetech.event.EventListener;
import com.crudetech.junit.feature.Equivalent;
import com.crudetech.junit.feature.Feature;
import com.crudetech.junit.feature.Features;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;
import jcurses.system.InputChar;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

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
    public void setGridTriggersRepaint() {
        GridWidget.Cursor cursor = new GridWidget.Cursor();
        StandAloneGridWidget w = new StandAloneGridWidget(cursor);
        cursor.setLocation(Grid.Location.of(Grid.Row.First, Grid.Column.First));

        assertThat(w.getRepaints(), is(0));

        Grid currentGrid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Cross
        });


        w.setGrid(currentGrid);

        assertThat(w.getRepaints(), is(1));
    }

    @Test
    public void setGridChangesWidgetContent() {
        GridWidget.Cursor cursor = new GridWidget.Cursor();
        GridWidget w = new StandAloneGridWidget(cursor);
        cursor.setLocation(Grid.Location.of(Grid.Row.Second, Grid.Column.Second));

        Grid currentGrid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Cross
        });


        w.setGrid(currentGrid);

        String expectedText =
                        "   | X |   " + "\n" +
                        "---+---+---" + "\n" +
                        " O | O |   " + "\n" +
                        "---+---+---" + "\n" +
                        "   |   | X ";

        assertThat(w.getText(), is(expectedText));
    }

    @Test
    public void setGridLetsCursorInSameLocation() {
        GridWidget.Cursor cursor = new GridWidget.Cursor();
        StandAloneGridWidget w = new StandAloneGridWidget(cursor);

        final Grid.Location location = Grid.Location.of(Grid.Row.First, Grid.Column.First);
        cursor.setLocation(location);


        Grid currentGrid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Cross
        });


        w.setGrid(currentGrid);

        assertThat(cursor, is(new GridWidget.Cursor(location)));
    }

    @Feature(Equivalent.class)
    public static Equivalent.Factory<GridWidget.Cursor> cursorIsEqual() {
        return new Equivalent.Factory<GridWidget.Cursor>() {
            @Override
            public GridWidget.Cursor createItem() {
                return new GridWidget.Cursor(Grid.Location.of(Grid.Row.Second, Grid.Column.Second));
            }

            @Override
            public List<GridWidget.Cursor> createOtherItems() {
                return asList(new GridWidget.Cursor(Grid.Location.of(Grid.Row.First, Grid.Column.Second)));
            }
        };
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
}
