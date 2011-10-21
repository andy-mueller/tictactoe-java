package com.crudetech.tictactoe.client.jcurses;

import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;
import org.junit.Test;

public class GridWidgetPlayerTest {
    @Test
    public void yourTurnSetsPassedGridOnWidget() {
        GridWidget widget = new GridWidget();

        Grid currentGrid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Cross
        });
    }
}
