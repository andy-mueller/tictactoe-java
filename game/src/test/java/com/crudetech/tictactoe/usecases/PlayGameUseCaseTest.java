package com.crudetech.tictactoe.usecases;

import com.crudetech.tictactoe.game.*;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PlayGameUseCaseTest {

    private Object gameId = "__gameId__";
    private Object movingPlayerId = "__movingPlayerId__";


    @Test
    public void givenPlayerMakeMove_NewGridIsPresented() throws Exception {
        PlayGameUseCase.Presenter mockPresenter = mock(PlayGameUseCase.Presenter.class);

        Grid.Mark movingPlayersMark = Grid.Mark.Cross;
        Grid.Location movingPlayersMove = Grid.Location.of(Grid.Row.First, Grid.Column.First);
        Grid.Location otherPlayersMove = Grid.Location.of(Grid.Row.Third, Grid.Column.Third);

        Grid expectedGrid = LinearRandomAccessGrid.of(
                Grid.Mark.Cross, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought
        );


        Player otherPlayer = new SingleMovePlayer(otherPlayersMove);
        Player movingPlayer = new HumanPlayer();
        TicTacToeGame game = new TicTacToeGame(movingPlayer, otherPlayer);
        game.startWithPlayer(movingPlayer, movingPlayersMark);
        GameReference gameReference = new GameReference(game, movingPlayer, otherPlayer);

        GameReferenceGateway gameReferenceGatewayMock = mock(GameReferenceGateway.class);
        when(gameReferenceGatewayMock.fetchById(gameId)).thenReturn(gameReference);


        UseCase<PlayGameUseCase.Request, PlayGameUseCase.Presenter> playGame = new PlayGameUseCase(gameReferenceGatewayMock);

        PlayGameUseCase.Request request = new PlayGameUseCase.Request();
        request.gameId = gameId;
        request.movingPlayerId = movingPlayerId;
        request.move = movingPlayersMove;

        playGame.execute(request, mockPresenter);

        verify(mockPresenter).display(expectedGrid);
    }

    private static class SingleMovePlayer extends ComputerPlayer {
        private final Grid.Location move;

        public SingleMovePlayer(Grid.Location otherPlayersMove) {
            move = otherPlayersMove;
        }

        @Override
        protected Grid.Location computeNextMove(Grid actualGrid) {
            return move;
        }
    }

}
