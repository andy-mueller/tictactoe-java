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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
    public void ctorSetsCursorIntoMiddleCell() {
        GridWidget.Cursor cursor = mock(GridWidget.Cursor.class);
        new GridWidget(cursor);

        verify(cursor).setLocation(Grid.Location.of(Grid.Row.Second, Grid.Column.Second));
    }

    @Test
    public void userKeyPressSendsEvent() {
        GridWidget.Cursor cursor = new GridWidget.Cursor();
        GridWidget w = new StandAloneGridWidget(cursor);
        final Grid.Location cursorLocation = Grid.Location.of(Grid.Row.First, Grid.Column.First);
        cursor.setLocation(cursorLocation);

        final String originalContent = w.getText();

        @SuppressWarnings("unchecked")
        EventListener<GridWidget.KeyDownEventObject> keyDown = mock(EventListener.class);
        w.keyDownEvent().addListener(keyDown);

        w.handleInput(new InputChar('X'));

        verify(keyDown).onEvent(new GridWidget.KeyDownEventObject(w, 'X', cursorLocation));
        assertThat("Grid content was not modified", originalContent, is(w.getText()));
    }


    @Test
    public void setGridTriggersRepaint() {
        GridWidget.Cursor cursor = new GridWidget.Cursor();
        StandAloneGridWidget w = new StandAloneGridWidget(cursor);
        cursor.setLocation(Grid.Location.of(Grid.Row.First, Grid.Column.First));

        assertThat(w.getRepaints(), is(0));

        Grid aGid = LinearRandomAccessGrid.of(
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Cross);


        w.setModel(aGid);

        assertThat(w.getRepaints(), is(1));
    }

    @Test
    public void setGridChangesWidgetContent() {
        GridWidget.Cursor cursor = new GridWidget.Cursor();
        GridWidget w = new StandAloneGridWidget(cursor);
        cursor.setLocation(Grid.Location.of(Grid.Row.Second, Grid.Column.Second));

        Grid currentGrid = LinearRandomAccessGrid.of(
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Cross);


        w.setModel(currentGrid);

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


        Grid currentGrid = LinearRandomAccessGrid.of(
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Cross);


        w.setModel(currentGrid);

        assertThat(cursor, is(new GridWidget.Cursor(location)));
    }

    @Feature(Equivalent.class)
    public static Equivalent.Factory<GridWidget.Cursor> cursorEquivalentFeature() {
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
    public static Equivalent.Factory<GridWidget.KeyDownEventObject> keyDownEventObjectEquivalentFeature() {
        return new Equivalent.Factory<GridWidget.KeyDownEventObject>() {
            public GridWidget widget = new GridWidget();

            @Override
            public GridWidget.KeyDownEventObject createItem() {
                return new GridWidget.KeyDownEventObject(widget, 'P', Grid.Location.of(Grid.Row.First, Grid.Column.First));
            }

            @Override
            public List<GridWidget.KeyDownEventObject> createOtherItems() {
                return asList(
                        new GridWidget.KeyDownEventObject(widget, 'Z', Grid.Location.of(Grid.Row.First, Grid.Column.First)),
                        new GridWidget.KeyDownEventObject(new GridWidget(), 'P', Grid.Location.of(Grid.Row.First, Grid.Column.First)),
                        new GridWidget.KeyDownEventObject(widget, 'P', Grid.Location.of(Grid.Row.Second, Grid.Column.First))
                );
            }
        };
    }

    @Test
    public void moveCursorToFirstEmptyCell() throws Exception {
        GridWidget.Cursor cursor = new GridWidget.Cursor();
        GridWidget widget = new StandAloneGridWidget(cursor);
        cursor.setLocation(Grid.Location.of(Grid.Row.Second, Grid.Column.Second));
        Grid grid = LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.Nought,
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.Nought, Grid.Mark.None);
        widget.setModel(grid);

        widget.moveCursorToFirstMarkedCell(grid);

        assertThat(cursor.getLocation(), is(Grid.Location.of(Grid.Row.First, Grid.Column.Second)));
    }

    @Test
    public void moveCursorToFirstEmptyCellDoesNothingWhenCurrentCellIsAlreadyEmpty() throws Exception {
        GridWidget.Cursor cursor = new GridWidget.Cursor();
        GridWidget widget = new StandAloneGridWidget(cursor);
        cursor.setLocation(Grid.Location.of(Grid.Row.Second, Grid.Column.Second));
        Grid grid = LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.Nought,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.Nought, Grid.Mark.None);
        widget.setModel(grid);

        widget.moveCursorToFirstMarkedCell(grid);

        assertThat(cursor.getLocation(), is(Grid.Location.of(Grid.Row.Second, Grid.Column.Second)));
    }

    @Test
    public void moveCursorToFirstEmptyCellTakesMiddleWhenEverythingIsFull() throws Exception {
        GridWidget.Cursor cursor = new GridWidget.Cursor();
        GridWidget widget = new StandAloneGridWidget(cursor);
        Grid grid = LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Nought,
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.Nought,
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Nought);
        widget.setModel(grid);

        widget.moveCursorToFirstMarkedCell(grid);

        assertThat(cursor.getLocation(), is(Grid.Location.of(Grid.Row.Second, Grid.Column.Second)));
    }

    @Test
    public void highlightChangesWidgetContent() {
        GridWidget.Cursor cursor = new GridWidget.Cursor();
        GridWidget w = new StandAloneGridWidget(cursor);
        cursor.setLocation(Grid.Location.of(Grid.Row.Second, Grid.Column.Second));

        Grid currentGrid = LinearRandomAccessGrid.of(
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Cross);
        w.setModel(currentGrid);

        Grid.Triple diagonalTriple = Grid.Triple.of(Grid.Mark.Cross,
                Grid.Location.of(Grid.Row.Third, Grid.Column.First),
                Grid.Location.of(Grid.Row.Second, Grid.Column.Second),
                Grid.Location.of(Grid.Row.First, Grid.Column.Third));

        w.highlight(diagonalTriple);


        String expectedText =
                "   | X | # " + "\n" +
                        "---+---+---" + "\n" +
                        " O | # |   " + "\n" +
                        "---+---+---" + "\n" +
                        " # |   | X ";

        assertThat(w.getText(), is(expectedText));
    }

    @Test
    public void highlightTriggersRepaint() {
        GridWidget.Cursor cursor = new GridWidget.Cursor();
        StandAloneGridWidget w = new StandAloneGridWidget(cursor);
        cursor.setLocation(Grid.Location.of(Grid.Row.Second, Grid.Column.Second));
        Grid currentGrid = LinearRandomAccessGrid.of(
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Cross);
        w.setModel(currentGrid);

        Grid.Triple diagonalTriple = Grid.Triple.of(Grid.Mark.Cross,
                Grid.Location.of(Grid.Row.Third, Grid.Column.First),
                Grid.Location.of(Grid.Row.Second, Grid.Column.Second),
                Grid.Location.of(Grid.Row.First, Grid.Column.Third));
        final int currentPaintCount = w.getRepaints();

        w.highlight(diagonalTriple);

        assertThat(w.getRepaints(), is(currentPaintCount + 1));
    }
}
