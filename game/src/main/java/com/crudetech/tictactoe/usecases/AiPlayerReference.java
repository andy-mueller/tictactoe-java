package com.crudetech.tictactoe.usecases;


import com.crudetech.tictactoe.game.Grid;

public abstract class AiPlayerReference extends PlayerReference {
    @Override
    public void yourTurn(GameReference game, Grid grid) {
        Grid.Location nextMove = nextMove();
        makeMove(game, nextMove);
    }

    public abstract Grid.Location nextMove();

}
