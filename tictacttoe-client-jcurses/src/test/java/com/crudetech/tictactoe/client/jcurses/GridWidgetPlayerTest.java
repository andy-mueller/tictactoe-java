package com.crudetech.tictactoe.client.jcurses;

import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class GridWidgetPlayerTest {
    @Test
    public void yourTurnSetsPassedGridOnWidget() {
        GridWidgetView widget = mock(GridWidgetView.class);

        Grid currentGrid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Cross
        });

        GridWidgetPlayer player = new GridWidgetPlayer(widget);
        player.yourTurn(currentGrid);

        verify(widget).setGrid(currentGrid);
    }
    @Test
    public void yourTurnMovesCursorToNextOpenSpot() {
        GridWidgetView widget = mock(GridWidgetView.class);

        Grid currentGrid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Cross
        });

        GridWidgetPlayer player = new GridWidgetPlayer(widget);
        player.yourTurn(currentGrid);

        verify(widget).moveCursorToFirstEmptyCell(currentGrid);
    }
}
