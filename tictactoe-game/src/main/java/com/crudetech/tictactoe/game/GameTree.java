package com.crudetech.tictactoe.game;


import com.crudetech.collections.Pair;

public class GameTree<TGameState> {
    private final Node<TGameState> root;

    public interface Node<TGameState> {
        boolean hasFinished();

        int getValue();

        Iterable<? extends Node<TGameState>> getChildren();

        TGameState getGameState();
    }

    public enum Player {
        Max {
            @Override
            Player otherPlayer() {
                return Min;
            }

            @Override
            public <TGameState> Pair<Integer, Node<TGameState>> alphaBeta(Node<TGameState> node, int alpha, int beta, int depth, Player player) {
                if (hasSufficientDepth(node, depth)) {
                    return new Pair<Integer, Node<TGameState>>(node.getValue(), node);
                }
                Pair<Integer, Node<TGameState>> alphaPair = new Pair<Integer, Node<TGameState>>(alpha, null);
                for (Node<TGameState> child : node.getChildren()) {
                    Pair<Integer, Node<TGameState>> value = otherPlayer().alphaBeta(child, alphaPair.getFirst(), beta, depth - 1, player);

                    if (value.getFirst() > alphaPair.getFirst()) {
                        alphaPair = new Pair<Integer, Node<TGameState>>(value.getFirst(), child);
                    }
                    if (beta <= alphaPair.getFirst()) {
                        break;
                    }
                }
                return alphaPair;
            }
        },
        Min {
            @Override
            Player otherPlayer() {
                return Max;
            }

            @Override
            public <TGameState> Pair<Integer, Node<TGameState>> alphaBeta(Node<TGameState> node, int alpha, int beta, int depth, Player player) {
                if (hasSufficientDepth(node, depth)) {
                    return new Pair<Integer, Node<TGameState>>(node.getValue(), node);
                }
                Pair<Integer, Node<TGameState>> betaPair = new Pair<Integer, Node<TGameState>>(beta, null);
                for (Node<TGameState> child : node.getChildren()) {
                    Pair<Integer, Node<TGameState>> value = otherPlayer().alphaBeta(child, alpha, betaPair.getFirst(), depth - 1, player);
                    if (value.getFirst() < betaPair.getFirst()) {
                        betaPair = new Pair<Integer, Node<TGameState>>(value.getFirst(), child);
                    }
                    if (betaPair.getFirst() <= alpha) {
                        break;
                    }
                }
                return betaPair;
            }

        };

        private static boolean hasSufficientDepth(Node node, int depth) {
            return node.hasFinished() || depth == 0;
        }

        abstract Player otherPlayer();

        abstract <TGameState> Pair<Integer, Node<TGameState>> alphaBeta(Node<TGameState> node, int alpha, int beta, int depth, Player player);
    }

    public GameTree(Node<TGameState> root) {
        this.root = root;
    }

    public Node<TGameState> getRoot() {
        return root;
    }

    static <TGameState> Pair<Integer, Node<TGameState>> alphaBeta(Node<TGameState> node, int alpha, int beta, int depth, Player player) {
        return player.alphaBeta(node, alpha, beta, depth, player);
    }

    public Pair<Integer, Node<TGameState>> alphaBeta(Player player, int depth) {
        return alphaBeta(root, Integer.MIN_VALUE, Integer.MAX_VALUE, depth, player);
    }

    public Pair<Integer, Node<TGameState>> alphaBeta(Player player) {
        return alphaBeta(player, Integer.MAX_VALUE);
    }
}
