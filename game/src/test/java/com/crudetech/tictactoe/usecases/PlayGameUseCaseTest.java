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

    public class GivenAStartedGame {
        private PlayGameUseCase.Presenter movingPlayerPresenter;
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

            movingPlayerPresenter = mock(PlayGameUseCase.Presenter.class);

            playGame = new PlayGameUseCase(gameReferenceGatewayMock, players);
        }

        @Test
        public void givenPlayerMakeMove_ChangedGridsArePresented() throws Exception {
            Grid.Location movingPlayersMove = Grid.Location.of(Grid.Row.First, Grid.Column.First);


            PlayGameUseCase.Request request = createMoveRequest(movingPlayersMove, movingPlayerId);
            playGame.execute(request, movingPlayerPresenter);


            Grid expectedGridAfterInitialMove = gridOf("" +
                    "X|*|*" +
                    "*|*|*" +
                    "*|*|*");


            Grid expectedGridAfterMove = gridOf("" +
                    "X|*|*" +
                    "*|*|*" +
                    "*|*|O");

            verify(movingPlayerPresenter).display(expectedGridAfterInitialMove);
            verify(movingPlayerPresenter).display(expectedGridAfterMove);
        }
    }

    public class GivenAnAlmostFinishedGame {
        private PlayGameUseCase.Presenter movingPlayerPresenter;
        private PlayGameUseCase.Presenter otherPlayerPresenter;
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
            when(players.fetchById(otherPlayerId)).thenReturn(otherPlayer);

            movingPlayerPresenter = mock(PlayGameUseCase.Presenter.class);
            otherPlayerPresenter = mock(PlayGameUseCase.Presenter.class);

            playGame = new PlayGameUseCase(gameReferenceGatewayMock, players);

        }
        @Test
        public void givenMovingPlayerWins_ResultIsPresented() {
            Grid.Location movingPlayersMove = Grid.Location.of(Grid.Row.First, Grid.Column.First);


            makeMove(movingPlayerId, movingPlayerPresenter, movingPlayersMove);

            Grid expectedGridAfterInitialMove = gridOf("" +
                    "X|*|O" +
                    "X|X|O" +
                    "X|O|*");

            verify(movingPlayerPresenter, times(2)).display(expectedGridAfterInitialMove);
            verify(movingPlayerPresenter).highlight(FirstColumn);
            verify(movingPlayerPresenter).finished();
        }

        @Test
        public void givenMovesEndsInTie_ResultIsPresented() {
            makeMove(movingPlayerId, movingPlayerPresenter, Grid.Location.of(Grid.Row.Third, Grid.Column.Third));

            makeMove(otherPlayerId, otherPlayerPresenter, Grid.Location.of(Grid.Row.First, Grid.Column.First));

            makeMove(movingPlayerId, movingPlayerPresenter, Grid.Location.of(Grid.Row.First, Grid.Column.Second));

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

            InOrder inOrder = inOrder(movingPlayerPresenter);
            inOrder.verify(movingPlayerPresenter, atLeast(1)).display(expectedGridAfterInitialMove);
            inOrder.verify(movingPlayerPresenter, atLeast(1)).display(expectedGridAfter2ndMove);
            inOrder.verify(movingPlayerPresenter, times(2)).display(expectedGridAfter3rdMove);
            inOrder.verify(movingPlayerPresenter, never()).highlight(any(Grid.ThreeInARow.class));
            inOrder.verify(movingPlayerPresenter).finished();
        }

        private void makeMove(Object movingPlayerId, PlayGameUseCase.Presenter presenter, Grid.Location move) {
            PlayGameUseCase.Request othersRequest = createMoveRequest(move, movingPlayerId);
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

    private PlayGameUseCase.Request createMoveRequest(Grid.Location movingPlayersMove, Object movingPlayerId) {
        PlayGameUseCase.Request request = new PlayGameUseCase.Request();
        request.gameId = gameId;
        request.movingPlayerId = movingPlayerId;
        request.move = movingPlayersMove;
        return request;
    }
}
