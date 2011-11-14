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

        public abstract int getRecursiveValue(int alpha, int beta);
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

    @Test
    public void leafNodeGivesScore() {
        LeafNode node = createLeafNode(42);

        assertThat(node.getRecursiveValue(Integer.MIN_VALUE, Integer.MAX_VALUE), is(42));
    }


    @Test
    public void maxNodeGivesMaximizedScoreOfChildren() {
        final LeafNode a = createLeafNode(42);
        final LeafNode b = createLeafNode(12);
        final LeafNode c = createLeafNode(-4);

        MaxNode maxNode = createMaxNode(a, b, c);

        assertThat(maxNode.getRecursiveValue(Integer.MIN_VALUE, Integer.MAX_VALUE), is(42));
    }

    private ValueLeafNode createLeafNode(int value) {
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
        final LeafNode a = createLeafNode(42);
        final LeafNode b = createLeafNode(12);
        final LeafNode c = createLeafNode(-4);

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
        LeafNode e = createLeafNode(9);
        LeafNode f = createLeafNode(-6);
        LeafNode g = createLeafNode(0);
        LeafNode h = createLeafNode(0);
        LeafNode i = createLeafNode(-2);
        LeafNode j = createLeafNode(-4);
        LeafNode k = createLeafNode(-3);

        MinNode b = createMinNode(e, f, g);
        MinNode c = createMinNode(h, i);
        MinNode d = createMinNode(j, k);

        MaxNode a = createMaxNode(b, c, d);

        assertThat(a.getRecursiveValue(Integer.MIN_VALUE, Integer.MAX_VALUE), is(-2));
    }

    @Test
    public void threeLevelsWithMinInRoot() {
        LeafNode e = createLeafNode(9);
        LeafNode f = createLeafNode(-6);
        LeafNode g = createLeafNode(0);
        LeafNode h = createLeafNode(0);
        LeafNode i = createLeafNode(-2);
        LeafNode j = createLeafNode(-4);
        LeafNode k = createLeafNode(-3);

        MaxNode b = createMaxNode(e, f, g);
        MaxNode c = createMaxNode(h, i);
        MaxNode d = createMaxNode(j, k);

        MinNode a = createMinNode(b, c, d);

        assertThat(a.getRecursiveValue(Integer.MIN_VALUE, Integer.MAX_VALUE), is(-3));
    }

    @Test
    public void alphaBetaCutOff() throws Exception {
        LeafNode e = createLeafNode(3);
        LeafNode f = createLeafNode(12);
        LeafNode g = createLeafNode(8);
        LeafNode h = createLeafNode(2);
        LeafNode i = spy(createLeafNode(4));
        LeafNode j = spy(createLeafNode(6));
        LeafNode k = createLeafNode(14);
        LeafNode l = createLeafNode(5);
        LeafNode m = createLeafNode(2);

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
