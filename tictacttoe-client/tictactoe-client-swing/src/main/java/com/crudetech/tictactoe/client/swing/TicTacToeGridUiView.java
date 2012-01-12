package com.crudetech.tictactoe.client.swing;

import com.crudetech.tictactoe.client.swing.grid.TicTacToeGridModel;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.ui.UiView;

class TicTacToeGridUiView implements UiView {
    private final TicTacToeGridModel gridModel;

    TicTacToeGridUiView(TicTacToeGridModel gridModel) {
        this.gridModel = gridModel;
    }

    @Override
    public void setGrid(Grid grid) {
        gridModel.setModelObject(grid);
    }

    @Override
    public void highlight(Grid.Triple triple) {
        System.out.println("Highlight: " + triple);
    }
}
