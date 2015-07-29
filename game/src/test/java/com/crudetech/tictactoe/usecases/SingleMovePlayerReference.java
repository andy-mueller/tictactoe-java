package com.crudetech.tictactoe.usecases;

import com.crudetech.tictactoe.game.Grid;

class SingleMovePlayerReference extends AiPlayerReference {
     final Grid.Location move;

    public SingleMovePlayerReference(Grid.Location move) {
        this.move = move;
    }
}
