package com.crudetech.tictactoe.client.jcurses;

import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.Player;

class GridWidgetPlayer implements Player {
    private final GridWidgetView widget;
    private final UserFeedbackChannel userFeedback;

    GridWidgetPlayer(GridWidgetView widget, UserFeedbackChannel userFeedback) {
        this.widget = widget;
        this.userFeedback = userFeedback;
    }

    @Override
    public void yourTurn(Grid actualGrid) {
        widget.setGrid(actualGrid);
        widget.moveCursorToFirstMarkedCell(actualGrid);
    }

    @Override
    public void youWin(Grid actualGrid, Grid.Triple triple) {
        userFeedback.showMessage("You win!");
        widget.highlight(triple);
    }

    @Override
    public void youLoose(Grid actualGrid, Grid.Triple triple) {
        throw new UnsupportedOperationException("Implement me!");
    }

    @Override
    public void tie(Grid actualGrid) {
        throw new UnsupportedOperationException("Implement me!");
    }
}
