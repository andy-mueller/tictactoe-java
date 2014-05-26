package com.crudetech.tictactoe.delivery.text.cli;


import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;
import org.junit.Before;
import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TextGridWidgetTest {
    private TextGridWidget widget;
    private StringWriter stringWriter;

    @Before
    public void setUp() throws Exception {
        stringWriter = new StringWriter();
        widget = new TextGridWidget();
        widget.setModel(LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.Cross,
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.Cross,
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Nought));
    }

    //   O |   | X    <0
    //  ---+---+---    1
    //   X | O | O    <2
    //  ---+---+---    3
    //   X | O |      <4
    //   ^   ^   ^
    //  01234567890
    @Test
    public void givenAGridWasSet_WidgetDisplaysItToUser() {
        String expectedOutput =
                " X |   | X " + "\n" +
                        "---+---+---" + "\n" +
                        " X | O | X " + "\n" +
                        "---+---+---" + "\n" +
                        " X | X | O ";

        assertWidgetRenders(expectedOutput);
    }

    @Test
    public void givenAGridWasSetAndTripleHighlighted_WidgetDisplaysItToUser() {
        Grid.ThreeInARow triple = Grid.ThreeInARow.of(
                Grid.Mark.Cross,
                Grid.Location.of(Grid.Row.First, Grid.Column.First),
                Grid.Location.of(Grid.Row.Second, Grid.Column.Second),
                Grid.Location.of(Grid.Row.Third, Grid.Column.Third));
        widget.highlight(triple);

        String expectedOutput =
                        " # |   | X " + "\n" +
                        "---+---+---" + "\n" +
                        " X | # | X " + "\n" +
                        "---+---+---" + "\n" +
                        " X | X | # ";

        assertWidgetRenders(expectedOutput);
    }

    private void assertWidgetRenders(String expectedOutput) {
        widget.render(new PrintWriter(stringWriter));
        assertThat(stringWriter.toString(), is(expectedOutput));
    }
}
