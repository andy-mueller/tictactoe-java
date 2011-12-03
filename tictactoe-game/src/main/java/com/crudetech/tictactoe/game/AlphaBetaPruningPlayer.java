package com.crudetech.tictactoe.game;

import com.crudetech.collections.Iterables;

public class AlphaBetaPruningPlayer implements Player {
    protected TicTacToeGame game;
    private final Grid.Mark mark;

    public AlphaBetaPruningPlayer(Grid.Mark mark) {
        this.mark = mark;
    }

    @Override
    public void yourTurn(Grid actualGrid) {
        LinearRandomAccessGrid currentGrid = LinearRandomAccessGrid.of(actualGrid);
        Grid nextBestMove = nextBestMove(currentGrid);
        Iterable<Grid.Cell> difference = currentGrid.difference(nextBestMove);
        Grid.Cell cell = Iterables.firstOf(difference);

        game.addMark(this, cell.getLocation());
    }

    private Grid nextBestMove(LinearRandomAccessGrid currentGrid) {
        TicTacToeGameTree gameTree = new TicTacToeGameTree(currentGrid, mark, getStrategy());
        return gameTree.bestNextMove();
    }

    private GameTree.Player getStrategy() {
        return mark.equals(Grid.Mark.Cross) ? GameTree.Player.Max : GameTree.Player.Min;
    }

    @Override
    public void youWin(Grid actualGrid, Grid.Triple triple) {
        throw new UnsupportedOperationException("Implement me!");
    }

    @Override
    public void youLoose(Grid actualGrid, Grid.Triple triple) {
        throw new UnsupportedOperationException("Implement me!");
    }

    @Override
    public void tie(Grid g) {
    }


    public void setGame(TicTacToeGame game) {
        this.game = game;
    }
}
