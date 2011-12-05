package com.crudetech.tictactoe.game;

public abstract class ComputerPlayer implements Player {
    private TicTacToeGame game;

    @Override
    public void youWin(Grid actualGrid, Grid.Triple triple) {
    }

    @Override
    public void youLoose(Grid actualGrid, Grid.Triple triple) {
    }

    @Override
    public void tie(Grid g) {
    }

    public void setGame(TicTacToeGame game) {
        this.game = game;
    }

    protected void addMark(Grid.Location location) {
        game.addMark(this, location);
    }
}
