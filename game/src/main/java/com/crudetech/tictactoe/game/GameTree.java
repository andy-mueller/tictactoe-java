package com.crudetech.tictactoe.game;


public class GameTree<TGameState> {
    private final Node<TGameState> root;

    public interface Node<TGameState> {
        boolean hasFinished();

        int getHeuristicValue();

        Iterable<? extends Node<TGameState>> getChildren();

        TGameState getGameState();
    }

    public static class AlphaBetaValue<TGameState> {
        final Node<TGameState> node;
        final int value;

        AlphaBetaValue(Node<TGameState> node, int value) {
            this.node = node;
            this.value = value;
        }
    }

    public enum Player {
        Max {
            @Override
            Player otherPlayer() {
                return Min;
            }

            @Override
            public <TGameState> AlphaBetaValue<TGameState> alphaBeta(Node<TGameState> node, int alpha, int beta, int depth) {
                if (hasSufficientDepth(node, depth)) {
                    return new AlphaBetaValue<>(node, node.getHeuristicValue());
                }
                AlphaBetaValue<TGameState> currentAlpha = new AlphaBetaValue<>(null, alpha);
                for (Node<TGameState> child : node.getChildren()) {
                    AlphaBetaValue<TGameState> value = otherPlayer().alphaBeta(child, currentAlpha.value, beta, depth - 1);

                    if (value.value > currentAlpha.value) {
                        currentAlpha = new AlphaBetaValue<>(child, value.value);
                    }
                    if (beta <= currentAlpha.value) {
                        break;
                    }
                }
                return currentAlpha;
            }
        },
        Min {
            @Override
            Player otherPlayer() {
                return Max;
            }

            @Override
            public <TGameState> AlphaBetaValue<TGameState> alphaBeta(Node<TGameState> node, int alpha, int beta, int depth) {
                if (hasSufficientDepth(node, depth)) {
                    return new AlphaBetaValue<>(node, node.getHeuristicValue());
                }
                AlphaBetaValue<TGameState> currentBeta = new AlphaBetaValue<>(null, beta);
                for (Node<TGameState> child : node.getChildren()) {
                    AlphaBetaValue<TGameState> value = otherPlayer().alphaBeta(child, alpha, currentBeta.value, depth - 1);
                    if (value.value < currentBeta.value) {
                        currentBeta = new AlphaBetaValue<>(child, value.value);
                    }
                    if (currentBeta.value <= alpha) {
                        break;
                    }
                }
                return currentBeta;
            }

        };

        private static boolean hasSufficientDepth(Node node, int depth) {
            return node.hasFinished() || depth == 0;
        }

        abstract Player otherPlayer();

        abstract <TGameState> AlphaBetaValue<TGameState> alphaBeta(Node<TGameState> node, int alpha, int beta, int depth);
    }

    public GameTree(Node<TGameState> root) {
        this.root = root;
    }

    public Node<TGameState> getRoot() {
        return root;
    }

    static <TGameState> AlphaBetaValue<TGameState> alphaBeta(Node<TGameState> node, int alpha, int beta, int depth, Player player) {
        return player.alphaBeta(node, alpha, beta, depth);
    }

    public AlphaBetaValue<TGameState> alphaBeta(Player player, int depth) {
        return alphaBeta(root, Integer.MIN_VALUE, Integer.MAX_VALUE, depth, player);
    }

    public AlphaBetaValue<TGameState> alphaBeta(Player player) {
        return alphaBeta(player, Integer.MAX_VALUE);
    }
}
