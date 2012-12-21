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
        this.startPlayersMark = startPlayersMark;
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

        AlphaBetaPruningPlayer newPlayerInstance(Grid.Mark playersMark, GameTree.Player strategy, Grid.Mark startPlayersMark) {
            return new AlphaBetaPruningPlayer(playersMark, strategy, startPlayersMark);
        }

        public AlphaBetaPruningPlayer asStartPlayer() {
            return newPlayerInstance(playersMark, GameTree.Player.Max, playersMark);
        }

        public AlphaBetaPruningPlayer asSecondPlayer() {
            return newPlayerInstance(playersMark, GameTree.Player.Min, playersMark.getOpposite());
        }
    }
    @Override
    protected Grid.Location computeNextMove(Grid actualGrid) {
        LinearRandomAccessGrid currentGrid = LinearRandomAccessGrid.of(actualGrid);
        Grid nextBestMove = nextBestMove(currentGrid);
        Iterable<Grid.Cell> difference = currentGrid.difference(nextBestMove);
        return Iterables.firstOf(difference).getLocation();
    }

    private Grid nextBestMove(LinearRandomAccessGrid currentGrid) {
        TicTacToeGameTree gameTree = new TicTacToeGameTree(currentGrid, playersMark, playersStrategy, startPlayersMark);
        return gameTree.bestNextMove();
    }
}
