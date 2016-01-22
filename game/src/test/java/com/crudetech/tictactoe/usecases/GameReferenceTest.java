package com.crudetech.tictactoe.usecases;

import com.crudetech.tictactoe.game.*;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;

import static com.crudetech.tictactoe.game.GridBuilder.gridOf;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(HierarchicalContextRunner.class)
public class GameReferenceTest {

    private final Object startingPlayerId = "__startingPlayerId__";
    private final Object otherPlayerId = "__otherPlayerId__";

    private static PlayerReference createPlayerSpy(Object playersId) {
        PlayerReference player = new SimplePlayer();
        return createPlayerSpy(playersId, player);
    }

    private static PlayerReference createPlayerSpy(Object playersId, PlayerReference player) {
        player.setId(playersId);
        return spy(player);
    }

    static class SimplePlayer extends PlayerReference{}

    public class GivenStartedGame {
        private GameReference game;
        private PlayerReference otherPlayer;
        private PlayerReference startPlayer;

        @Before
        public void setup() {
            startPlayer = createPlayerSpy(startingPlayerId);
            otherPlayer = createPlayerSpy(otherPlayerId);

            game = GameReference.builder()
                    .withStartPlayer(startPlayer)
                    .withStartPlayerMark(Grid.Mark.Cross)
                    .withOtherPlayer(otherPlayer)
                    .build()
                    .start();
        }

        @Test
        public void givenStartingPlayerMakesMove_ResultIsPresented() throws Exception {
            Grid.Location move = Grid.Location.of(Grid.Row.First, Grid.Column.Second);

            startPlayer.makeMove(game, move);

            Grid expectedGrid = LinearRandomAccessGrid.of(
                    Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.None,
                    Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                    Grid.Mark.None, Grid.Mark.None, Grid.Mark.None
            );
            verify(startPlayer).moveWasMade(game, expectedGrid);
            verifyGameIsNotFinished(startPlayer);
            verifyGameIsNotFinished(otherPlayer);
        }

        private void verifyGameIsNotFinished(PlayerReference player) {
            verify(player, never()).tie(any(GameReference.class), any(Grid.class));
            verify(player, never()).youLoose(any(GameReference.class), any(Grid.class), any(Grid.ThreeInARow.class));
            verify(player, never()).youWin(any(GameReference.class), any(Grid.class), any(Grid.ThreeInARow.class));
        }


        @Test
        public void givenOtherPlayerMakesMove_ResultIsPresented() throws Exception {
            Grid.Location startingPlayerMove = Grid.Location.of(Grid.Row.First, Grid.Column.Second);
            Grid.Location otherPlayerMove = Grid.Location.of(Grid.Row.First, Grid.Column.Third);

            startPlayer.makeMove(game, startingPlayerMove);
            otherPlayer.makeMove(game, otherPlayerMove);

            Grid expectedGridAfter1stPlayersMove = gridOf("" +
                    "*|X|*" +
                    "*|*|*" +
                    "*|*|*"
            );

            Grid expectedGridAfter2ndPlayersMove = gridOf("" +
                    "*|X|O" +
                    "*|*|*" +
                    "*|*|*"
            );

            InOrder inOrder = inOrder(startPlayer, otherPlayer);
            inOrder.verify(startPlayer).moveWasMade(game, expectedGridAfter1stPlayersMove);
            inOrder.verify(otherPlayer).moveWasMade(game, expectedGridAfter2ndPlayersMove);

            inOrder.verify(otherPlayer).yourTurn(game, expectedGridAfter2ndPlayersMove);

            inOrder.verify(otherPlayer).moveWasMade(game, expectedGridAfter2ndPlayersMove);
            inOrder.verify(startPlayer).moveWasMade(game, expectedGridAfter2ndPlayersMove);

            inOrder.verify(startPlayer).yourTurn(game, expectedGridAfter2ndPlayersMove);

            verifyGameIsNotFinished(startPlayer);
            verifyGameIsNotFinished(otherPlayer);
        }
    }

