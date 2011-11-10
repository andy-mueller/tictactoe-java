package com.crudetech.tictactoe.game;

import com.crudetech.collections.Iterables;
import com.crudetech.lang.Compare;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GameTreeTest {

    interface Node extends Comparable<Node> {
        int getRecursiveValue();
    }

    abstract class ComparableNode implements Node {
        @Override
        public int compareTo(Node o) {
            return Integer.valueOf(getRecursiveValue()).compareTo(o.getRecursiveValue());
        }
    }

    class MinNode extends ComparableNode {
        private final Iterable<? extends Node> children;

        MinNode(Iterable<? extends Node> children) {
            this.children = children;
        }

        protected Iterable<? extends Node> getChildren() {
            return children;
        }

        @Override
        public int getRecursiveValue() {
            Iterable<? extends Node> children = getChildren();
            Node min = Iterables.firstOf(children);
            for (Node node : getChildren()) {
                min = Compare.min(min, node);
            }
            return min.getRecursiveValue();
        }
    }

    class MaxNode extends ComparableNode {
        private final Iterable<? extends Node> children;

        MaxNode(Iterable<? extends Node> children) {
            this.children = children;
        }

        protected Iterable<? extends Node> getChildren() {
            return children;
        }

        @Override
        public int getRecursiveValue() {
            Iterable<? extends Node> children = getChildren();
            Node max = Iterables.firstOf(children);
            for (Node node : getChildren()) {
                max = Compare.max(max, node);
            }
            return max.getRecursiveValue();
        }
    }

    abstract class LeafNode extends ComparableNode {
    }

    class ValueLeafNode extends LeafNode {
        private final int value;

        ValueLeafNode(int value) {
            this.value = value;
        }

        @Override
        public int getRecursiveValue() {
            return value;
        }
    }

    @Test
    public void leafNodeGivesScore() {
        LeafNode node = new ValueLeafNode(42);

        assertThat(node.getRecursiveValue(), is(42));
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

        assertThat(maxNode.getRecursiveValue(), is(42));
    }

    @Test
    public void minNodeGivesMaximizedScoreOfChildren() {
        final LeafNode a = new ValueLeafNode(42);
        final LeafNode b = new ValueLeafNode(12);
        final LeafNode c = new ValueLeafNode(-4);

        MinNode maxNode = new MinNode(asList(a, b, c));

        assertThat(maxNode.getRecursiveValue(), is(-4));
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

        assertThat(a.getRecursiveValue(), is(-2));
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

        assertThat(a.getRecursiveValue(), is(-3));
    }


//    @Test
//    public void alphaBetaRecursesTree() {
//        GameTree gameTree = new GameTree();
//
//        class LeafNode implements GameTree.Node {
//            private final int value;
//
//            LeafNode(int value) {
//                this.value = value;
//            }
//
//            @Override
//            public Iterable<? extends GameTree.Node> getChildren() {
//                return emptyList();
//            }
//
//            @Override
//            public int getValue() {
//                return value;
//            }
//        }
//
//        LeafNode e = new LeafNode(9);
//        LeafNode f = new LeafNode(-6);
//        LeafNode g = new LeafNode(0);
//        LeafNode h = new LeafNode(0);
//        LeafNode i = new LeafNode(-2);
//        LeafNode j = new LeafNode(-4);
//        LeafNode k = new LeafNode(-3);
//
//        class Node implements GameTree.Node {
//            private final Iterable<? extends GameTree.Node> children;
//
//            Node(Iterable<? extends GameTree.Node> children) {
//                this.children = children;
//            }
//
//            Node(GameTree.Node... children) {
//                this(asList(children));
//            }
//
//            @Override
//            public Iterable<? extends GameTree.Node> getChildren() {
//                return children;
//            }
//
//            @Override
//            public int getValue() {
//                return 42;
//            }
//
//            Node getChildNode(GameTree.PlayerStrategy playerStrategy){
//                return null;
//            }
//        }
//
//        Node b = new Node(e, f, g);
//        Node c = new Node(h, i);
//        Node d = new Node(j, k);
//
//        Node a = new Node(b, c, d);
//
//
//        Object maxPlayer = null;
//        int max = gameTree.alphaBeta(a, 9, Integer.MIN_VALUE, Integer.MAX_VALUE, maxPlayer);
//
//        assertThat(max, is(-2));
//    }
}
