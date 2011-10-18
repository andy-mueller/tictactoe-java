package com.crudetech.tictactoe.game;

public class NaiveTryAndErrorPlayer implements Player{
    private TicTacToeGame game;


    @Override
    public void yourTurn(Grid actualGrid) {
        for(Grid.Cell cell : actualGrid.getCells()){
            if(cell.getMark() == Grid.Mark.None){
                game.addMark(this, cell.getLocation());
                break;
            }
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
