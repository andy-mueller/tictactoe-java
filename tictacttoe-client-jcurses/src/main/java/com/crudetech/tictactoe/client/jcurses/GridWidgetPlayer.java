package com.crudetech.tictactoe.client.jcurses;

import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.Player;

class GridWidgetPlayer implements Player {
    private final GridWidget widget;

    GridWidgetPlayer(GridWidget widget) {
        this.widget = widget;
    }

    @Override
    public void yourTurn(Grid actualGrid) {
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
