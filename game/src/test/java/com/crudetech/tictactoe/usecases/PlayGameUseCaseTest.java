package com.crudetech.tictactoe.usecases;

import com.crudetech.tictactoe.game.Grid;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;

import static com.crudetech.tictactoe.game.GridBuilder.gridOf;
import static org.mockito.Mockito.*;

@RunWith(HierarchicalContextRunner.class)
public class PlayGameUseCaseTest {

    private Object gameId = "__gameId__";
    private Object movingPlayerId = "__movingPlayerId__";
    private final Grid.Mark movingPlayersMark = Grid.Mark.Cross;
    private Object otherPlayerId = "__otherPlayerId__";
    private PlayGameUseCase playGame;
    private PlayGameUseCase.Presenter presenter;

    public class GivenAStartedGame {
        @Before
        public void setUp() {
            Grid.Location otherPlayersMove = Grid.Location.of(Grid.Row.Third, Grid.Column.Third);


            PlayerReference movingPlayer = new HumanPlayerReference();
            movingPlayer.setId(movingPlayerId);
            PlayerReference otherPlayer = new SingleMovePlayerReference(otherPlayersMove);
            otherPlayer.setId(otherPlayerId);

            final GameReference gameReference = GameReference.builder()
                    .withStartPlayer(movingPlayer)
                    .withStartPlayerMark(movingPlayersMark)
                    .withOtherPlayer(otherPlayer)
                    .build()
                    .start();

            GameReferenceGateway gameReferenceGatewayMock = mock(GameReferenceGateway.class);
            when(gameReferenceGatewayMock.fetchById(gameId)).thenReturn(gameReference);


            PlayerReferenceGateway players = mock(PlayerReferenceGateway.class);
            when(players.fetchById(movingPlayerId)).thenReturn(movingPlayer);

            presenter = mock(PlayGameUseCase.Presenter.class);

            playGame = new PlayGameUseCase(gameReferenceGatewayMock, players);
        }

        @Test
        public void givenPlayerMakeMove_ChangedGridsArePresented() throws Exception {
            Grid.Location movingPlayersMove = Grid.Location.of(Grid.Row.First, Grid.Column.First);


            PlayGameUseCase.Request request = createMoveRequest(movingPlayersMove);
            playGame.execute(request, presenter);


            Grid expectedGridAfterInitialMove = gridOf("" +
                    "X|*|*" +
                    "*|*|*" +
                    "*|*|*");


            Grid expectedGridAfterMove = gridOf("" +
                    "X|*|*" +
                    "*|*|*" +
                    "*|*|O");

            verify(presenter).display(expectedGridAfterInitialMove);
            verify(presenter).display(expectedGridAfterMove);
        }
    }

    class GivenAnAlmostFinishedGame {
        @Before
        public void setUp() throws Exception {

            PlayerReference movingPlayer = new HumanPlayerReference();
            movingPlayer.setId(movingPlayerId);
            PlayerReference otherPlayer = new HumanPlayerReference();
            otherPlayer.setId(otherPlayerId);
            GameReference gameReference = new AlmostFinishedGameReferenceBuilder()
                    .withStartPlayer(movingPlayer)
                    .withStartPlayerMark(movingPlayersMark)
                    .withOtherPlayer(otherPlayer)
                    .build()
                    .start();


            GameReferenceGateway gameReferenceGatewayMock = mock(GameReferenceGateway.class);
            when(gameReferenceGatewayMock.fetchById(gameId)).thenReturn(gameReference);


            PlayerReferenceGateway players = mock(PlayerReferenceGateway.class);
            when(players.fetchById(movingPlayerId)).thenReturn(movingPlayer);

            presenter = mock(PlayGameUseCase.Presenter.class);

            playGame = new PlayGameUseCase(gameReferenceGatewayMock, players);

        }
        @Test
        public void givenMovingPlayerWins_ResultIsPresented() {
            Grid.Location movingPlayersMove = Grid.Location.of(Grid.Row.First, Grid.Column.First);


            makeMove(movingPlayersMove);

            Grid expectedGridAfterInitialMove = gridOf("" +
                    "X|*|O" +
                    "X|X|O" +
                    "X|O|*");

            verify(presenter, times(2)).display(expectedGridAfterInitialMove);
            verify(presenter).highlight(FirstColumn);
            verify(presenter).finished();
        }

        @Test
        public void givenMovesEndsInTie_ResultIsPresented() {
            makeMove(Grid.Location.of(Grid.Row.Third, Grid.Column.Third));

            makeMove(Grid.Location.of(Grid.Row.First, Grid.Column.First));

            makeMove(Grid.Location.of(Grid.Row.First, Grid.Column.First));

            Grid expectedGridAfterInitialMove = gridOf("" +
                    "*|*|O" +
                    "X|X|O" +
                    "X|O|X");

            Grid expectedGridAfter2ndMove = gridOf("" +
                    "O|*|O" +
                    "X|X|O" +
                    "X|O|X");

            Grid expectedGridAfter3rdMove = gridOf("" +
                    "O|X|O" +
                    "X|X|O" +
                    "X|O|X");

            InOrder inOrder = inOrder(presenter);
            inOrder.verify(presenter, times(2)).display(expectedGridAfterInitialMove);
            inOrder.verify(presenter, times(2)).display(expectedGridAfter2ndMove);
            inOrder.verify(presenter, times(1)).display(expectedGridAfter3rdMove);
            inOrder.verify(presenter, never()).highlight(FirstColumn);
            inOrder.verify(presenter).finished();
        }

        private void makeMove(Grid.Location move) {
            PlayGameUseCase.Request othersRequest = createMoveRequest(move);
            playGame.execute(othersRequest, presenter);
        }

        //loose
        //already finished
    }

    static final Grid.ThreeInARow FirstColumn = Grid.ThreeInARow.of(
            Grid.Mark.Cross,
            Grid.Location.of(Grid.Row.First, Grid.Column.First),
            Grid.Location.of(Grid.Row.Second, Grid.Column.First),
            Grid.Location.of(Grid.Row.Third, Grid.Column.First)
    );

    private PlayGameUseCase.Request createMoveRequest(Grid.Location movingPlayersMove) {
        PlayGameUseCase.Request request = new PlayGameUseCase.Request();
        request.gameId = gameId;
        request.movingPlayerId = movingPlayerId;
        request.move = movingPlayersMove;
        return request;
    }
}
