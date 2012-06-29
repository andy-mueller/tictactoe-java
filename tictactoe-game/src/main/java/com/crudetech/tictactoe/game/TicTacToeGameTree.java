package com.crudetech.tictactoe.game;

import com.crudetech.collections.Pair;
import com.crudetech.functional.UnaryFunction;

import static com.crudetech.query.Query.from;
import static com.crudetech.tictactoe.game.GridCells.location;
import static com.crudetech.tictactoe.game.GridCells.markIsEqualTo;

class TicTacToeGameTree {
    private final GameTree<Grid> gameTree;
    private final GameTree.Player playerStrategy;

    static class Node implements GameTree.Node<Grid> {
        private final LinearRandomAccessGrid grid;
        private final Grid.Mark currentMark;
        private final Grid.Mark startPlayerMark;

        Node(LinearRandomAccessGrid grid, Grid.Mark mark) {
            this(grid, mark, Grid.Mark.Cross);
        }
        Node(LinearRandomAccessGrid grid, Grid.Mark mark, Grid.Mark startPlayerMark) {
            this.grid = grid;
            this.currentMark = mark;
            this.startPlayerMark= startPlayerMark;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "grid=" + grid +
                    ", currentMark=" + currentMark +
                    ", startPlayerMark=" + startPlayerMark +
                    '}';
        }

        @Override
        public Grid getGameState() {
            return grid;
        }

        @Override
        public boolean hasFinished() {
            return grid.isTieForFirstPlayersMark(startPlayerMark)
                || grid.isWinForMark(currentMark)
                || grid.isWinForMark(currentMark.getOpposite());
        }

        @Override
        public int getValue() {
            if (grid.isWinForMark(startPlayerMark)) {
                return 1;
            } else if (grid.isWinForMark(startPlayerMark.getOpposite())) {
                return -1;
            } else if (grid.isTieForFirstPlayersMark(startPlayerMark)) {
                return 0;
            }
            throw new IllegalStateException("The game is ongoing and cannot be evaluated!");
        }

        @Override
        public Iterable<Node> getChildren() {
            return from(grid.getCells())
                    .where(markIsEqualTo(Grid.Mark.None))
                    .select(location())
                    .select(permutationOf(grid))
                    .select(toNode());
        }

        private UnaryFunction<Grid.Location, LinearRandomAccessGrid> permutationOf(final Grid base) {
            return new UnaryFunction<Grid.Location, LinearRandomAccessGrid>() {
                @Override
                public LinearRandomAccessGrid execute(Grid.Location location) {
                    LinearRandomAccessGrid permutedGrid = new LinearRandomAccessGrid(base);
                    permutedGrid.setAt(location, currentMark);
                    return permutedGrid;
                }
            };
        }

        private UnaryFunction<LinearRandomAccessGrid, Node> toNode() {
            return new UnaryFunction<LinearRandomAccessGrid, Node>() {
                @Override
                public Node execute(LinearRandomAccessGrid grid) {
                    return new Node(grid, currentMark.getOpposite(), startPlayerMark);
                }
            };
        }
    }

    TicTacToeGameTree(LinearRandomAccessGrid grid, Grid.Mark mark, GameTree.Player playerStrategy, Grid.Mark startPlayerMark) {
        this.playerStrategy = playerStrategy;
        this.gameTree = new GameTree<Grid>(new Node(grid, mark, startPlayerMark));
    }

    GameTree<Grid> getGameTree() {
        return gameTree;
    }

    Grid bestNextMove() {
        Pair<Integer, GameTree.Node<Grid>> nextMove = getGameTree().alphaBeta(playerStrategy);
        return nextMove.getSecond().getGameState();
    }
}
