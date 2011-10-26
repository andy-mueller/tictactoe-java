package com.crudetech.tictactoe.game;

import java.util.List;

import static com.crudetech.query.Query.from;
import static com.crudetech.tictactoe.game.GridCells.location;
import static com.crudetech.tictactoe.game.GridCells.markIsEqualTo;

public class NaiveTryAndErrorPlayer implements Player {
    private TicTacToeGame game;


    @Override
    public void yourTurn(Grid actualGrid) {
        List<Grid.Location> cells =
                from(actualGrid.getCells()).where(markIsEqualTo(Grid.Mark.None)).select(location()).toList();
        if (!cells.isEmpty()) {
            game.addMark(this, cells.get(0));
        }
    }


    @Override
    public void youWin(Grid actualGrid, Grid.Triple locations) {
    }

    @Override
    public void youLoose(Grid actualGrid, Grid.Triple triple) {
    }

    @Override
    public void tie(Grid actualGrid) {
    }

    public void setGame(TicTacToeGame game) {
        this.game = game;
    }
}
