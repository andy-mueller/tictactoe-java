package com.crudetech.tictactoe.game;


import com.crudetech.collections.Pair;

public class GameTree {
    private final Node root;

    public GameTree(Node root) {
        this.root = root;
    }

    public interface Node {
        boolean hasFinished();

        int getValue();

        Iterable<? extends Node> getChildren();
    }

    public enum Player {
        Max {
            @Override
            Player otherPlayer() {
                return Min;
            }

            @Override
            public Pair<Integer, Node> alphaBeta(Node node, int alpha, int beta, int depth, Player player) {
                if (hasSufficientDepth(node, depth)) {
                    return new Pair<>(node.getValue(), node);
                }
                Pair<Integer, Node> alphaPair = new Pair<>(alpha, null);
                for (Node child : node.getChildren()) {
                    Pair<Integer, Node> value = otherPlayer().alphaBeta(child, alphaPair.getFirst(), beta, depth - 1, player);

                    if (value.getFirst() > alphaPair.getFirst()) {
                        alphaPair = new Pair<>(value.getFirst(), child);
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
            public Pair<Integer, Node> alphaBeta(Node node, int alpha, int beta, int depth, Player player) {
                if (hasSufficientDepth(node, depth)) {
                    return new Pair<>(node.getValue(), node);
                }
                Pair<Integer, Node> betaPair = new Pair<>(beta, null);
                for (Node child : node.getChildren()) {
                    Pair<Integer, Node> value = otherPlayer().alphaBeta(child, alpha, betaPair.getFirst(), depth - 1, player);
                    if (value.getFirst() <= betaPair.getFirst()) {
                        betaPair = new Pair<>(value.getFirst(), child);
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

        abstract Pair<Integer, Node> alphaBeta(Node node, int alpha, int beta, int depth, Player player);
    }


    static Pair<Integer, Node> alphaBeta(Node node, int alpha, int beta, int depth, Player player) {
        return player.alphaBeta(node, alpha, beta, depth, player);
    }

    public Pair<Integer, Node> alphaBeta(Player player, int depth) {
        return alphaBeta(root, Integer.MIN_VALUE, Integer.MAX_VALUE, depth, player);
    }

    public Pair<Integer, Node> alphaBeta(Player player) {
        return alphaBeta(player, Integer.MAX_VALUE);
    }
}
