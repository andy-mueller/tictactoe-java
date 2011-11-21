package com.crudetech.tictactoe.game;

import com.crudetech.collections.Pair;
import com.crudetech.tictactoe.game.GameTree.Node;
import com.crudetech.tictactoe.game.GameTree.Player;
import org.junit.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyZeroInteractions;

public class GameTreeTest {
    @Test
    public void leafNodeGivesScore() {
        GameTree.Node<Void> node = createLeafNode(42, "node");
        assertThat(node.getValue(), is(42));
    }


    @Test
    public void maxNodeGivesMaximizedScoreOfChildren() {
        final Node<Void> a = createLeafNode(42, "a");
        final Node<Void> b = createLeafNode(12, "b");
        final Node<Void> c = createLeafNode(-4, "c");

        Node<Void> maxNode = createNode(a, b, c);
        GameTree<Void> gameTree = new GameTree<>(maxNode);
        Pair<Integer, Node<Void>> value = gameTree.alphaBeta(Player.Max);

        assertThat(value.getFirst(), is(42));
        assertThat(value.getSecond(), is(a));
    }


    @SafeVarargs
    final Node<Void> createNode(final Node<Void>... nodes) {
        return createNode("<UnNamed>", nodes);
    }

    @SafeVarargs
    final Node<Void> createNode(final String name, final Node<Void>... nodes) {
        return createNode(name, -42, nodes);
    }

    @SafeVarargs
    final Node<Void> createNode(final String name, final int value, final Node<Void>... nodes) {
        return new Node<Void>() {
            @Override
            public boolean hasFinished() {
                return false;
            }

            @Override
            public int getValue() {
                return value;
            }

            @Override
            public Iterable<? extends Node<Void>> getChildren() {
                return asList(nodes);
            }

            @Override
            public Void getGameState() {
                return null;
            }

            @Override
            public String toString() {
                return "ANode{" +
                        "name=" + name +
                        '}';
            }
        };
    }

    private Node<Void> createLeafNode(final int value, final String name) {
        return new Node<Void>() {
            @Override
            public boolean hasFinished() {
                return true;
            }

            @Override
            public int getValue() {
                return value;
            }

            @SuppressWarnings("unchecked")
            @Override
            public Iterable<Node<Void>> getChildren() {
                return emptyList();
            }

            @Override
            public Void getGameState() {
                return null;
            }

            @Override
            public String toString() {
                return "LeafNode{" +
                        "name=" + name +
                        ",value=" + value +
                        '}';
            }
        };
    }


    @Test
    public void minNodeGivesMaximizedScoreOfChildren() {
        final Node<Void> a = createLeafNode(42, "a");
        final Node<Void> b = createLeafNode(12, "b");
        final Node<Void> c = createLeafNode(-4, "c");

        Node<Void> minNode = createNode(a, b, c);
        GameTree<Void> gameTree = new GameTree<>(minNode);

        Pair<Integer, Node<Void>> recursiveValue = gameTree.alphaBeta(Player.Min);
        assertThat(recursiveValue.getFirst(), is(-4));
        assertThat(recursiveValue.getSecond(), is(c));
    }

    @Test
    public void threeLevelsWithMaxInRoot() {
        Node<Void> e = createLeafNode(9, "e");
        Node<Void> f = createLeafNode(-6, "f");
        Node<Void> g = createLeafNode(0, "g");
        Node<Void> h = createLeafNode(0, "h");
        Node<Void> i = createLeafNode(-2, "i");
        Node<Void> j = createLeafNode(-4, "j");
        Node<Void> k = createLeafNode(-3, "k");

        Node<Void> b = createNode(e, f, g);
        Node<Void> c = createNode(h, i);
        Node<Void> d = createNode(j, k);

        Node<Void> a = createNode(b, c, d);
        GameTree<Void> gameTree = new GameTree<>(a);


        Pair<Integer, Node<Void>> value = gameTree.alphaBeta(Player.Max);
        assertThat(value.getFirst(), is(-2));
        assertThat(value.getSecond(), is(c));
        assertThat(value, is(new Pair<>(-2, c)));
    }

    @Test
    public void threeLevelsWithMinInRoot() {
        Node<Void> e = createLeafNode(9, "e");
        Node<Void> f = createLeafNode(-6, "f");
        Node<Void> g = createLeafNode(0, "g");
        Node<Void> h = createLeafNode(0, "h");
        Node<Void> i = createLeafNode(-2, "i");
        Node<Void> j = createLeafNode(-4, "j");
        Node<Void> k = createLeafNode(-3, "k");

        Node<Void> b = createNode(e, f, g);
        Node<Void> c = createNode(h, i);
        Node<Void> d = createNode(j, k);

        Node<Void> a = createNode(b, c, d);
        GameTree<Void> gameTree = new GameTree<>(a);


        Pair<Integer, Node<Void>> nodePair = gameTree.alphaBeta(Player.Min);
        assertThat(nodePair, is(new Pair<>(-3, d)));
    }

    @Test
    public void alphaBetaCutOff() throws Exception {
        Node<Void> e = createLeafNode(3, "e");
        Node<Void> f = createLeafNode(12, "f");
        Node<Void> g = createLeafNode(8, "g");
        Node<Void> h = createLeafNode(2, "h");
        Node<Void> i = spy(createLeafNode(4, "i"));
        Node<Void> j = spy(createLeafNode(6, "j"));
        Node<Void> k = createLeafNode(14, "k");
        Node<Void> l = createLeafNode(5, "l");
        Node<Void> m = createLeafNode(2, "m");

        Node<Void> b = createNode(e, f, g);
        Node<Void> c = createNode(h, i, j);
        Node<Void> d = createNode(k, l, m);

        Node<Void> a = createNode(b, c, d);//max
        GameTree<Void> gameTree = new GameTree<>(a);


        Pair<Integer, Node<Void>> nodePair = gameTree.alphaBeta(Player.Max);
        assertThat(nodePair, is(new Pair<>(3, b)));
        verifyZeroInteractions(i, j);
    }

    @Test
    public void levelCutOff() throws Exception {
        Node<Void> e = createLeafNode(3, "e");
        Node<Void> f = createLeafNode(12, "f");
        Node<Void> g = createLeafNode(8, "g");

        Node<Void> n = createLeafNode(0, "n");
        Node<Void> o = createLeafNode(2, "o");
        Node<Void> h = createNode("h", 4, n, o);
//        Node<Void> h = createLeafNode(4, "h");

        Node<Void> i = createLeafNode(4, "i");
        Node<Void> j = createLeafNode(6, "j");
        Node<Void> k = createLeafNode(14, "k");
        Node<Void> l = createLeafNode(5, "l");
        Node<Void> m = createLeafNode(2, "m");

        Node<Void> b = createNode("b", e, f, g);
        Node<Void> c = createNode("c", h, i, j);
        Node<Void> d = createNode("e", k, l, m);

        Node<Void> a = createNode("a", b, c, d);//max
        GameTree<Void> gameTree = new GameTree<>(a);


        Pair<Integer, Node<Void>> nodePair = gameTree.alphaBeta(Player.Max, 2);
        assertThat(nodePair, is(new Pair<>(4, c)));
//        verifyZeroInteractions(i, j);
    }

    // min Node<Void> has max children
    // max Node<Void> has min children
}
