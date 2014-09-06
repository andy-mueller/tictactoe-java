package com.crudetech.tictactoe.game;

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

        static class EvaluationState {
            private final boolean gameFinished;
            private final int heuristicValue;

            public EvaluationState(boolean gameFinished) {
                this(gameFinished, 0);
            }

            static EvaluationState evaluate(LinearRandomAccessGrid grid, Grid.Mark startPlayerMark) {

                if (grid.isWinForMark(startPlayerMark)) {
                    return new EvaluationState(true, 1);
                } else if (grid.isWinForMark(startPlayerMark.getOpposite())) {
                    return new EvaluationState(true, -1);
                } else if (grid.isTieForFirstPlayersMark(startPlayerMark)) {
                    return new EvaluationState(true, 0);
                } else {
                    return new EvaluationState(false);
                }
            }

            EvaluationState(boolean gameFinished, int heuristicValue) {
                this.gameFinished = gameFinished;
                this.heuristicValue = heuristicValue;
            }

            public int getHeuristicValue() {
                if (!hasFinished())
                    throw new IllegalStateException("The game is ongoing and cannot be evaluated!");

                return heuristicValue;
            }

            public boolean hasFinished() {
                return gameFinished;
            }
        }

        private final EvaluationState evaluationState;

        Node(LinearRandomAccessGrid grid, Grid.Mark mark) {
            this(grid, mark, Grid.Mark.Cross);
        }

        Node(LinearRandomAccessGrid grid, Grid.Mark mark, Grid.Mark startPlayerMark) {
            this.grid = grid;
            this.currentMark = mark;
            this.startPlayerMark = startPlayerMark;
            evaluationState = EvaluationState.evaluate(grid, startPlayerMark);
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
            return evaluationState.hasFinished();
        }

        @Override
        public int getHeuristicValue() {
            return evaluationState.getHeuristicValue();
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
        this.gameTree = new GameTree<>(new Node(grid, mark, startPlayerMark));
    }

    GameTree<Grid> getGameTree() {
        return gameTree;
    }

    Grid bestNextMove() {
        GameTree.AlphaBetaValue<Grid> nextMove = getGameTree().alphaBeta(playerStrategy);
        return nextMove.node.getGameState();
    }
}
