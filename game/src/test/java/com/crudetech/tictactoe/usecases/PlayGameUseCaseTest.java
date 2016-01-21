package com.crudetech.tictactoe.usecases;

import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class PlayGameUseCaseTest {

    private Object gameId = "__gameId__";
    private Object movingPlayerId = "__movingPlayerId__";
    private Object otherPlayerId = "__otherPlayerId__";

    private Grid.Location movingPlayersMove;
    private PlayGameUseCase.Presenter mockPresenter;
    private UseCase<PlayGameUseCase.Request, PlayGameUseCase.Presenter> playGame;


    @Before
    public void setUp() throws Exception {
        movingPlayersMove = Grid.Location.of(Grid.Row.First, Grid.Column.First);

        GameReference gameReferenceMock = mock(GameReference.class);


        GameReferenceGateway gameReferenceGatewayMock = mock(GameReferenceGateway.class);
        when(gameReferenceGatewayMock.fetchById(gameId)).thenReturn(gameReferenceMock);
        mockPresenter = mock(PlayGameUseCase.Presenter.class);

        playGame = new PlayGameUseCase(gameReferenceGatewayMock);
    }

    @Test
    public void givenPlayerMakeMove_ChangedGridsArePresented() throws Exception {
        Grid.Mark movingPlayersMark = Grid.Mark.Cross;
        Grid.Location otherPlayersMove = Grid.Location.of(Grid.Row.Third, Grid.Column.Third);

        Grid expectedGridAfterInitialMove = LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None
        );

        Grid expectedGridAfterMove = LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought
        );


        PlayerReference movingPlayer = new HumanPlayerReference();
        movingPlayer.setId(movingPlayerId);
        PlayerReference otherPlayer = new SingleMovePlayerReference(otherPlayersMove);
        otherPlayer.setId(otherPlayerId);

        GameReference gameReference = GameReference.builder()
                .withStartPlayer(movingPlayer)
                .withStartPlayerMark(movingPlayersMark)
                .withOtherPlayer(otherPlayer)
                .build()
                .start();

        GameReferenceGateway gameReferenceGatewayMock = mock(GameReferenceGateway.class);
        when(gameReferenceGatewayMock.fetchById(gameId)).thenReturn(gameReference);


        PlayGameUseCase.Request request = createMoveRequest();

        playGame.execute(request, mockPresenter);

        verify(mockPresenter).display(expectedGridAfterInitialMove);
        verify(mockPresenter).display(expectedGridAfterMove);
    }


    private PlayGameUseCase.Request createMoveRequest() {
        PlayGameUseCase.Request request = new PlayGameUseCase.Request();
        request.gameId = gameId;
        request.movingPlayerId = movingPlayerId;
        request.move = movingPlayersMove;
        return request;
    }
}
