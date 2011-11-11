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
        int beta = 0;
        int alpha = 0;
        @Override
        public int getRecursiveValue(int alpha, int beta) {
            this.beta = beta;
            this.alpha = alpha;
            for (Node node : getChildren()) {
                applyEvaluation(node.getRecursiveValue(this.alpha, this.beta));
                if (this.beta <= this.alpha) {
                    break;
                }
            }
            return evaluatedValue();
        }

        protected abstract int evaluatedValue();

        abstract void applyEvaluation(int value);
    }

    class MinNode extends AlphaBetaNode{
        private final Iterable<? extends Node> children;

        MinNode(Iterable<? extends Node> children) {
            this.children = children;
        }
        @Override
        protected Iterable<? extends Node> getChildren() {
            return children;
        }

        @Override
        protected int evaluatedValue() {
            return this.beta;
        }

        @Override
        void applyEvaluation(int value) {
            this.beta = Math.min(beta, value);
        }
    }

    class MaxNode extends AlphaBetaNode{
        private final Iterable<? extends Node> children;

        MaxNode(Iterable<? extends Node> children) {
            this.children = children;
        }

        protected Iterable<? extends Node> getChildren() {
            return children;
        }

        @Override
        protected int evaluatedValue() {
            return alpha;
        }

        @Override
        void applyEvaluation(int value) {
            alpha = Math.max(alpha, value);
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
        LeafNode node = new ValueLeafNode(42);

        assertThat(node.getRecursiveValue(Integer.MIN_VALUE, Integer.MAX_VALUE), is(42));
    }


    @Test
    public void maxNodeGivesMaximizedScoreOfChildren() {
        final LeafNode a = new ValueLeafNode(42);
        final LeafNode b = new ValueLeafNode(12);
        final LeafNode c = new ValueLeafNode(-4);

        MaxNode maxNode = new MaxNode(asList(a, b, c)) {
            @Override
            protected Iterable<? extends Node> getChildren() {
                return asList(a, b, c);
            }
        };

        assertThat(maxNode.getRecursiveValue(Integer.MIN_VALUE, Integer.MAX_VALUE), is(42));
    }

    @Test
    public void minNodeGivesMaximizedScoreOfChildren() {
        final LeafNode a = new ValueLeafNode(42);
        final LeafNode b = new ValueLeafNode(12);
        final LeafNode c = new ValueLeafNode(-4);

        MinNode maxNode = new MinNode(asList(a, b, c));

        assertThat(maxNode.getRecursiveValue(Integer.MIN_VALUE, Integer.MAX_VALUE), is(-4));
    }

    @Test
    public void threeLevelsWithMaxInRoot() {
        LeafNode e = new ValueLeafNode(9);
        LeafNode f = new ValueLeafNode(-6);
        LeafNode g = new ValueLeafNode(0);
        LeafNode h = new ValueLeafNode(0);
        LeafNode i = new ValueLeafNode(-2);
        LeafNode j = new ValueLeafNode(-4);
        LeafNode k = new ValueLeafNode(-3);

        MinNode b = new MinNode(asList(e, f, g));
        MinNode c = new MinNode(asList(h, i));
        MinNode d = new MinNode(asList(j, k));

        MaxNode a = new MaxNode(asList(b, c, d));

        assertThat(a.getRecursiveValue(Integer.MIN_VALUE, Integer.MAX_VALUE), is(-2));
    }

    @Test
    public void threeLevelsWithMinInRoot() {
        LeafNode e = new ValueLeafNode(9);
        LeafNode f = new ValueLeafNode(-6);
        LeafNode g = new ValueLeafNode(0);
        LeafNode h = new ValueLeafNode(0);
        LeafNode i = new ValueLeafNode(-2);
        LeafNode j = new ValueLeafNode(-4);
        LeafNode k = new ValueLeafNode(-3);

        MaxNode b = new MaxNode(asList(e, f, g));
        MaxNode c = new MaxNode(asList(h, i));
        MaxNode d = new MaxNode(asList(j, k));

        MinNode a = new MinNode(asList(b, c, d));

        assertThat(a.getRecursiveValue(Integer.MIN_VALUE, Integer.MAX_VALUE), is(-3));
    }

    @Test
    public void alphaBetaCutOff() throws Exception {
        LeafNode e = new ValueLeafNode(3);
        LeafNode f = new ValueLeafNode(12);
        LeafNode g = new ValueLeafNode(8);
        LeafNode h = new ValueLeafNode(2);
        LeafNode i = spy(new ValueLeafNode(4));
        LeafNode j = spy(new ValueLeafNode(6));
        LeafNode k = new ValueLeafNode(14);
        LeafNode l = new ValueLeafNode(5);
        LeafNode m = new ValueLeafNode(2);

        MinNode b = new MinNode(asList(e, f, g));
        MinNode c = new MinNode(asList(h, i, j));
        MinNode d = new MinNode(asList(k, l, m));

        MaxNode a = new MaxNode(asList(b, c, d));

        assertThat(a.getRecursiveValue(Integer.MIN_VALUE, Integer.MAX_VALUE), is(3));
        verifyZeroInteractions(i, j);
    }

    // min node has max children
    // max node has min children
}
