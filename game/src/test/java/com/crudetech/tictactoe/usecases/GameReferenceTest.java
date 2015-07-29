package com.crudetech.tictactoe.usecases;

import com.crudetech.tictactoe.game.*;
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

    @Test
    public void givenMoveResultsInTie_resultIsPresented() throws Exception {
        Player startPlayer = new GameReference.HumanPlayer();
        Player otherPlayer = new SingleMovePlayer(Grid.Location.of(Grid.Row.First, Grid.Column.First));
        GameReference gameRef = new AlmostFinishedGameReferenceBuilder()
                .withStartPlayer(startPlayer)
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

    @Test
    public void givenMoveResultsInWinning_resultIsPresented() throws Exception {
        Player startPlayer = new GameReference.HumanPlayer();
        Player otherPlayer = mock(Player.class);
        GameReference gameRef = new AlmostFinishedGameReferenceBuilder()
                .withStartPlayer(startPlayer)
                .withStartPlayerMark(Grid.Mark.Cross)
                .withOtherPlayer(otherPlayer)
                .build();

        GameReference.Presenter presenter = mock(GameReference.Presenter.class);
        gameRef.makeMove(startingPlayerId, Grid.Location.of(Grid.Row.First, Grid.Column.First), presenter);


        Grid expectedGrid = LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.Nought,
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Nought,
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.None
        );

        verify(presenter, times(2)).display(expectedGrid);
        verify(presenter).highlight(FirstColumnWithCrosses);
        verify(presenter).finished();
    }

    @Test
    public void givenMoveResultsLoss_resultIsPresented() throws Exception {
        Player startPlayer = new GameReference.HumanPlayer();
        Player otherPlayer = new SingleMovePlayer(Grid.Location.of(Grid.Row.Third, Grid.Column.Third));
        GameReference gameRef = new AlmostFinishedGameReferenceBuilder()
                .withStartPlayer(startPlayer)
                .withStartPlayerMark(Grid.Mark.Cross)
                .withOtherPlayer(otherPlayer)
                .build();

        GameReference.Presenter presenter = mock(GameReference.Presenter.class);
        gameRef.makeMove(startingPlayerId, Grid.Location.of(Grid.Row.First, Grid.Column.Second), presenter);


        Grid expectedGrid = LinearRandomAccessGrid.of(
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.Nought,
                Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Nought,
                Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.Nought
        );

        verify(presenter).display(expectedGrid);
        verify(presenter).highlight(ThirdColumnWithNoughts);
        verify(presenter).finished();
    }

    private static final Grid.ThreeInARow FirstColumnWithCrosses =
            Grid.ThreeInARow.of(
                    Grid.Mark.Cross,
                    Grid.Location.of(Grid.Row.First, Grid.Column.First),
                    Grid.Location.of(Grid.Row.Second, Grid.Column.First),
                    Grid.Location.of(Grid.Row.Third, Grid.Column.First)
            );
    private static final Grid.ThreeInARow ThirdColumnWithNoughts =
            Grid.ThreeInARow.of(
                    Grid.Mark.Nought,
                    Grid.Location.of(Grid.Row.First, Grid.Column.Third),
                    Grid.Location.of(Grid.Row.Second, Grid.Column.Third),
                    Grid.Location.of(Grid.Row.Third, Grid.Column.Third)
            );

    @Test
    public void givenGameIsAlreadyFinishedOnMove_ErrorIsDisplayed() throws Exception {
        Player startPlayer = new GameReference.HumanPlayer();
        Player otherPlayer = mock(Player.class);
        GameReference finishedGameRef = new FinishedGameReferenceBuilder()
                .withStartPlayer(startPlayer)
                .withStartPlayerMark(Grid.Mark.Cross)
                .withOtherPlayer(otherPlayer)
                .build();

        GameReference.Presenter presenter = mock(GameReference.Presenter.class);
        finishedGameRef.makeMove(startingPlayerId, Grid.Location.of(Grid.Row.First, Grid.Column.Second), presenter);

        verify(presenter).gameAlreadyFinished();
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
        TicTacToeGame.Builder gameBuilder() {
            TicTacToeGameMother gameMother = new TicTacToeGameMother();
            TicTacToeGame.Builder builder = super.gameBuilder();
            return builder.withGrid(gameMother.almostFinishedGrid());
        }
    }

    /**
     * Creates a game is finished nd won by the x player:
     * <pre>
     *    |   | O
     * ---+---+---
     *  X | X | O
     * ---+---+---
     *  X | O | O
     * </pre>
     * <p/>
     * It is the no players turn now
     */
    private static class FinishedGameReferenceBuilder extends GameReference.Builder {
        @Override
        TicTacToeGame.Builder gameBuilder() {
            TicTacToeGameMother gameMother = new TicTacToeGameMother();
            TicTacToeGame.Builder builder = super.gameBuilder();
            return builder
                    .withStartingPlayerWins()
                    .withGrid(gameMother.finishedGridWitNoughtsWinning());
        }
    }
}