package com.crudetech.tictactoe.client.jcurses;

import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.Player;

class GridWidgetPlayer implements Player {
    private final GridWidgetView widget;

    GridWidgetPlayer(GridWidgetView widget) {
        this.widget = widget;
    }

    @Override
    public void yourTurn(Grid actualGrid) {
        widget.setGrid(actualGrid);
    }

    @Override
    public void youWin(Grid actualGrid, Grid.Triple triple) {
        throw new UnsupportedOperationException("Implement me!");
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
