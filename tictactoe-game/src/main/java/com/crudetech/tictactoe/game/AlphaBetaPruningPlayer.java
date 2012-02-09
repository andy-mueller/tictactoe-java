package com.crudetech.tictactoe.game;

import com.crudetech.collections.Iterables;

import static com.crudetech.matcher.Verify.verifyThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class AlphaBetaPruningPlayer extends ComputerPlayer {
    private final Grid.Mark playersMark;
    private final GameTree.Player playersStrategy;
    private final Grid.Mark startPlayersMark = Grid.Mark.Cross;

    AlphaBetaPruningPlayer(Grid.Mark playersMark, GameTree.Player playersStrategy) {
        this.playersMark = playersMark;
        this.playersStrategy = playersStrategy;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Grid.Mark playersMark;

        public Builder withMark(Grid.Mark playersMark) {
            verifyThat(playersMark, is(notNullValue()));
            this.playersMark = playersMark;
            return this;
        }

        public AlphaBetaPruningPlayer asMin() {
            return new AlphaBetaPruningPlayer(playersMark, GameTree.Player.Min);
        }

        public AlphaBetaPruningPlayer asMax() {
            return new AlphaBetaPruningPlayer(playersMark, GameTree.Player.Max);
        }
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
        TicTacToeGameTree gameTree = new TicTacToeGameTree(currentGrid, playersMark, playersStrategy, startPlayersMark);
        return gameTree.bestNextMove();
    }
}
