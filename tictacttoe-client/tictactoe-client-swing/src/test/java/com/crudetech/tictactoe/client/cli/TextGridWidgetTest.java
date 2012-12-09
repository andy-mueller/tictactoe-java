package com.crudetech.tictactoe.client.cli;


import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TextGridWidgetTest {
//   O |   | X    <0
//  ---+---+---    1
//   X | O | O    <2
//  ---+---+---    3
//   X | O |      <4
//   ^   ^   ^
//  01234567890
    @Test
    public void givenAGridWasSet_WidgetDisplaysItToUser() {
        TextGridWidget widget = new TextGridWidget();

        widget.setModel(LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.Cross,
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.Cross,
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Nought));

        String expectedOutput =
                        " X |   | X " + "\n" +
                        "---+---+---" + "\n" +
                        " X | O | X " + "\n" +
                        "---+---+---" + "\n" +
                        " X | X | O ";
        assertThat(widget.createTextRepresentation().toString(), is(expectedOutput));
    }
}
