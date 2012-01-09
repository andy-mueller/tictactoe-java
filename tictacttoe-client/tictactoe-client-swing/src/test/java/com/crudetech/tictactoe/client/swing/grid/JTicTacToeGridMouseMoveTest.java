package com.crudetech.tictactoe.client.swing.grid;


import com.crudetech.event.EventListener;
import com.crudetech.event.EventObject;
import com.crudetech.tictactoe.game.Grid;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.MouseEvent;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class JTicTacToeGridMouseMoveTest {

    private JTicTacToeGrid grid;
    private Style style;

    @Before
    public void setUp() throws Exception {
        grid = new JTicTacToeGrid();
        style = new StyleStub();
        grid.getUI().setStyle(style);
    }

    @Test
    public void mouseClickOutsideOfCellDoesNotRaiseCellClickedEvent() throws Exception {
        EventListener<JTicTacToeGrid.CellClickedEventObject> cellClicked = newEventListenerMock();
        grid.cellClicked().addListener(cellClicked);

        MouseEvent click = buildLButtonClickedEvent(4000, 4000);
        grid.raiseMouseEvent(click);

        verify(cellClicked, never()).onEvent(any(JTicTacToeGrid.CellClickedEventObject.class));
    }

    @SuppressWarnings("unchecked")
    private <T extends EventObject<?>> EventListener<T> newEventListenerMock() {
        return mock(EventListener.class);
    }

    private MouseEvent buildLButtonClickedEvent(int x, int y) {
        return new MouseEvent(grid, MouseEvent.MOUSE_CLICKED,
                System.currentTimeMillis(), 1, x, y, 1, false, MouseEvent.BUTTON1);
    }

    private MouseEvent buildLButtonClickedEvent(Point pos) {
        return new MouseEvent(grid, MouseEvent.MOUSE_CLICKED,
                System.currentTimeMillis(), 1, pos.x, pos.y, 1, false, MouseEvent.BUTTON1);
    }

    @Test
    public void mouseClickOnCellRaisesCellClickedEventWithCorrectCells() throws Exception {

        @SuppressWarnings("unchecked")
        EventListener<JTicTacToeGrid.CellClickedEventObject> cellClicked = newEventListenerMock();
        grid.cellClicked().addListener(cellClicked);

        Rectangle cell = style.getGridMarkLocations()[2][2];

        MouseEvent click = buildLButtonClickedEvent(getPointInsideOfRectangle(cell));

        grid.raiseMouseEvent(click);

        Grid.Location expectedCell = Grid.Location.of(Grid.Row.First, Grid.Column.First);
        verify(cellClicked).onEvent(new JTicTacToeGrid.CellClickedEventObject(grid, expectedCell));
    }

    private Point getPointInsideOfRectangle(Rectangle rectangle) {
        return new Point(rectangle.x + 1, rectangle.y + 1);
    }

    @Test
    public void mouseHoverOnCellHighlightsCell() throws Exception {
        TicTacToeGridModel model = grid.getModel();

        Rectangle cell = style.getGridMarkLocations()[2][2];
        MouseEvent move = buildMouseMovedEvent(getPointInsideOfRectangle(cell));

        grid.raiseMouseEvent(move);

        assertThat(model.hasHighlight(), is(true));
        assertThat(model.getHighlighted(), is(Grid.Location.of(Grid.Row.First, Grid.Column.First)));
    }

    private MouseEvent buildMouseMovedEvent(Point pos) {
        return new MouseEvent(grid, MouseEvent.MOUSE_MOVED,
                System.currentTimeMillis(), 0, pos.x, pos.y, 1, false, MouseEvent.NOBUTTON);
    }
    @Test
    public void mouseHoverOutsideOfCellsUnHighlights() throws Exception {
        TicTacToeGridModel model = grid.getModel();
        model.highlight(Grid.Location.of(Grid.Row.First, Grid.Column.Third));
        MouseEvent move = buildMouseMovedEvent(new Point(40000, 40000));

        grid.raiseMouseEvent(move);

        assertThat(model.hasHighlight(), is(false));
    }
}
