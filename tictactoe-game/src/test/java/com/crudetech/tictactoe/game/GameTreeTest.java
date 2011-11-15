package com.crudetech.tictactoe.game;

import com.crudetech.collections.Pair;
import org.junit.Test;

import java.util.Comparator;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyZeroInteractions;

public class GameTreeTest {

    interface Node {
        public Pair<Integer, Node> getRecursiveValue(int alpha, int beta);
    }

    abstract class AlphaBetaNode implements Node {
        abstract Iterable<? extends Node> getChildren();

        boolean isBetaCutOff(int beta, Integer alpha) {
            return beta <= alpha;
        }

        abstract boolean compareByFirst(Pair<Integer, Node> pair, Pair<Integer, Node> currentValue);
    }

    abstract class MinNode extends AlphaBetaNode {
        @Override
        public Pair<Integer, Node> getRecursiveValue(int alpha, int beta) {
            Pair<Integer, Node> betaPair = new Pair<>(beta, null);
            for (Node node : getChildren()) {
                Pair<Integer, Node> value = node.getRecursiveValue(alpha, betaPair.getFirst());
                betaPair = minimum(betaPair, value, node);
                if (isBetaCutOff(betaPair.getFirst(), alpha)) {
                    break;
                }
            }
            return betaPair;
        }

        boolean compareByFirst(Pair<Integer, Node> pair, Pair<Integer, Node> currentValue) {
            return currentValue.getFirst() <= pair.getFirst();
        }

        private Pair<Integer, Node> minimum(Pair<Integer, Node> pair, Pair<Integer, Node> currentValue, Node currentNode) {
            if (compareByFirst(pair, currentValue)) {
                return new Pair<>(currentValue.getFirst(), currentNode);
            }
            return pair;
        }

    }

    abstract class MaxNode extends AlphaBetaNode {
        @Override
        public Pair<Integer, Node> getRecursiveValue(int alpha, int beta) {
            Pair<Integer, Node> alphaPair = new Pair<>(alpha, null);
            for (Node node : getChildren()) {
                Pair<Integer, Node> value = node.getRecursiveValue(alphaPair.getFirst(), beta);
                alphaPair = maximum(alphaPair, value, node);
                if (isBetaCutOff(beta, alphaPair.getFirst())) {
                    break;
                }
            }
            return alphaPair;
        }

        @Override
        boolean compareByFirst(Pair<Integer, Node> pair, Pair<Integer, Node> currentValue) {
            return currentValue.getFirst() >= pair.getFirst();
        }

        private Pair<Integer, Node> maximum(Pair<Integer, Node> pair, Pair<Integer, Node> currentValue, Node currentNode) {
            if (compareByFirst(pair, currentValue)) {
                return new Pair<>(currentValue.getFirst(), currentNode);
            }
            return pair;
        }
    }

    static Comparator<? super Pair<Integer, Node>> compareByFirst() {
        return new Comparator<Pair<Integer, Node>>() {
            @Override
            public int compare(Pair<Integer, Node> lhs, Pair<Integer, Node> rhs) {
                return lhs.getFirst().compareTo(rhs.getFirst());
            }
        };
    }

    abstract class LeafNode implements Node {

        @Override
        public Pair<Integer, Node> getRecursiveValue(int alpha, int beta) {
            return new Pair<>(getValue(), (Node) this);
        }

        protected abstract int getValue();
    }

    @Test
    public void leafNodeGivesScore() {
        Node node = createLeafNode(42, "node");

        assertThat(alphaBeta(node), is(42));
    }


    @Test
    public void maxNodeGivesMaximizedScoreOfChildren() {
        final Node a = createLeafNode(42, "a");
        final Node b = createLeafNode(12, "b");
        final Node c = createLeafNode(-4, "c");

        MaxNode maxNode = createMaxNode(a, b, c);

        Pair<Integer, Node> value = maxNode.getRecursiveValue(Integer.MIN_VALUE, Integer.MAX_VALUE);
        assertThat(value.getFirst(), is(42));
        assertThat(value.getSecond(), is(a));
    }

    private Node createLeafNode(int value, final String name) {
        class ValueLeafNode extends LeafNode {
            private final int value;

            ValueLeafNode(int value) {
                this.value = value;
            }

            @Override
            protected int getValue() {
                return value;
            }

            @Override
            public String toString() {
                return "ValueLeafNode{" +
                        "name=" + name +
                        ",value=" + value +
                        '}';
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
        final Node a = createLeafNode(42, "a");
        final Node b = createLeafNode(12, "b");
        final Node c = createLeafNode(-4, "c");

        MinNode minNode = createMinNode(a, b, c);

        Pair<Integer, Node> recursiveValue = minNode.getRecursiveValue(Integer.MIN_VALUE, Integer.MAX_VALUE);
        assertThat(recursiveValue.getFirst(), is(-4));
        assertThat(recursiveValue.getSecond(), is(c));
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
        Node e = createLeafNode(9, "e");
        Node f = createLeafNode(-6, "f");
        Node g = createLeafNode(0, "g");
        Node h = createLeafNode(0, "h");
        Node i = createLeafNode(-2, "i");
        Node j = createLeafNode(-4, "j");
        Node k = createLeafNode(-3, "k");

        MinNode b = createMinNode(e, f, g);
        MinNode c = createMinNode(h, i);
        MinNode d = createMinNode(j, k);

        MaxNode a = createMaxNode(b, c, d);

        Pair<Integer, Node> value = a.getRecursiveValue(Integer.MIN_VALUE, Integer.MAX_VALUE);
        assertThat(value.getFirst(), is(-2));
        assertThat(value.getSecond(), is((Node) c));
        assertThat(value, is(new Pair<Integer, Node>(-2, c)));
    }

    @Test
    public void threeLevelsWithMinInRoot() {
        Node e = createLeafNode(9, "e");
        Node f = createLeafNode(-6, "f");
        Node g = createLeafNode(0, "g");
        Node h = createLeafNode(0, "h");
        Node i = createLeafNode(-2, "i");
        Node j = createLeafNode(-4, "j");
        Node k = createLeafNode(-3, "k");

        MaxNode b = createMaxNode(e, f, g);
        MaxNode c = createMaxNode(h, i);
        MaxNode d = createMaxNode(j, k);

        MinNode a = createMinNode(b, c, d);

        assertThat(alphaBeta(a), is(-3));
    }

    @Test
    public void alphaBetaCutOff() throws Exception {
        Node e = createLeafNode(3, "e");
        Node f = createLeafNode(12, "f");
        Node g = createLeafNode(8, "g");
        Node h = createLeafNode(2, "h");
        Node i = spy(createLeafNode(4, "i"));
        Node j = spy(createLeafNode(6, "j"));
        Node k = createLeafNode(14, "k");
        Node l = createLeafNode(5, "l");
        Node m = createLeafNode(2, "m");

        MinNode b = createMinNode(e, f, g);
        MinNode c = createMinNode(h, i, j);
        MinNode d = createMinNode(k, l, m);

        MaxNode a = createMaxNode(b, c, d);

        assertThat(alphaBeta(a), is(3));
        verifyZeroInteractions(i, j);
    }

    private int alphaBeta(Node node) {
        return node.getRecursiveValue(Integer.MIN_VALUE, Integer.MAX_VALUE).getFirst();
    }

    // min node has max children
    // max node has min children
}
