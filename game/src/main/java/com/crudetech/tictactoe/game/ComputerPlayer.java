package com.crudetech.tictactoe.game;

public abstract class ComputerPlayer implements Player {
    private TicTacToeGame game;

    @Override
    public void youWin(Grid actualGrid, Grid.ThreeInARow triple) {
    }

    @Override
    public void youLoose(Grid actualGrid, Grid.ThreeInARow triple) {
    }

    @Override
    public void tie(Grid g) {
    }

    public void setGame(TicTacToeGame game) {
        this.game = game;
    }

    protected void makeMove(Grid.Location location) {
        game.makeMove(this, location);
    }

    @Override
    public void yourTurn(Grid actualGrid) {
        Grid.Location nextMove = computeNextMove(actualGrid);
        makeMove(nextMove);
    }

    protected abstract Grid.Location computeNextMove(Grid actualGrid);
}
