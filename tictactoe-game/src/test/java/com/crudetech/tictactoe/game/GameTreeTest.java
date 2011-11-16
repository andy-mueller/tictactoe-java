package com.crudetech.tictactoe.game;

import com.crudetech.collections.Iterables;
import com.crudetech.collections.Pair;
import com.crudetech.tictactoe.game.GameTree.Node;
import com.crudetech.tictactoe.game.GameTree.Player;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyZeroInteractions;

public class GameTreeTest {
    @Test
    public void leafNodeGivesScore() {
        GameTree.Node node = createLeafNode(42, "node");
        assertThat(node.getValue(), is(42));
    }


    @Test
    public void maxNodeGivesMaximizedScoreOfChildren() {
        final Node a = createLeafNode(42, "a");
        final Node b = createLeafNode(12, "b");
        final Node c = createLeafNode(-4, "c");

        Node maxNode = createNode(a, b, c);
        GameTree gameTree = new GameTree(maxNode);
        Pair<Integer, Node> value = gameTree.alphaBeta(Player.Max);

        assertThat(value.getFirst(), is(42));
        assertThat(value.getSecond(), is(a));
    }

    private Node createNode(final Node... nodes) {
        return new Node() {
            @Override
            public boolean hasFinished() {
                return false;
            }

            @Override
            public int getValue() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Iterable<? extends Node> getChildren() {
                return asList(nodes);
            }

            @Override
            public String toString() {
                return "ANode{" +
                        '}';
            }
        };
    }

    private Node createLeafNode(final int value, final String name) {
        return new Node() {
            @Override
            public boolean hasFinished() {
                return true;
            }

            @Override
            public int getValue() {
                return value;
            }

            @Override
            public Iterable<? extends Node> getChildren() {
                return Iterables.emptyListOf(Node.class);
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
        final Node a = createLeafNode(42, "a");
        final Node b = createLeafNode(12, "b");
        final Node c = createLeafNode(-4, "c");

        Node minNode = createNode(a, b, c);
        GameTree gameTree = new GameTree(minNode);

        Pair<Integer, Node> recursiveValue = gameTree.alphaBeta(Player.Min);
        assertThat(recursiveValue.getFirst(), is(-4));
        assertThat(recursiveValue.getSecond(), is(c));
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

        Node b = createNode(e, f, g);
        Node c = createNode(h, i);
        Node d = createNode(j, k);

        Node a = createNode(b, c, d);
        GameTree gameTree = new GameTree(a);


        Pair<Integer, Node> value = gameTree.alphaBeta(Player.Max);
        assertThat(value.getFirst(), is(-2));
        assertThat(value.getSecond(), is(c));
        assertThat(value, is(new Pair<>(-2, c)));
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

        Node b = createNode(e, f, g);
        Node c = createNode(h, i);
        Node d = createNode(j, k);

        Node a = createNode(b, c, d);
        GameTree gameTree = new GameTree(a);


        Pair<Integer, Node> nodePair = gameTree.alphaBeta(Player.Min);
        assertThat(nodePair, is(new Pair<>(-3, d)));
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

        Node b = createNode(e, f, g);
        Node c = createNode(h, i, j);
        Node d = createNode(k, l, m);

        Node a = createNode(b, c, d);//max
        GameTree gameTree = new GameTree(a);


        Pair<Integer, Node> nodePair = gameTree.alphaBeta(Player.Max);
        assertThat(nodePair, is(new Pair<>(3, b)));
        verifyZeroInteractions(i, j);
    }

//    @Test
    public void levelCutOff() throws Exception {
        Node e = createLeafNode(3, "e");
        Node f = createLeafNode(12, "f");
        Node g = createLeafNode(8, "g");
        Node h = createLeafNode(2, "h");
        Node i = spy(createLeafNode(4, "i"));
        Node j = spy(createLeafNode(6, "j"));
        Node k = createLeafNode(14, "k");
        Node l = createLeafNode(5, "l");
        Node m = createLeafNode(2, "m");

        Node b = createNode(e, f, g);
        Node c = createNode(h, i, j);
        Node d = createNode(k, l, m);

        Node a = createNode(b, c, d);//max
        GameTree gameTree = new GameTree(a);


        Pair<Integer, Node> nodePair = gameTree.alphaBeta(Player.Max);
        assertThat(nodePair, is(new Pair<>(3, b)));
        verifyZeroInteractions(i, j);
    }

    // min node has max children
    // max node has min children
}