    public class GivenAlmostFinishedGame {
        @Test
        public void givenMoveResultsInTie_resultIsPresented() throws Exception {
            PlayerReference startPlayer = createPlayerSpy(startingPlayerId);
            PlayerReference otherPlayer = createSingleMovePlayerSpy(otherPlayerId, Grid.Location.of(Grid.Row.First, Grid.Column.First));
            otherPlayer.setId(otherPlayerId);

            GameReference gameRef = new AlmostFinishedGameReferenceBuilder()
                    .withStartPlayer(startPlayer)
                    .withStartPlayerMark(Grid.Mark.Cross)
                    .withOtherPlayer(otherPlayer)
                    .build()
                    .start();

            startPlayer.makeMove(Grid.Location.of(Grid.Row.Third, Grid.Column.Third));
            gameRef.makeMove(startingPlayerId, Grid.Location.of(Grid.Row.Third, Grid.Column.Third));


            Grid expectedGridAfter1stMove = gridOf("" +
                    "*|*|O" +
                    "X|X|O" +
                    "X|O|X"
            );


            Grid expectedGridAfter2ndMove = gridOf("" +
                    "O|*|O" +
                    "X|X|O" +
                    "X|O|X"
            );


            InOrder inOrder = inOrder(startPlayer);
            inOrder.verify(startPlayer).moveWasMade(gameRef, expectedGridAfter1stMove);
            inOrder.verify(startPlayer).moveWasMade(gameRef, expectedGridAfter2ndMove);
            inOrder.verify(startPlayer).tie(gameRef, expectedGridAfter2ndMove);
            inOrder.verify(otherPlayer).tie(gameRef, expectedGridAfter2ndMove);
        }

        PlayerReference createSingleMovePlayerSpy(final Object id, final Grid.Location move){
            return createPlayerSpy(id, new SingleMovePlayerReference(move));
        }

        @Test
        public void givenMoveResultsInWinning_resultIsPresented() throws Exception {
            PlayerReference startPlayer = createPlayerSpy(startingPlayerId);
            PlayerReference otherPlayer = createPlayerSpy(otherPlayerId);

            GameReference gameRef = new AlmostFinishedGameReferenceBuilder()
                    .withStartPlayer(startPlayer)
                    .withStartPlayerMark(Grid.Mark.Cross)
                    .withOtherPlayer(otherPlayer)
                    .build()
                    .start();

            gameRef.makeMove(startingPlayerId, Grid.Location.of(Grid.Row.First, Grid.Column.First));


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
                    .build()
                    .start();

            GameReference.Presenter presenter = mock(GameReference.Presenter.class);
            gameRef.makeMove(startingPlayerId, Grid.Location.of(Grid.Row.First, Grid.Column.Second));


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
        private class AlmostFinishedGameReferenceBuilder extends GameReference.Builder {
            public GameReference build() {
                TicTacToeGameMother gameMother = new TicTacToeGameMother();
                withGrid(gameMother.almostFinishedGrid());
                return super.build();
            }
        }

    }


    public class GivenFinishedGame {
        @Test
        public void givenGameIsAlreadyFinishedOnMove_ErrorIsDisplayed() throws Exception {
            PlayerReference startPlayer = new HumanPlayerReference();
            startPlayer.setId(startingPlayerId);
            PlayerReference otherPlayer = mock(PlayerReference.class);
            otherPlayer.setId(otherPlayerId);

            GameReference finishedGameRef = new FinishedGameReferenceBuilder()
                    .withStartPlayer(startPlayer)
                    .withStartPlayerMark(Grid.Mark.Nought)
                    .withOtherPlayer(otherPlayer)
                    .build();

            GameReference.Presenter presenter = mock(GameReference.Presenter.class);
            finishedGameRef.makeMove(startingPlayerId, Grid.Location.of(Grid.Row.First, Grid.Column.Second));

            verify(presenter).gameAlreadyFinished();
        }


        /**
         * Creates a game is started, finished and won by the O player:
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
        private class FinishedGameReferenceBuilder extends GameReference.Builder {
            @Override
            public GameReference build() {
                TicTacToeGameMother gameMother = new TicTacToeGameMother();
                withGrid(gameMother.finishedGridWithNoughtsWinning());
                withState(TicTacToeGameFsm.State.StartingPlayerWins);
                return super.build();
            }
        }
    }
}