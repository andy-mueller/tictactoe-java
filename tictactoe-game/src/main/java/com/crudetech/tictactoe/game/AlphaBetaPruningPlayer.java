package com.crudetech.tictactoe.game;

import com.crudetech.collections.Iterables;

import static com.crudetech.matcher.Verify.verifyThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class AlphaBetaPruningPlayer extends ComputerPlayer {
    private final Grid.Mark playersMark;
    private final GameTree.Player playersStrategy;
    private final Grid.Mark startPlayersMark;

    AlphaBetaPruningPlayer(Grid.Mark playersMark, GameTree.Player playersStrategy, Grid.Mark startPlayersMark) {
        verifyThat(playersMark, is(notNullValue()));
        verifyThat(playersStrategy, is(notNullValue()));
        verifyThat(startPlayersMark, is(notNullValue()));

        this.playersMark = playersMark;
        this.playersStrategy = playersStrategy;
        this.startPlayersMark= startPlayersMark;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Grid.Mark playersMark;
        private Grid.Mark startPlayersMark;

        public Builder withMark(Grid.Mark playersMark) {
            verifyThat(playersMark, is(notNullValue()));
            this.playersMark = playersMark;
            return this;
        }


        public Builder withStartPlayerMark(Grid.Mark startPlayersMark) {
            verifyThat(startPlayersMark, is(notNullValue()));
            this.startPlayersMark = startPlayersMark;
            return this;
        }

        public AlphaBetaPruningPlayer asMin() {
            return new AlphaBetaPruningPlayer(playersMark, GameTree.Player.Min, startPlayersMark);
        }

        public AlphaBetaPruningPlayer asMax() {
            return new AlphaBetaPruningPlayer(playersMark, GameTree.Player.Max, startPlayersMark);
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
