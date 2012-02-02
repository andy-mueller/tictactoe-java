package com.crudetech.tictactoe.game;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.crudetech.query.Query.from;
import static com.crudetech.tictactoe.game.GridCells.markIsEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

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

        List<Grid.Cell> crossedCells = from(otherPlayer.getLastGrid().getCells()).where(markIsEqualTo(Grid.Mark.Cross)).toList();

        assertThat(crossedCells, hasSize(1));
    }

    @Test
    public void naivePlayerMarksNextUnMarkedFieldOnSecondMove() {

        game.startWithPlayer(naivePlayer, Grid.Mark.Cross);
        game.addMark(otherPlayer, Grid.Row.First, Grid.Column.Second);

        List<Grid.Cell> crossedCells = from(otherPlayer.getLastGrid().getCells()).where(markIsEqualTo(Grid.Mark.Cross)).toList();
        List<Grid.Cell> noughtCells= from(otherPlayer.getLastGrid().getCells()).where(markIsEqualTo(Grid.Mark.Nought)).toList();

        assertThat(crossedCells, hasSize(2));
        assertThat(noughtCells, hasSize(1));
    }
}
