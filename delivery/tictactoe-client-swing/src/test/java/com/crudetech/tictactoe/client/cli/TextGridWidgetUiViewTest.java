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
    private Grid gridModel;
    private Grid.Triple triple;

    @Before
    public void setUp() throws Exception {
        pipe = new PrintWriter(new StringWriter());
        widget = mock(TextGridWidget.class);
        uiView = new TextGridWidgetUiView(widget, pipe);
        gridModel = LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.Nought,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.None);
        triple = Grid.Triple.of(
                Grid.Mark.Cross,
                Grid.Location.of(Grid.Row.First, Grid.Column.First),
                Grid.Location.of(Grid.Row.Second, Grid.Column.Second),
                Grid.Location.of(Grid.Row.Third, Grid.Column.Third));
    }

    @Test
    public void givenCallToSetModel_UiViewDelegatesToWidget() throws Exception {
        uiView.setModel(gridModel);

        verify(widget).setModel(gridModel);
    }

    @Test
    public void givenCallToHighlight_UiViewDelegatesToWidget() throws Exception {
        uiView.highlight(triple);

        verify(widget).highlight(triple);
    }

    @Test
    public void givenCallToSetModel_UiViewRepaintsWidget() throws Exception {
        uiView.setModel(gridModel);

        verify(widget).render(pipe);
    }
    @Test
    public void givenCallToHighlight_UiViewRepaintsWidget() throws Exception {
        uiView.highlight(triple);

        verify(widget).render(pipe);
    }
}
