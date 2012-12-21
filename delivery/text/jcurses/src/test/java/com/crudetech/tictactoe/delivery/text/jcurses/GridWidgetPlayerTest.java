package com.crudetech.tictactoe.delivery.text.jcurses;

import com.crudetech.tictactoe.delivery.text.jcurses.GridWidget;
import com.crudetech.tictactoe.delivery.text.jcurses.GridWidgetPlayer;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;
import com.crudetech.tictactoe.ui.UiFeedbackChannel;
import com.crudetech.tictactoe.ui.UiPlayer;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class GridWidgetPlayerTest {
    private UiFeedbackChannel uiFeedback;
    private UiPlayer player;
    private GridWidget widget;

    @Before
    public void setUp() throws Exception {
        widget = mock(GridWidget.class);
        uiFeedback = mock(UiFeedbackChannel.class);
        player = new GridWidgetPlayer(widget, uiFeedback);
    }

    @Test
    public void yourTurnMovesCursorToNextOpenSpot() {
        Grid currentGrid = LinearRandomAccessGrid.of(
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Cross);

        player.yourTurn(currentGrid);

        verify(widget).moveCursorToFirstMarkedCell(currentGrid);
    }
}
