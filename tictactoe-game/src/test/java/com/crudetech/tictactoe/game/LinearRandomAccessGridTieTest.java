package com.crudetech.tictactoe.game;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LinearRandomAccessGridTieTest {

    @Test
    public void checkingForTieIsTrueOnTie() {
        LinearRandomAccessGrid tie = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Nought,
                Grid.Mark.Nought, Grid.Mark.Cross, Grid.Mark.Cross,
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.Nought,
        });

        assertThat(tie.isTieForFirstPlayersMark(Grid.Mark.Cross), is(true));
        assertThat(tie.isTieForFirstPlayersMark(Grid.Mark.Nought), is(true));
    }

    @Test
    public void checkingForTieIsFalseWhenOnePlayerWins() {
        LinearRandomAccessGrid win = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Nought,
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Cross,
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.Nought,
        });

        assertThat(win.isTieForFirstPlayersMark(Grid.Mark.Cross), is(false));
        assertThat(win.isTieForFirstPlayersMark(Grid.Mark.Nought), is(false));
    }

    @Test
    public void checkingForTieIsFalseWhenGameNotFinished() {
        LinearRandomAccessGrid tie = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Nought,
                Grid.Mark.None, Grid.Mark.Nought, Grid.Mark.Nought,
        });

        assertThat(tie.isTieForFirstPlayersMark(Grid.Mark.Cross), is(false));
        assertThat(tie.isTieForFirstPlayersMark(Grid.Mark.Nought), is(false));
    }

    @Test
    public void checkForTieIsTrueWhenPlayerCrossIsNextAndWillFinishWithATie() {
        LinearRandomAccessGrid tie = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Nought,
                Grid.Mark.Nought, Grid.Mark.Cross, Grid.Mark.Cross,
                Grid.Mark.None, Grid.Mark.Nought, Grid.Mark.Nought,
        });

        assertThat(tie.isTieForFirstPlayersMark(Grid.Mark.Cross), is(true));
        assertThat(tie.isTieForFirstPlayersMark(Grid.Mark.Nought), is(false));
    }

    @Test
    public void checkForTieIsTrueWhenPlayerNoughtIsNextAndWillFinishWithATie() {
        LinearRandomAccessGrid tie = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.Cross,
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.Nought,
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.Cross,
        });
        assertThat(tie.isTieForFirstPlayersMark(Grid.Mark.Cross), is(false));
        assertThat(tie.isTieForFirstPlayersMark(Grid.Mark.Nought), is(true));
    }
}
