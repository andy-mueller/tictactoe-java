package com.crudetech.tictactoe.usecases;

import com.crudetech.tictactoe.game.Grid;

public class AlphaBetaPruningPlayer extends AiPlayerReference {
    @Override
    public Grid.Location nextMove() {
        return Grid.Location.of(Grid.Row.Second, Grid.Column.Second);
    }
}
