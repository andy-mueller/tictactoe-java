package com.crudetech.tictactoe.game;

import com.crudetech.collections.Iterables;

public class AlphaBetaPruningPlayer extends ComputerPlayer{
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

        addMark(cell.getLocation());
    }

    private Grid nextBestMove(LinearRandomAccessGrid currentGrid) {
        TicTacToeGameTree gameTree = new TicTacToeGameTree(currentGrid, mark, getStrategy());
        return gameTree.bestNextMove();
    }

    private GameTree.Player getStrategy() {
        return mark.equals(Grid.Mark.Cross) ? GameTree.Player.Max : GameTree.Player.Min;
    }
}
