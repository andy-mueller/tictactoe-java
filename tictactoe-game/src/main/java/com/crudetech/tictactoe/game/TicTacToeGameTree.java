package com.crudetech.tictactoe.game;

import com.crudetech.functional.UnaryFunction;
import static com.crudetech.query.Query.from;

import static com.crudetech.tictactoe.game.GridCells.location;
import static com.crudetech.tictactoe.game.GridCells.markIsEqualTo;

class TicTacToeGameTree {
    private final GameTree<Grid> gameTree;

    static class Node implements GameTree.Node<Grid> {
        private final LinearRandomAccessGrid grid;
        private final Grid.Mark nextMark = Grid.Mark.Cross;

        public Node(LinearRandomAccessGrid grid) {
            this.grid = grid;
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

        private UnaryFunction<Grid.Location, LinearRandomAccessGrid> permutationOf(final LinearRandomAccessGrid base) {
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
                    return new Node(grid);
                }
            };
        }
    }


    TicTacToeGameTree(LinearRandomAccessGrid grid) {
        this.gameTree = new GameTree<>(new Node(grid));
    }

    GameTree<Grid> getGameTree() {
        return gameTree;
    }

}
