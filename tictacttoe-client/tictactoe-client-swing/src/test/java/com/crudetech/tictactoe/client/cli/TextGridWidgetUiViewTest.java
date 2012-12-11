package com.crudetech.tictactoe.client.cli;

import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;
import org.junit.Before;
import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TextGridWidgetUiViewTest {
    private PrintWriter pipe;
    private TextGridWidget widget;
    private TextGridWidgetUiView uiView;

    @Before
    public void setUp() throws Exception {
        pipe = new PrintWriter(new StringWriter());
        widget = mock(TextGridWidget.class);
        uiView = new TextGridWidgetUiView(widget, pipe);
    }

    @Test
    public void givenCallToSetModel_UiViewDelegatesToWidget() throws Exception {

        Grid gridModel = LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.Nought,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.None);

        uiView.setModel(gridModel);

        verify(widget).setModel(gridModel);
    }

    @Test
    public void givenCallToHighlight_UiViewDelegatesToWidget() throws Exception {
        TextGridWidget widget = mock(TextGridWidget.class);
        TextGridWidgetUiView uiView = new TextGridWidgetUiView(widget, new PrintWriter(pipe));

        Grid.Triple triple = Grid.Triple.of(
                Grid.Mark.Cross,
                Grid.Location.of(Grid.Row.First, Grid.Column.First),
                Grid.Location.of(Grid.Row.Second, Grid.Column.Second),
                Grid.Location.of(Grid.Row.Third, Grid.Column.Third));
        uiView.highlight(triple);

        verify(widget).highlight(triple);
    }

    @Test
    public void givenCallToSetModel_UiViewRepaintsWidget() throws Exception {
        Grid gridModel = LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.Nought,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.None);

        uiView.setModel(gridModel);

        verify(widget).render(pipe);
    }
}
