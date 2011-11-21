package com.crudetech.tictactoe.game;

import com.crudetech.functional.UnaryFunction;
import static com.crudetech.query.Query.from;

import static com.crudetech.tictactoe.game.GridCells.location;
import static com.crudetech.tictactoe.game.GridCells.markIsEqualTo;

class TicTacToeGameTree {
    private final GameTree<Grid> gameTree;

    static class Node implements GameTree.Node<Grid> {
        private final Grid grid;
        private final Grid.Mark nextMark;

        public Node(LinearRandomAccessGrid grid, Grid.Mark nextMark) {
            this.grid = grid;
            this.nextMark = nextMark;
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
            throw new UnsupportedOperationException("Implement me!");
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
                    permutedGrid.setAt(location, nextMark);
                    return permutedGrid;
                }
            };
        }

        private UnaryFunction<LinearRandomAccessGrid, Node> toNode() {
            return new UnaryFunction<LinearRandomAccessGrid, Node>() {
                @Override
                public Node execute(LinearRandomAccessGrid grid) {
                    return new Node(grid, nextMark);
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
