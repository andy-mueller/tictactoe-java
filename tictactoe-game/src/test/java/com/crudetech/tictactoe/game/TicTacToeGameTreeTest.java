package com.crudetech.tictactoe.game;

import com.crudetech.functional.UnaryFunction;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.crudetech.lang.Enums.iterableOf;
import static com.crudetech.matcher.RangeIsEquivalent.equivalentTo;
import static com.crudetech.matcher.ThrowsException.doesThrow;
import static com.crudetech.query.Query.from;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class TicTacToeGameTreeTest {
    private LinearRandomAccessGrid grid;
    private TicTacToeGameTree gameTree;

    @Before
    public void setUp() throws Exception {
        grid = new LinearRandomAccessGrid();
        gameTree = new TicTacToeGameTree(grid, Grid.Mark.Cross, GameTree.Player.Max);
    }

    @Test
    public void ctorSetsGameStateIntoRoot() {
        assertThat(gameTree.getGameTree().getRoot().getGameState(), is((Grid) grid));
    }

    @Test
    public void nodeHoldsGame() {
        TicTacToeGameTree.Node node = new TicTacToeGameTree.Node(grid, Grid.Mark.Nought);

        assertThat(grid, is(node.getGameState()));
    }

    @Test
    public void nodeChildrenAreAllPossibleNextMovePermutations() {
        TicTacToeGameTree.Node node = new TicTacToeGameTree.Node(grid, Grid.Mark.Cross);

        Iterable<Grid> childStates = from(node.getChildren()).select(toGrid());

        Iterable<Grid> expectedStates = allSingleMarkGridPermutationsOfEmptyGame(Grid.Mark.Cross);

        assertThat(childStates, is(equivalentTo(expectedStates)));
    }

    @Test
    public void nodeChildrenAreProperlyMarked() {
        LinearRandomAccessGrid grid = LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.Cross,
                Grid.Mark.Nought, Grid.Mark.Cross, Grid.Mark.Nought);
        TicTacToeGameTree.Node node = new TicTacToeGameTree.Node(grid, Grid.Mark.Cross);

        Iterable<TicTacToeGameTree.Node> children = node.getChildren();

        Iterable<Grid> actualGrids = from(children).select(toGrid());

        Iterable<Grid> expected = asList(
                (Grid)LinearRandomAccessGrid.of(
                        Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Cross,
                        Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.Cross,
                        Grid.Mark.Nought, Grid.Mark.Cross, Grid.Mark.Nought)
        );

        assertThat(actualGrids, is(equivalentTo(expected)));
    }


    private Iterable<Grid> allSingleMarkGridPermutationsOfEmptyGame(Grid.Mark mark) {
        List<Grid> allSingleMarkGridPermutations = new ArrayList<>();

        for (Grid.Row row : iterableOf(Grid.Row.class)) {
            for (Grid.Column col : iterableOf(Grid.Column.class)) {
                LinearRandomAccessGrid permutation = new LinearRandomAccessGrid(this.grid);
                permutation.setAt(Grid.Location.of(row, col), mark);
                allSingleMarkGridPermutations.add(permutation);
            }
        }

        return allSingleMarkGridPermutations;
    }

    private UnaryFunction<TicTacToeGameTree.Node, Grid> toGrid() {
        return new UnaryFunction<TicTacToeGameTree.Node, Grid>() {
            @Override
            public Grid execute(TicTacToeGameTree.Node node) {
                return node.getGameState();
            }
        };
    }

    @Test
    public void nodeChildrenAreAllPossibleNextMovePermutationsForOtherMark() {
        TicTacToeGameTree.Node node = new TicTacToeGameTree.Node(grid, Grid.Mark.Cross);

        Iterable<Grid> childStates = from(node.getChildren()).select(toGrid());

        Iterable<Grid> expectedStates = allSingleMarkGridPermutationsOfEmptyGame(Grid.Mark.Cross);

        assertThat(childStates, is(equivalentTo(expectedStates)));
    }

    @Test
    public void winOfStartPlayerGivesValueOne() {
        grid = LinearRandomAccessGrid.of(
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None);

        TicTacToeGameTree.Node node = new TicTacToeGameTree.Node(grid, Grid.Mark.Nought);

        Iterable<Grid> childStates = from(node.getChildren()).select(toGrid());

        Iterable<Grid> expectedStates = allSingleMarkGridPermutationsOfEmptyGame(Grid.Mark.Nought);

        assertThat(childStates, is(equivalentTo(expectedStates)));
    }

    @Test
    public void winOfStartPlayerResultsInNodeValueOne() {
        LinearRandomAccessGrid playerOneWins = LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Cross,
                Grid.Mark.None, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought);
        TicTacToeGameTree.Node node = new TicTacToeGameTree.Node(playerOneWins, Grid.Mark.Cross);

        assertThat(node.getValue(), is(1));
    }

    @Test
    public void winOfNonStartingPlayerResultsInNodeValueMinusOne() {
        LinearRandomAccessGrid playerOneWins = LinearRandomAccessGrid.of(
                Grid.Mark.Nought, Grid.Mark.Cross, Grid.Mark.Cross,
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought);
        TicTacToeGameTree.Node node = new TicTacToeGameTree.Node(playerOneWins, Grid.Mark.Nought);

        assertThat(node.getValue(), is(-1));
    }

    @Test
    public void tieResultsInNodeValueZero() {
        LinearRandomAccessGrid tie = LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Nought,
                Grid.Mark.Nought, Grid.Mark.Cross, Grid.Mark.Cross,
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.Nought);
        TicTacToeGameTree.Node node = new TicTacToeGameTree.Node(tie, Grid.Mark.Nought);

        assertThat(node.getValue(), is(0));
    }

    @Test
    public void inOpenGameValueResultsIntoException() {
        LinearRandomAccessGrid open = LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.Nought,
                Grid.Mark.Nought, Grid.Mark.Cross, Grid.Mark.Cross,
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.None);
        final TicTacToeGameTree.Node node = new TicTacToeGameTree.Node(open, Grid.Mark.Nought);

        Runnable getValueOnOpenGame = new Runnable() {
            @Override
            public void run() {
                node.getValue();
            }
        };

        assertThat(getValueOnOpenGame, doesThrow(IllegalStateException.class));
    }

    @Test
    public void hasFinishedIsTrueOnTie() {
        LinearRandomAccessGrid tie = LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Nought,
                Grid.Mark.Nought, Grid.Mark.Cross, Grid.Mark.Cross,
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.Nought);
        TicTacToeGameTree.Node node = new TicTacToeGameTree.Node(tie, Grid.Mark.Nought);

        assertThat(node.hasFinished(), is(true));
    }

    @Test
    public void hasFinishedIsTrueForStartPlayerWin() {
        LinearRandomAccessGrid playerOneWins = LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Cross,
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought);
        TicTacToeGameTree.Node node = new TicTacToeGameTree.Node(playerOneWins, Grid.Mark.Cross);

        assertThat(node.hasFinished(), is(true));
    }

    @Test
    public void hasFinishedIsTrueForSecondPlayerWin() {
        LinearRandomAccessGrid playerTwoWins = LinearRandomAccessGrid.of(
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.Nought,
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought);
        TicTacToeGameTree.Node node = new TicTacToeGameTree.Node(playerTwoWins, Grid.Mark.Cross);

        assertThat(node.hasFinished(), is(true));
    }

    @Test
    public void evaluatePartialGame() {
        LinearRandomAccessGrid open = LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.Nought,
                Grid.Mark.Nought, Grid.Mark.Cross, Grid.Mark.Cross,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought);
        TicTacToeGameTree gameTree = new TicTacToeGameTree(open, Grid.Mark.Cross, GameTree.Player.Max);

        Grid nextMove = gameTree.bestNextMove();

        Grid expectedNextMove = LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Nought,
                Grid.Mark.Nought, Grid.Mark.Cross, Grid.Mark.Cross,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought);
        assertThat(nextMove, is(expectedNextMove));
    }

    @Test
    public void blocksIfNecessary() {
        LinearRandomAccessGrid grid = LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought);
        TicTacToeGameTree gameTree = new TicTacToeGameTree(grid, Grid.Mark.Nought, GameTree.Player.Min);

        Grid nextMove = gameTree.bestNextMove();

        Grid expectedNextMove = LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Nought,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought);
        assertThat(nextMove, is(expectedNextMove));
    }

    @Test
    public void blocksIfNecessary2() {
        LinearRandomAccessGrid grid = LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.Cross,
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.Nought);
        TicTacToeGameTree gameTree = new TicTacToeGameTree(grid, Grid.Mark.Nought, GameTree.Player.Min);

        Grid nextMove = gameTree.bestNextMove();

        Grid expectedNextMove = LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Nought,
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.Cross,
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.Nought);
        assertThat(nextMove, is(expectedNextMove));
    }

    @Test
    public void blocksIfNecessary3() {
        LinearRandomAccessGrid grid = LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.Cross,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought);
        TicTacToeGameTree gameTree = new TicTacToeGameTree(grid, Grid.Mark.Nought, GameTree.Player.Min);

        Grid nextMove = gameTree.bestNextMove();

        Grid expectedNextMove = LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.Cross,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought);
        assertThat(nextMove, is(expectedNextMove));
    }
    @Test
    public void blocksIfNecessary4() {
        LinearRandomAccessGrid grid = LinearRandomAccessGrid.of(
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.Cross,
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Cross, Grid.Mark.None);
        TicTacToeGameTree gameTree = new TicTacToeGameTree(grid, Grid.Mark.Cross, GameTree.Player.Max);

        Grid nextMove = gameTree.bestNextMove();

        Grid expectedNextMove = LinearRandomAccessGrid.of(
                Grid.Mark.Nought, Grid.Mark.Nought, Grid.Mark.Cross,
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.Nought, Grid.Mark.Cross, Grid.Mark.None);
        assertThat(nextMove, is(expectedNextMove));
    }

    @Test
    public void winsIfPossible() {
        LinearRandomAccessGrid grid = LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.Nought, Grid.Mark.Nought);
        TicTacToeGameTree gameTree = new TicTacToeGameTree(grid, Grid.Mark.Cross, GameTree.Player.Max);

        Grid nextMove = gameTree.bestNextMove();

        Grid expectedNextMove = LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Cross,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.Nought, Grid.Mark.Nought);
        assertThat(nextMove, is(expectedNextMove));
    }
}
