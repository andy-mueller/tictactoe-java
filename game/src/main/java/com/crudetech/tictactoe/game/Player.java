package com.crudetech.tictactoe.game;

public interface Player {
    void yourTurn(Grid actualGrid);
    void youWin(Grid actualGrid, Grid.Triple triple);
    void youLoose(Grid actualGrid, Grid.Triple triple);
    void tie(Grid actualGrid);

    void setGame(TicTacToeGame game);
}
