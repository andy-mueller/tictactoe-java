package com.crudetech.tictactoe.game;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyZeroInteractions;

public class GameTreeTest {

    interface Node {
        public int getRecursiveValue(int alpha, int beta);
    }

    abstract class AlphaBetaNode implements Node {
        abstract Iterable<? extends Node> getChildren();
    }

    abstract class MinNode extends AlphaBetaNode {
        @Override
        public int getRecursiveValue(int alpha, int beta) {
            for (Node node : getChildren()) {
                beta = Math.min(beta, node.getRecursiveValue(alpha, beta));
                if (beta <= alpha) {
                    break;
                }
            }
            return beta;
        }
    }

    abstract class MaxNode extends AlphaBetaNode {
        @Override
        public int getRecursiveValue(int alpha, int beta) {
            for (Node node : getChildren()) {
                alpha = Math.max(alpha, node.getRecursiveValue(alpha, beta));
                if (beta <= alpha) {
                    break;
                }
            }
            return alpha;
        }
    }

    abstract class LeafNode implements Node {

    }

    @Test
    public void leafNodeGivesScore() {
        Node node = createLeafNode(42);

        assertThat(node.getRecursiveValue(Integer.MIN_VALUE, Integer.MAX_VALUE), is(42));
    }


    @Test
    public void maxNodeGivesMaximizedScoreOfChildren() {
        final Node a = createLeafNode(42);
        final Node b = createLeafNode(12);
        final Node c = createLeafNode(-4);

        MaxNode maxNode = createMaxNode(a, b, c);

        assertThat(maxNode.getRecursiveValue(Integer.MIN_VALUE, Integer.MAX_VALUE), is(42));
    }

    private Node createLeafNode(int value) {
        class ValueLeafNode extends LeafNode {
            private final int value;

            ValueLeafNode(int value) {
                this.value = value;
            }

            @Override
            public int getRecursiveValue(int alpha, int beta) {
                return value;
            }
        }

        return new ValueLeafNode(value);
    }

    private MaxNode createMaxNode(final Node... nodes) {
        return new MaxNode() {
            @Override
            protected Iterable<? extends Node> getChildren() {
                return asList(nodes);
            }
        };
    }

    @Test
    public void minNodeGivesMaximizedScoreOfChildren() {
        final Node a = createLeafNode(42);
        final Node b = createLeafNode(12);
        final Node c = createLeafNode(-4);

        MinNode maxNode = createMinNode(a, b, c);

        assertThat(maxNode.getRecursiveValue(Integer.MIN_VALUE, Integer.MAX_VALUE), is(-4));
    }

    private MinNode createMinNode(final Node... children) {
        return new MinNode() {
            @Override
            Iterable<? extends Node> getChildren() {
                return asList(children);
            }
        };
    }

    @Test
    public void threeLevelsWithMaxInRoot() {
        Node e = createLeafNode(9);
        Node f = createLeafNode(-6);
        Node g = createLeafNode(0);
        Node h = createLeafNode(0);
        Node i = createLeafNode(-2);
        Node j = createLeafNode(-4);
        Node k = createLeafNode(-3);

        MinNode b = createMinNode(e, f, g);
        MinNode c = createMinNode(h, i);
        MinNode d = createMinNode(j, k);

        MaxNode a = createMaxNode(b, c, d);

        assertThat(a.getRecursiveValue(Integer.MIN_VALUE, Integer.MAX_VALUE), is(-2));
    }

    @Test
    public void threeLevelsWithMinInRoot() {
        Node e = createLeafNode(9);
        Node f = createLeafNode(-6);
        Node g = createLeafNode(0);
        Node h = createLeafNode(0);
        Node i = createLeafNode(-2);
        Node j = createLeafNode(-4);
        Node k = createLeafNode(-3);

        MaxNode b = createMaxNode(e, f, g);
        MaxNode c = createMaxNode(h, i);
        MaxNode d = createMaxNode(j, k);

        MinNode a = createMinNode(b, c, d);

        assertThat(a.getRecursiveValue(Integer.MIN_VALUE, Integer.MAX_VALUE), is(-3));
    }

    @Test
    public void alphaBetaCutOff() throws Exception {
        Node e = createLeafNode(3);
        Node f = createLeafNode(12);
        Node g = createLeafNode(8);
        Node h = createLeafNode(2);
        Node i = spy(createLeafNode(4));
        Node j = spy(createLeafNode(6));
        Node k = createLeafNode(14);
        Node l = createLeafNode(5);
        Node m = createLeafNode(2);

        MinNode b = createMinNode(e, f, g);
        MinNode c = createMinNode(h, i, j);
        MinNode d = createMinNode(k, l, m);

        MaxNode a = createMaxNode(b, c, d);

        assertThat(a.getRecursiveValue(Integer.MIN_VALUE, Integer.MAX_VALUE), is(3));
        verifyZeroInteractions(i, j);
    }

    // min node has max children
    // max node has min children
}
