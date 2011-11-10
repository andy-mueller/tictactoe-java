package com.crudetech.tictactoe.game;


import com.crudetech.collections.Iterables;
import com.crudetech.lang.Compare;

public class GameTree {
    public int alphaBeta(Node node, int depth, int alpha, int beta, Object maxPlayer) {
        return 0;
    }

    enum PlayerStrategy {
        MinPlayer {
            @Override
            Node alphaBeta(Iterable<? extends Node> nodes) {
                Node n = Iterables.firstOf(nodes);
                for (Node current : nodes) {
                    n = Compare.min(n, current);
                }
                return n;
            }
        };

        abstract Node alphaBeta(Iterable<? extends Node> nodes);

    }

    interface Node extends Comparable<Node>{
        int getValue();
    }
    class AbstractNode implements Node{
        @Override
        public int getValue() {
            // if !leaf ->doMinMax
            // else get real value
            return 0;
        }

        @Override
        public int compareTo(Node o) {
            return Integer.valueOf(getValue()).compareTo(o.getValue());
        }
    }
}
