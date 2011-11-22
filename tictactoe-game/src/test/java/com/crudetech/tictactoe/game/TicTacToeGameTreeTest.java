package com.crudetech.tictactoe.game;

import com.crudetech.functional.UnaryFunction;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.crudetech.lang.Enums.iterableOf;
import static com.crudetech.matcher.RangeIsEquivalent.equivalentTo;
import static com.crudetech.query.Query.from;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class TicTacToeGameTreeTest {
    private LinearRandomAccessGrid grid;
    private TicTacToeGameTree gameTree;

    @Before
    public void setUp() throws Exception {
        grid = new LinearRandomAccessGrid();
        gameTree = new TicTacToeGameTree(grid, Grid.Mark.Cross);
    }

    @Test
    public void ctorSetsGameStateIntoRoot(){
        assertThat(gameTree.getGameTree().getRoot().getGameState(), is((Grid)grid));
    }
    @Test
    public void nodeHoldsGame(){
        TicTacToeGameTree.Node node= new TicTacToeGameTree.Node(grid, Grid.Mark.Nought);

        assertThat(grid, is(node.getGameState()));
    }

    @Test
    public void nodeChildrenAreAllPossibleNextMovePermutations(){
        TicTacToeGameTree.Node node= new TicTacToeGameTree.Node(grid, Grid.Mark.Nought);

        Iterable<Grid> childStates= from(node.getChildren()).select(toGrid());

        Iterable<Grid> expectedStates = allSingleMarkGridPermutationsOfEmptyGame(Grid.Mark.Cross);

        assertThat(childStates, is(equivalentTo(expectedStates)));
    }

    private Iterable<Grid> allSingleMarkGridPermutationsOfEmptyGame(Grid.Mark mark) {
        List<Grid> allSingleMarkGridPermutations = new ArrayList<>();

        for(Grid.Row row : iterableOf(Grid.Row.class)){
            for(Grid.Column col : iterableOf(Grid.Column.class)){
                LinearRandomAccessGrid permutation = new LinearRandomAccessGrid(this.grid);
                permutation.setAt(Grid.Location.of(row, col), mark);
                allSingleMarkGridPermutations.add(permutation);
            }
        }

        return allSingleMarkGridPermutations;
    }

    private UnaryFunction<TicTacToeGameTree.Node, Grid> toGrid() {
        return new UnaryFunction<TicTacToeGameTree.Node, Grid> (){
            @Override
            public Grid execute(TicTacToeGameTree.Node node) {
                return node.getGameState();
            }
        };
    }
    @Test
    public void nodeChildrenAreAllPossibleNextMovePermutationsForOtherMark(){
        TicTacToeGameTree.Node node= new TicTacToeGameTree.Node(grid, Grid.Mark.Nought);

        Iterable<Grid> childStates= from(node.getChildren()).select(toGrid());

        Iterable<Grid> expectedStates = allSingleMarkGridPermutationsOfEmptyGame(Grid.Mark.Cross);

        assertThat(childStates, is(equivalentTo(expectedStates)));
    }

    @Test
    public void winOfStartPlayerGivesValueOne(){
        grid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.None,Grid.Mark.None,Grid.Mark.None,
                Grid.Mark.None,Grid.Mark.None,Grid.Mark.None,
                Grid.Mark.None,Grid.Mark.None,Grid.Mark.None,
        });

        TicTacToeGameTree.Node node= new TicTacToeGameTree.Node(grid, Grid.Mark.Cross);

        Iterable<Grid> childStates= from(node.getChildren()).select(toGrid());

        Iterable<Grid> expectedStates = allSingleMarkGridPermutationsOfEmptyGame(Grid.Mark.Nought);

        assertThat(childStates, is(equivalentTo(expectedStates)));
    }
    @Test
    public void winOfStartPlayerResultsInNodeValueOne(){
        LinearRandomAccessGrid playerOneWins = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Cross,
                Grid.Mark.None, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought,
        });
        TicTacToeGameTree.Node node= new TicTacToeGameTree.Node(playerOneWins, Grid.Mark.Cross);

        assertThat(node.getValue(), is(1));
    }
    @Ignore
    @Test
    public void winOfNonStartingPlayerResultsInNodeValueOne(){
        LinearRandomAccessGrid playerOneWins = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.Nought, Grid.Mark.Cross, Grid.Mark.Cross,
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought,
        });
        TicTacToeGameTree.Node node= new TicTacToeGameTree.Node(playerOneWins, Grid.Mark.Nought);

        assertThat(node.getValue(), is(-1));
    }

    // win of start player gives value one
    // win second player gives value -1
    // tie gives value 0
    // open game throws IllegalStateException
    @Ignore
    @Test
    public void evaluatePartialGame(){
    }
    @Ignore
    @Test
    public void evaluateCompleteGame(){
    }



}
