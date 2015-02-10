package com.crudetech.tictactoe.usecases;

import com.crudetech.tictactoe.game.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

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

    @Test
    public void givenPlayerMakeMove_MoveIsForwardedToGame() throws Exception {
        PlayGameUseCase.Presenter mockPresenter = mock(PlayGameUseCase.Presenter.class);

        Grid.Location movingPlayersMove = Grid.Location.of(Grid.Row.First, Grid.Column.First);


        GameReference gameReference = mock(GameReference.class);
        GameReferenceGateway gameReferenceGatewayMock = mock(GameReferenceGateway.class);
        when(gameReferenceGatewayMock.fetchById(gameId)).thenReturn(gameReference);


        UseCase<PlayGameUseCase.Request, PlayGameUseCase.Presenter> playGame = new PlayGameUseCase(gameReferenceGatewayMock);

        PlayGameUseCase.Request request = new PlayGameUseCase.Request();
        request.gameId = gameId;
        request.movingPlayerId = movingPlayerId;
        request.move = movingPlayersMove;

        playGame.execute(request, mockPresenter);

        verify(gameReference).makeMove(eq(movingPlayerId), eq(movingPlayersMove), any(GameReference.Presenter.class));
    }

    @Test
    public void givenGameAnswers_GridIsDisplayed() throws Exception {
        PlayGameUseCase.Presenter mockPresenter = mock(PlayGameUseCase.Presenter.class);

        Grid.Location movingPlayersMove = Grid.Location.of(Grid.Row.First, Grid.Column.First);


        GameReference gameReference = mock(GameReference.class);
        GameReferencePresenterMock gameRefPresenterMock = new GameReferencePresenterMock();
        doAnswer(gameRefPresenterMock).when(gameReference).makeMove(eq(movingPlayerId), eq(movingPlayersMove), any(GameReference.Presenter.class));
        gameRefPresenterMock.displayGrid(LinearRandomAccessGrid.empty());
        GameReferenceGateway gameReferenceGatewayMock = mock(GameReferenceGateway.class);
        when(gameReferenceGatewayMock.fetchById(gameId)).thenReturn(gameReference);


        UseCase<PlayGameUseCase.Request, PlayGameUseCase.Presenter> playGame = new PlayGameUseCase(gameReferenceGatewayMock);

        PlayGameUseCase.Request request = new PlayGameUseCase.Request();
        request.gameId = gameId;
        request.movingPlayerId = movingPlayerId;
        request.move = movingPlayersMove;

        playGame.execute(request, mockPresenter);

        verify(gameReference).makeMove(eq(movingPlayerId), eq(movingPlayersMove), any(GameReference.Presenter.class));
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


    private static class GameReferencePresenterMock implements Answer<Void> {

        interface Action<T> {
            void execute(T item);
        }

        Action<GameReference.Presenter> action = mock(Action.class);

        @Override
        public Void answer(InvocationOnMock invocation) throws Throwable {
            GameReference.Presenter grp = (GameReference.Presenter) invocation.getArguments()[2];
            action.execute(grp);
            return null;
        }

        public void displayGrid(final Grid grid) {
            action = new Action<GameReference.Presenter>() {
                @Override
                public void execute(GameReference.Presenter item) {
                    item.display(grid);
                }
            };
        }
    }
}
