package com.crudetech.tictactoe.usecases;

import com.crudetech.tictactoe.game.Grid;
import org.junit.Test;

import static com.crudetech.tictactoe.game.GridBuilder.gridOf;
import static org.mockito.Mockito.*;

public class PlayGameUseCaseTest {

    private Object gameId = "__gameId__";
    private Object movingPlayerId = "__movingPlayerId__";
    private Object otherPlayerId = "__otherPlayerId__";


    @Test
    public void givenPlayerMakeMove_ChangedGridsArePresented() throws Exception {
        Grid.Location movingPlayersMove = Grid.Location.of(Grid.Row.First, Grid.Column.First);

        Grid.Mark movingPlayersMark = Grid.Mark.Cross;
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

        PlayGameUseCase.Presenter mockPresenter = mock(PlayGameUseCase.Presenter.class);

        PlayGameUseCase playGame = new PlayGameUseCase(gameReferenceGatewayMock, players);


        PlayGameUseCase.Request request = createMoveRequest(movingPlayersMove);
        playGame.execute(request, mockPresenter);


        Grid expectedGridAfterInitialMove = gridOf("" +
                "X|*|*" +
                "*|*|*" +
                "*|*|*");


        Grid expectedGridAfterMove = gridOf("" +
                "X|*|*" +
                "*|*|*" +
                "*|*|O");

        verify(mockPresenter).display(expectedGridAfterInitialMove);
        verify(mockPresenter).display(expectedGridAfterMove);
    }

    @Test
    public void givenMovingPlayerWins_ResultIsPresented() {
        Grid.Location movingPlayersMove = Grid.Location.of(Grid.Row.First, Grid.Column.First);
        Grid.Mark movingPlayersMark = Grid.Mark.Cross;


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

        PlayGameUseCase.Presenter mockPresenter = mock(PlayGameUseCase.Presenter.class);

        PlayGameUseCase playGame = new PlayGameUseCase(gameReferenceGatewayMock, players);


        PlayGameUseCase.Request request = createMoveRequest(movingPlayersMove);
        playGame.execute(request, mockPresenter);

        Grid expectedGridAfterInitialMove = gridOf("" +
                "X|*|O" +
                "X|X|O" +
                "X|O|*");

        verify(mockPresenter, times(2)).display(expectedGridAfterInitialMove);
        verify(mockPresenter).highlight(FirstColumn);
        verify(mockPresenter).finished();
    }

    //loose
    //tie
    //already finished
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
