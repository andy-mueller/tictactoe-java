package com.crudetech.tictactoe.game;

import org.junit.Test;

import static com.crudetech.tictactoe.game.GridBuilder.gridOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GridBuilderTest {
    @Test
    public void givenGridAsString_GridCanBeParsed() throws Exception {

        Grid expectedGrid = LinearRandomAccessGrid.of(
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought,
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Nought,
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.None
        );
        LinearRandomAccessGrid prettyFormat = gridOf("" +
                "   |   | O " +
                "---+---+---" +
                " X | X | O " +
                "---+---+---" +
                " X | O |   "
        );
        LinearRandomAccessGrid shortFormat = gridOf("" +
                "*|*|O" +
                "X|X|O" +
                "X|O|*"
        );

        assertThat(prettyFormat, is(expectedGrid));
        assertThat(shortFormat, is(expectedGrid));
    }

}
