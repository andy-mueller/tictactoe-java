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

    @Override
    public void yourTurn(Grid actualGrid) {
        Grid.Location nextMove = computeNextMove(actualGrid);
        addMark(nextMove);
    }

    protected abstract Grid.Location computeNextMove(Grid actualGrid);
}
