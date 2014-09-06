package com.crudetech.tictactoe.game;

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
        assertThat(node.getHeuristicValue(), is(42));
    }


    @Test
    public void maxNodeGivesMaximizedScoreOfChildren() {
        final Node<Void> a = createLeafNode(42, "a");
        final Node<Void> b = createLeafNode(12, "b");
        final Node<Void> c = createLeafNode(-4, "c");

        Node<Void> maxNode = createNode(a, b, c);
        GameTree<Void> gameTree = new GameTree<>(maxNode);
        GameTree.AlphaBetaValue<Void> value = gameTree.alphaBeta(Player.Max);

        assertThat(value.value, is(42));
        assertThat(value.node, is(a));
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
            public int getHeuristicValue() {
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
            public int getHeuristicValue() {
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

        GameTree.AlphaBetaValue<Void> recursiveValue = gameTree.alphaBeta(Player.Min);
        assertThat(recursiveValue.value, is(-4));
        assertThat(recursiveValue.node, is(c));
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


        GameTree.AlphaBetaValue<Void> value = gameTree.alphaBeta(Player.Max);
        assertThat(value.value, is(-2));
        assertThat(value.node, is(c));
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


        GameTree.AlphaBetaValue<Void> nodePair = gameTree.alphaBeta(Player.Min);
        assertThat(nodePair.value, is(-3));
        assertThat(nodePair.node, is(d));
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


        GameTree.AlphaBetaValue<Void> nodePair = gameTree.alphaBeta(Player.Max);
        assertThat(nodePair.value, is(3));
        assertThat(nodePair.node, is(b));
        verifyZeroInteractions(i, j);
    }

    @Test
    public void levelCutOff() throws Exception {
        Node<Void> e = createLeafNode(3, "e");
        Node<Void> f = createLeafNode(12, "f");
        Node<Void> g = createLeafNode(8, "g");

        Node<Void> n = spy(createLeafNode(0, "n"));
        Node<Void> o = spy(createLeafNode(2, "o"));

        Node<Void> h = createNode("h", 4, n, o);

        Node<Void> i = createLeafNode(4, "i");
        Node<Void> j = createLeafNode(6, "j");
        Node<Void> k = createLeafNode(14, "k");
        Node<Void> l = createLeafNode(5, "l");
        Node<Void> m = createLeafNode(2, "m");

        Node<Void> b = createNode("b", e, f, g);
        Node<Void> c = createNode("c", h, i, j);
        Node<Void> d = createNode("d", k, l, m);

        Node<Void> a = createNode("a", b, c, d);//max
        GameTree<Void> gameTree = new GameTree<>(a);


        GameTree.AlphaBetaValue<Void> nodePair = gameTree.alphaBeta(Player.Max, 2);
        assertThat(nodePair.value, is(4));
        assertThat(nodePair.node, is(c));
        verifyZeroInteractions(n, o);
    }

    @Test
    public void anotherAlphaBetaCutOff() throws Exception {
        Node<Void> t = createLeafNode(5, "t");
        Node<Void> u = createLeafNode(6, "u");
        Node<Void> v = createLeafNode(7, "v");
        Node<Void> w = createLeafNode(4, "w");
        Node<Void> x = spy(createLeafNode(5, "x"));

        Node<Void> y = createLeafNode(3, "y");
        Node<Void> z = createLeafNode(6, "z");
        Node<Void> aa = createLeafNode(6, "aa");
        Node<Void> bb = spy(createLeafNode(9, "bb"));
        Node<Void> cc = createLeafNode(7, "c");

        Node<Void> dd = createLeafNode(5, "dd");

        Node<Void> ee = spy(createLeafNode(9, "ee"));
        Node<Void> ff = spy(createLeafNode(8, "ff"));
        Node<Void> gg = spy(createLeafNode(6, "gg"));

        Node<Void> k = createNode(t, u);
        Node<Void> l = createNode(v, w, x);
        Node<Void> m = createNode(y);
        Node<Void> n = createNode(z);
        Node<Void> o = createNode(aa, bb);
        Node<Void> p = createNode(cc);
        Node<Void> q = createNode(dd);
        Node<Void> r = spy(createNode(ee, ff));
        Node<Void> s = spy(createNode(gg));

        Node<Void> e = createNode(k, l);
        Node<Void> f = createNode(m);
        Node<Void> g = createNode(n, o);
        Node<Void> h = createNode(p);
        Node<Void> i = createNode(q);
        Node<Void> j = spy(createNode(r, s));

        Node<Void> b = createNode(e, f);
        Node<Void> c = createNode(g, h);
        Node<Void> d = createNode(i, j);

        Node<Void> a = createNode(b, c, d);
        GameTree<Void> gameTree = new GameTree<>(a);


        GameTree.AlphaBetaValue<Void> nodePair = gameTree.alphaBeta(Player.Max);

        assertThat(nodePair.value, is(6));
        assertThat(nodePair.node, is(c));
        verifyZeroInteractions(x, bb, ee, ff, gg, r, s, j);
    }
}
