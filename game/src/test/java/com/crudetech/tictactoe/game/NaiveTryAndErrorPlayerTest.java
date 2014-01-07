package com.crudetech.tictactoe.game;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.crudetech.matcher.ThrowsException.doesThrow;
import static com.crudetech.query.Query.from;
import static com.crudetech.tictactoe.game.GridCells.markIsEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
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
    public void naivePlayerMarksNextAnyMarkedField() {
        game.startWithPlayer(naivePlayer, Grid.Mark.Cross);

        List<Grid.Cell> crossedCells = from(otherPlayer.getLastGrid().getCells()).where(markIsEqualTo(Grid.Mark.Cross)).toList();

        assertThat(crossedCells, hasSize(1));
    }

    @Test
    public void naivePlayerMarksNextUnMarkedFieldOnSecondMove() {
        game.startWithPlayer(naivePlayer, Grid.Mark.Cross);

        game.makeMove(otherPlayer, nextFreeLocation());

        List<Grid.Cell> crossedCells = from(otherPlayer.getLastGrid().getCells()).where(markIsEqualTo(Grid.Mark.Cross)).toList();
        List<Grid.Cell> noughtCells = from(otherPlayer.getLastGrid().getCells()).where(markIsEqualTo(Grid.Mark.Nought)).toList();

        assertThat(crossedCells, hasSize(2));
        assertThat(noughtCells, hasSize(1));
    }

    private Grid.Location nextFreeLocation() {
        for (Grid.Cell cell : otherPlayer.getLastGrid().getCells()) {
            if (cell.getMark().equals(Grid.Mark.None)) {
                return cell.getLocation();
            }
        }
        throw new RuntimeException();
    }

    @Test
    public void naivePlayerThrowsWhenGameGridIsFull() {
        final Grid fullGrid = LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Cross,
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Cross,
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Cross
        );

        Runnable yourTurnWithFullGrid = new Runnable() {
            @Override
            public void run() {
                naivePlayer.yourTurn(fullGrid);
            }
        };

        assertThat(yourTurnWithFullGrid, doesThrow(IllegalStateException.class));
    }

    @Test
    public void naivePlayerSetsNextMark() {
        TicTacToeGame gameSpy = mock(TicTacToeGame.class);
        NaiveTryAndErrorPlayer naivePlayer = new NaiveTryAndErrorPlayer();
        naivePlayer.setGame(gameSpy);
        naivePlayer.yourTurn(LinearRandomAccessGrid.empty());

        verify(gameSpy).makeMove(any(Player.class), any(Grid.Location.class));
    }
}
