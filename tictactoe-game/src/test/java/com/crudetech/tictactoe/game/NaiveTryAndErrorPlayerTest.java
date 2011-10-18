package com.crudetech.tictactoe.game;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class NaiveTryAndErrorPlayerTest {

    private NaiveTryAndErrorPlayer naivePlayer;
    private PlayerStub otherPlayer;
    private TicTacToeGame game;

    @Before
    public void setUp() throws Exception {
        naivePlayer = new NaiveTryAndErrorPlayer();
        otherPlayer = new PlayerStub();
        game = new TicTacToeGame(naivePlayer, otherPlayer);
        naivePlayer.setGame(game);
    }

    @Test
    public void naivePlayerMarksNextUnMarkedField() {

        game.startWithPlayer(naivePlayer, Grid.Mark.Cross);

        Grid expectedGrid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
        });

        assertThat(otherPlayer.getLastGrid(), is(expectedGrid));
    }

    @Test
    public void naivePlayerMarksNextUnMarkedFieldOnSecondMove() {

        game.startWithPlayer(naivePlayer, Grid.Mark.Cross);
        game.addMark(otherPlayer, Grid.Row.First, Grid.Column.Second);

        Grid expectedGrid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.Cross,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
        });

        assertThat(otherPlayer.getLastGrid(), is(expectedGrid));
    }

    @Test
    public void naivePlayerCanPlayAgainstEachOther() {
        NaiveTryAndErrorPlayer aNaivePlayer = spy(new NaiveTryAndErrorPlayer());
        NaiveTryAndErrorPlayer otherNaivePlayer = new NaiveTryAndErrorPlayer();
        TicTacToeGame aGame = new TicTacToeGame(aNaivePlayer, otherNaivePlayer);
        otherNaivePlayer.setGame(aGame);
        aNaivePlayer.setGame(aGame);

        aGame.startWithPlayer(aNaivePlayer, Grid.Mark.Cross);

        Grid expectedGrid = LinearRandomAccessGrid.of(new Grid.Mark[]{
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.Cross,
                Grid.Mark.Nought, Grid.Mark.Cross, Grid.Mark.Nought,
                Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.None,
        });

        Grid.Triple diagonalTriple = new Grid.Triple(Grid.Mark.Cross,
                Grid.Location.of(Grid.Row.Third, Grid.Column.First),
                Grid.Location.of(Grid.Row.Second, Grid.Column.Second),
                Grid.Location.of(Grid.Row.First, Grid.Column.Third));


        verify(aNaivePlayer).youWin(expectedGrid, diagonalTriple);
    }

}
