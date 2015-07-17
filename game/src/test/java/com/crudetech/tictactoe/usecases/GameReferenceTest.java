package com.crudetech.tictactoe.usecases;

import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;
import com.crudetech.tictactoe.game.Player;
import com.crudetech.tictactoe.game.TicTacToeGameMother;
import org.junit.Ignore;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class GameReferenceTest {

    private final String startingPlayerId = "__startingPlayerId__";


    @Test
    public void givenPlayerMakesMove_ResultIsPresented() throws Exception {
        Player startPlayer = new GameReference.HumanPlayer();
        Player otherPlayer = mock(Player.class);
        GameReference gameRef = GameReference.builder()
                .withStartPlayer(startPlayer)
                .withStartPlayerMark(Grid.Mark.Cross)
                .withOtherPlayer(otherPlayer)
                .build();

        Grid.Location move = Grid.Location.of(Grid.Row.First, Grid.Column.Second);
        GameReference.Presenter presenter = mock(GameReference.Presenter.class);

        gameRef.makeMove(startingPlayerId, move, presenter);

        Grid expectedGrid = LinearRandomAccessGrid.of(
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None
        );

        verify(presenter).display(expectedGrid);
        verify(presenter, never()).highlight(any(Grid.ThreeInARow.class));
        verify(presenter, never()).finished();
    }

    @Ignore
    @Test
    public void givenMoveResultsInTie_resultIsPresented() throws Exception {
        Player startPlayer = new GameReference.HumanPlayer();
        Player otherPlayer = new SingleMovePlayer(Grid.Location.of(Grid.Row.First, Grid.Column.First));
        GameReference gameRef = new AlmostFinishedGameReferenceBuilder()
                .withStartPlayer( startPlayer)
                .withStartPlayerMark(Grid.Mark.Cross)
                .withOtherPlayer(otherPlayer)
                .build();

        GameReference.Presenter presenter = mock(GameReference.Presenter.class);
        gameRef.makeMove(startingPlayerId, Grid.Location.of(Grid.Row.Third, Grid.Column.Third), presenter);


        Grid expectedGrid = LinearRandomAccessGrid.of(
                Grid.Mark.Nought, Grid.Mark.None, Grid.Mark.Nought,
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Nought,
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.Cross
        );

        verify(presenter).display(expectedGrid);
        verify(presenter, never()).highlight(any(Grid.ThreeInARow.class));
        verify(presenter).finished();
    }

    /**
     * Creates a game that is still open for both players to win or to produce a tie:
     * <pre>
     *    |   | O
     * ---+---+---
     *  X | X | O
     * ---+---+---
     *  X | O |
     * </pre>
     * <p/>
     * It is the first players turn now, using the 'X' mark
     */
    private static class AlmostFinishedGameReferenceBuilder extends GameReference.Builder {
        @Override
        public GameReference build() {

//            TicTacToeGame.builder().withStartingPlayer()
//
//            TicTacToeGame g = newGame(startingPlayer, otherPlayer);
//            g.startWithPlayer(startingPlayer, startPlayerMark);
//            return new GameReference(g, startingPlayer, otherPlayer);
            GameReference ref = super.build();
            TicTacToeGameMother gameMother = new TicTacToeGameMother();
            gameMother.setupAlmostFinishedGame(ref.startPlayer, ref.otherPlayer, ref.game);
            return ref;
        }
    }
}