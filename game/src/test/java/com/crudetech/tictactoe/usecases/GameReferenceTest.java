package com.crudetech.tictactoe.usecases;

import com.crudetech.tictactoe.game.*;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(HierarchicalContextRunner.class)
public class GameReferenceTest {

    private final String startingPlayerId = "__startingPlayerId__";
    private final String otherPlayerId = "__otherPlayerId__";

    static class GameReferenceBuilder extends GameReference.Builder {
        @Override
        Player convertStartingPlayer(PlayerReference player) {
            if (player instanceof HumanPlayerReference) {
                return new GameReference.HumanPlayer();
            } else if (player instanceof MockPlayerReference) {
                return mock(Player.class);
            } else if (player instanceof SingleMovePlayerReference) {
                return new SingleMovePlayer(((SingleMovePlayerReference) player).move);
            } else
                return super.convertStartingPlayer(player);
        }

        @Override
        Player convertOtherPlayer(PlayerReference player) {
            if (player instanceof HumanPlayerReference) {
                return new GameReference.HumanPlayer();
            } else if (player instanceof MockPlayerReference) {
                return mock(Player.class);
            } else if (player instanceof SingleMovePlayerReference) {
                return new SingleMovePlayer(((SingleMovePlayerReference) player).move);
            } else
                return super.convertOtherPlayer(player);
        }
    }

    static class MockPlayerReference extends PlayerReference {
    }

    public class GivenStartedGame {

        private GameReference gameRef;

        @Before
        public void setup() {
            PlayerReference startPlayer = new HumanPlayerReference();
            startPlayer.setId(startingPlayerId);
            PlayerReference otherPlayer = new MockPlayerReference();
            otherPlayer.setId(otherPlayerId);

            gameRef = new GameReferenceBuilder()
                    .withStartPlayer(startPlayer)
                    .withStartPlayerMark(Grid.Mark.Cross)
                    .withOtherPlayer(otherPlayer)
                    .build();
        }

        @Test
        public void givenStartingPlayerMakesMove_ResultIsPresented() throws Exception {
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
        public void givenOtherPlayerMakesMove_ResultIsPresented() throws Exception {

            Grid.Location startingPlayerMove = Grid.Location.of(Grid.Row.First, Grid.Column.Second);
            Grid.Location otherPlayerMove = Grid.Location.of(Grid.Row.First, Grid.Column.Third);
            GameReference.Presenter presenter = mock(GameReference.Presenter.class);

            gameRef.makeMove(startingPlayerId, startingPlayerMove, presenter);
            gameRef.makeMove(otherPlayerId, otherPlayerMove, presenter);

            Grid expectedGrid = LinearRandomAccessGrid.of(
                    Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.Nought,
                    Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                    Grid.Mark.None, Grid.Mark.None, Grid.Mark.None
            );

            verify(presenter).display(expectedGrid);
            verify(presenter, never()).highlight(any(Grid.ThreeInARow.class));
            verify(presenter, never()).finished();
        }
    }

    public class GivenAlmostFinishedGame {
        @Test
        public void givenMoveResultsInTie_resultIsPresented() throws Exception {
            PlayerReference startPlayer = new HumanPlayerReference();
            startPlayer.setId(startingPlayerId);
            PlayerReference otherPlayer = new SingleMovePlayerReference(Grid.Location.of(Grid.Row.First, Grid.Column.First));
            otherPlayer.setId(otherPlayerId);

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
            PlayerReference startPlayer = new HumanPlayerReference();
            startPlayer.setId(startingPlayerId);
            PlayerReference otherPlayer = new MockPlayerReference();
            otherPlayer.setId(otherPlayerId);

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
            PlayerReference startPlayer = new HumanPlayerReference();
            startPlayer.setId(startingPlayerId);
            PlayerReference otherPlayer = new SingleMovePlayerReference(Grid.Location.of(Grid.Row.Third, Grid.Column.Third));
            otherPlayer.setId(otherPlayerId);

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

        private final Grid.ThreeInARow FirstColumnWithCrosses =
                Grid.ThreeInARow.of(
                        Grid.Mark.Cross,
                        Grid.Location.of(Grid.Row.First, Grid.Column.First),
                        Grid.Location.of(Grid.Row.Second, Grid.Column.First),
                        Grid.Location.of(Grid.Row.Third, Grid.Column.First)
                );
        private final Grid.ThreeInARow ThirdColumnWithNoughts =
                Grid.ThreeInARow.of(
                        Grid.Mark.Nought,
                        Grid.Location.of(Grid.Row.First, Grid.Column.Third),
                        Grid.Location.of(Grid.Row.Second, Grid.Column.Third),
                        Grid.Location.of(Grid.Row.Third, Grid.Column.Third)
                );

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
        private class AlmostFinishedGameReferenceBuilder extends GameReferenceBuilder {
            @Override
            TicTacToeGame.Builder gameBuilder(Player startingPlayer, Grid.Mark startPlayersMark, Player otherPlayer) {
                TicTacToeGameMother gameMother = new TicTacToeGameMother();
                TicTacToeGame.Builder builder = super.gameBuilder(startingPlayer, startPlayersMark, otherPlayer);
                return builder.withGrid(gameMother.almostFinishedGrid());
            }
        }

    }

    public class GivenFinishedGame {
        @Test
        public void givenGameIsAlreadyFinishedOnMove_ErrorIsDisplayed() throws Exception {
            PlayerReference startPlayer = new HumanPlayerReference();
            startPlayer.setId(startingPlayerId);
            PlayerReference otherPlayer = new MockPlayerReference();
            otherPlayer.setId(otherPlayerId);

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
        private class FinishedGameReferenceBuilder extends GameReferenceBuilder {
            @Override
            TicTacToeGame.Builder gameBuilder(Player startingPlayer, Grid.Mark startPlayersMark, Player otherPlayer) {
                TicTacToeGameMother gameMother = new TicTacToeGameMother();
                TicTacToeGame.Builder builder = super.gameBuilder(startingPlayer, startPlayersMark, otherPlayer);
                return builder
                        .withStartingPlayerWins()
                        .withGrid(gameMother.finishedGridWitNoughtsWinning());
            }
        }
    }
}