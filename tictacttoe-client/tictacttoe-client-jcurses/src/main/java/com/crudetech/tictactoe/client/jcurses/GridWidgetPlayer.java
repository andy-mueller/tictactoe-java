package com.crudetech.tictactoe.client.jcurses;

import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.ui.UiFeedbackChannel;
import com.crudetech.tictactoe.ui.UiPlayer;

public class GridWidgetPlayer extends UiPlayer{
    private final GridWidget widget;
    public GridWidgetPlayer(GridWidget widget, UiFeedbackChannel uiFeedback) {
        super(widget, uiFeedback);
        this.widget = widget;
    }

    @Override
    public void yourTurn(Grid actualGrid) {
        super.yourTurn(actualGrid);
        widget.moveCursorToFirstMarkedCell(actualGrid);
    }
}
