package com.crudetech.tictactoe.game;

public interface Player {
    void yourTurn(Grid actualGrid);
    void youWin(Grid actualGrid, Grid.ThreeInARow triple);
    void youLoose(Grid actualGrid, Grid.ThreeInARow triple);
    void tie(Grid actualGrid);

    void setGame(TicTacToeGame game);
}
