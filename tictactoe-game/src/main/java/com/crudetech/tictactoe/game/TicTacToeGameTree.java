package com.crudetech.tictactoe.game;

import com.crudetech.functional.UnaryFunction;

import static com.crudetech.query.Query.from;

import static com.crudetech.tictactoe.game.GridCells.location;
import static com.crudetech.tictactoe.game.GridCells.markIsEqualTo;

class TicTacToeGameTree {
    private final GameTree<Grid> gameTree;

    static class Node implements GameTree.Node<Grid> {
        private final LinearRandomAccessGrid grid;
        private final Grid.Mark currentMark;
        private Grid.Mark startPlayerMark = Grid.Mark.Cross;

        public Node(LinearRandomAccessGrid grid, Grid.Mark currentMark) {
            this.grid = grid;
            this.currentMark = currentMark;
        }

        @Override
        public Grid getGameState() {
            return grid;
        }

        @Override
        public boolean hasFinished() {
            throw new UnsupportedOperationException("Implement me!");
        }

        @Override
        public int getValue() {
            if (grid.isWinForMark(startPlayerMark)) {
                return 1;
            } else if (grid.isWinForMark(startPlayerMark.getOpposite())) {
                return -1;
//            } else if (grid.isTie()) {
//                return 0;
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
                    permutedGrid.setAt(location, currentMark.getOpposite());
                    return permutedGrid;
                }
            };
        }

        private UnaryFunction<LinearRandomAccessGrid, Node> toNode() {
            return new UnaryFunction<LinearRandomAccessGrid, Node>() {
                @Override
                public Node execute(LinearRandomAccessGrid grid) {
                    return new Node(grid, currentMark.getOpposite());
                }
            };
        }
    }


    TicTacToeGameTree(LinearRandomAccessGrid grid, Grid.Mark nextMark) {
        this.gameTree = new GameTree<>(new Node(grid, nextMark));
    }

    GameTree<Grid> getGameTree() {
        return gameTree;
    }

}
