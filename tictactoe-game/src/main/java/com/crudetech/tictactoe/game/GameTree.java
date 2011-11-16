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
            Player nextPlayer() {
                return Min;
            }

            @Override
            public Pair<Integer, Node> alphaBeta(Node node, int alpha, int beta) {
                if (node.hasFinished()) {
                    return new Pair<>(node.getValue(), node);
                }
                Pair<Integer, Node> alphaPair = new Pair<>(alpha, null);
                for (Node child : node.getChildren()) {
                    Pair<Integer, Node> value = nextPlayer().alphaBeta(child, alphaPair.getFirst(), beta);

                    if (value.getFirst() >= alphaPair.getFirst()) {
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
            Player nextPlayer() {
                return Max;
            }

            @Override
            public Pair<Integer, Node> alphaBeta(Node node, int alpha, int beta) {
                if (node.hasFinished()) {
                    return new Pair<>(node.getValue(), node);
                }
                Pair<Integer, Node> betaPair = new Pair<>(beta, null);
                for (Node child : node.getChildren()) {
                    Pair<Integer, Node> value = nextPlayer().alphaBeta(child, alpha, betaPair.getFirst());
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

        abstract Player nextPlayer();

        abstract Pair<Integer, Node> alphaBeta(Node node, int alpha, int beta);
    }

    static Pair<Integer, Node> alphaBeta(Node node, int alpha, int beta, Player player) {
        return player.alphaBeta(node, alpha, beta);
    }

    public Pair<Integer, Node> alphaBeta(Player player) {
        return alphaBeta(root, Integer.MIN_VALUE, Integer.MAX_VALUE, player);
    }
}
