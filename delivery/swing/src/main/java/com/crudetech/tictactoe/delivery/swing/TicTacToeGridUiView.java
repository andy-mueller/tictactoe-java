package com.crudetech.tictactoe.delivery.swing;

import com.crudetech.tictactoe.delivery.gui.widgets.TicTacToeGridModel;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.ui.UiView;

class TicTacToeGridUiView implements UiView {
    private final TicTacToeGridModel gridModel;

    TicTacToeGridUiView(TicTacToeGridModel gridModel) {
        this.gridModel = gridModel;
    }

    @Override
    public void setModel(Grid grid) {
        gridModel.setGrid(grid);
    }

    @Override
    public void highlight(Grid.ThreeInARow triple) {
        gridModel.highlightThreeInARow(triple);
    }
}
