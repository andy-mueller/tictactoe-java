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

            inOrder.verify(otherPlayer).yourTurn(game, expectedGridAfter1stPlayersMove);
            inOrder.verify(otherPlayer).moveWasMade(game, expectedGridAfter2ndPlayersMove);

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

            GameReference game = new AlmostFinishedGameReferenceBuilder()
                    .withStartPlayer(startPlayer)
                    .withStartPlayerMark(Grid.Mark.Cross)
                    .withOtherPlayer(otherPlayer)
                    .build()
                    .start();

            startPlayer.makeMove(game, Grid.Location.of(Grid.Row.Third, Grid.Column.Third));
            startPlayer.makeMove(game, Grid.Location.of(Grid.Row.First, Grid.Column.Second));


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

            Grid expectedGridAfter3rdMove = gridOf("" +
                    "O|X|O" +
                    "X|X|O" +
                    "X|O|X"
            );


            InOrder inOrder = inOrder(startPlayer, otherPlayer);
            inOrder.verify(startPlayer).moveWasMade(game, expectedGridAfter1stMove);
            inOrder.verify(otherPlayer).yourTurn(game, expectedGridAfter1stMove);
            inOrder.verify(otherPlayer).moveWasMade(game, expectedGridAfter2ndMove);
            inOrder.verify(startPlayer).yourTurn(game, expectedGridAfter2ndMove);
            inOrder.verify(startPlayer).moveWasMade(game, expectedGridAfter3rdMove);
            inOrder.verify(startPlayer).tie(game, expectedGridAfter3rdMove);
            inOrder.verify(otherPlayer).tie(game, expectedGridAfter3rdMove);
        }

        PlayerReference createSingleMovePlayerSpy(final Object id, final Grid.Location move){
            return createPlayerSpy(id, new SingleMovePlayerReference(move));
        }

        @Test
        public void givenMoveResultsInWinning_resultIsPresented() throws Exception {
            PlayerReference startPlayer = createPlayerSpy(startingPlayerId);
            PlayerReference otherPlayer = createPlayerSpy(otherPlayerId);

            GameReference game = new AlmostFinishedGameReferenceBuilder()
                    .withStartPlayer(startPlayer)
                    .withStartPlayerMark(Grid.Mark.Cross)
                    .withOtherPlayer(otherPlayer)
                    .build()
                    .start();

            startPlayer.makeMove(game, Grid.Location.of(Grid.Row.First, Grid.Column.First));


            Grid expectedGrid = LinearRandomAccessGrid.of(
                    Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.Nought,
                    Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Nought,
                    Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.None
            );

            verify(startPlayer).youWin(game, expectedGrid, FirstColumnWithCrosses);
            verify(otherPlayer).youLoose(game, expectedGrid, FirstColumnWithCrosses);
        }

        @Test
        public void givenMoveResultsLoss_resultIsPresented() throws Exception {
            PlayerReference startPlayer = createPlayerSpy(startingPlayerId);
            PlayerReference otherPlayer = createSingleMovePlayerSpy(otherPlayerId, Grid.Location.of(Grid.Row.Third, Grid.Column.Third));

            GameReference game = new AlmostFinishedGameReferenceBuilder()
                    .withStartPlayer(startPlayer)
                    .withStartPlayerMark(Grid.Mark.Cross)
                    .withOtherPlayer(otherPlayer)
                    .build()
                    .start();

            startPlayer.makeMove(game, Grid.Location.of(Grid.Row.First, Grid.Column.Second));



            Grid expectedGrid = LinearRandomAccessGrid.of(
                    Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.Nought,
                    Grid.Mark.Cross, Grid.Mark.Cross, Grid.Mark.Nought,
                    Grid.Mark.Cross, Grid.Mark.Nought, Grid.Mark.Nought
            );

            verify(startPlayer).youLoose(game, expectedGrid, ThirdColumnWithNoughts);
            verify(otherPlayer).youWin(game, expectedGrid, ThirdColumnWithNoughts);
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

    }

    public class GivenFinishedGame {
        @Test
        public void givenGameIsAlreadyFinishedOnMove_ErrorIsDisplayed() throws Exception {
            PlayerReference startPlayer = createPlayerSpy(startingPlayerId);
            PlayerReference otherPlayer = createPlayerSpy(otherPlayerId);

            GameReference finishedGame = new FinishedGameReferenceBuilder()
                    .withStartPlayer(startPlayer)
                    .withStartPlayerMark(Grid.Mark.Nought)
                    .withOtherPlayer(otherPlayer)
                    .build();


            startPlayer.makeMove(finishedGame, Grid.Location.of(Grid.Row.First, Grid.Column.Second));

            verify(startPlayer).gameAlreadyFinished();
        }
    }
}