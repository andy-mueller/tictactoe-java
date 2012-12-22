package com.crudetech.tictactoe.delivery.text.jcurses;

import com.crudetech.event.EventListener;
import com.crudetech.junit.feature.Equivalent;
import com.crudetech.junit.feature.Feature;
import com.crudetech.junit.feature.Features;
import com.crudetech.tictactoe.delivery.text.cli.TextGridWidget;
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
    //   | X |
    //---+---+---
    // O | O |
    //---+---+---
    //   |   | X
    private final Grid nonSpecificGrid = LinearRandomAccessGrid.of(
            Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.None,
            Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.None,
            Grid.Mark.None, Grid.Mark.None, Grid.Mark.Cross);
    //   |   | #
    //---+---+---
    //   | # |
    //---+---+---
    // # |   |
    private final Grid.Triple diagonalTriple = Grid.Triple.of(Grid.Mark.Cross,
            Grid.Location.of(Grid.Row.Third, Grid.Column.First),
            Grid.Location.of(Grid.Row.Second, Grid.Column.Second),
            Grid.Location.of(Grid.Row.First, Grid.Column.Third));

//   O |   | X    <0
//  ---+---+---    1
//   X | O | O    <2
//  ---+---+---    3
//   X | O |      <4
//   ^   ^   ^
//  01234567890

    @Test
    public void ctorSetsCursorIntoMiddleCell() {
        TextGridWidget.Cursor cursor = mock(TextGridWidget.Cursor.class);
        new GridWidget(cursor);

        verify(cursor).setLocation(Grid.Location.of(Grid.Row.Second, Grid.Column.Second));
    }

    @Test
    public void userKeyPressSendsEvent() {
        TextGridWidget.Cursor cursor = new TextGridWidget.Cursor();
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
        TextGridWidget.Cursor cursor = new TextGridWidget.Cursor();
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
        TextGridWidget.Cursor cursor = new TextGridWidget.Cursor();
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
        TextGridWidget.Cursor cursor = new TextGridWidget.Cursor();
        StandAloneGridWidget w = new StandAloneGridWidget(cursor);

        final Grid.Location location = Grid.Location.of(Grid.Row.First, Grid.Column.First);
        cursor.setLocation(location);


        Grid currentGrid = LinearRandomAccessGrid.of(
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Cross);


        w.setModel(currentGrid);

        assertThat(cursor, is(new TextGridWidget.Cursor(location)));
    }

    @Feature(Equivalent.class)
    public static Equivalent.Factory<TextGridWidget.Cursor> cursorEquivalentFeature() {
        return new Equivalent.Factory<TextGridWidget.Cursor>() {
            @Override
            public TextGridWidget.Cursor createItem() {
                return new TextGridWidget.Cursor(Grid.Location.of(Grid.Row.Second, Grid.Column.Second));
            }

            @Override
            public List<TextGridWidget.Cursor> createOtherItems() {
                return asList(new TextGridWidget.Cursor(Grid.Location.of(Grid.Row.First, Grid.Column.Second)));
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
        TextGridWidget.Cursor cursor = new TextGridWidget.Cursor();
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
        TextGridWidget.Cursor cursor = new TextGridWidget.Cursor();
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
        TextGridWidget.Cursor cursor = new TextGridWidget.Cursor();
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
        TextGridWidget.Cursor cursor = new TextGridWidget.Cursor();
        TextGridWidget textWidget = mock(TextGridWidget.class);
        GridWidget gridWidget = new StandAloneGridWidget(cursor, textWidget);

        gridWidget.setModel(nonSpecificGrid);
        gridWidget.highlight(diagonalTriple);

        verify(textWidget).setModel(nonSpecificGrid);
        verify(textWidget).highlight(diagonalTriple);
    }

    @Test
    public void highlightTriggersRepaint() {
        TextGridWidget.Cursor cursor = new TextGridWidget.Cursor();
        StandAloneGridWidget w = new StandAloneGridWidget(cursor);
        cursor.setLocation(Grid.Location.of(Grid.Row.Second, Grid.Column.Second));
        w.setModel(nonSpecificGrid);

        final int currentPaintCount = w.getRepaints();

        w.highlight(diagonalTriple);

        assertThat(w.getRepaints(), is(currentPaintCount + 1));
    }
    @Test
    public void setGridResetsHighlight() {
        TextGridWidget.Cursor cursor = new TextGridWidget.Cursor();
        TextGridWidget textWidget = mock(TextGridWidget.class);
        StandAloneGridWidget w = new StandAloneGridWidget(cursor, textWidget);


        w.setModel(nonSpecificGrid);
        verify(textWidget).highlight(Grid.Triple.Empty);
    }

    @Test
    public void paintMovesCursor() throws Exception {
        TextGridWidget.Cursor cursor = new TextGridWidget.Cursor(Grid.Location.of(Grid.Row.Third, Grid.Column.Third));

        class GridWidgetStub extends StandAloneGridWidget {
            private int cursorX;
            private int cursorY;

            public GridWidgetStub(TextGridWidget.Cursor cursor) {
                super(cursor);
            }

            @Override
            public void setCursorLocation(int x, int y) {
                cursorX = x;
                cursorY = y;
            }
        }

        GridWidgetStub widget = new GridWidgetStub(cursor);

        widget.doPaint();

        assertThat(widget.cursorX, is(cursor.getTextPositionX()));
        assertThat(widget.cursorY, is(cursor.getTextPositionY()));

    }
}
