package com.crudetech.tictactoe.client.swing.grid;


import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;

public class TicTacToeGridModel extends Model<Grid> {

    public TicTacToeGridModel(Grid grid) {
        super(grid);
    }

    public TicTacToeGridModel() {
        this(LinearRandomAccessGrid.empty());
    }

}
