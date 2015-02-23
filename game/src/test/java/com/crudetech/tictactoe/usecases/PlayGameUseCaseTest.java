package com.crudetech.tictactoe.usecases;

import com.crudetech.tictactoe.game.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class PlayGameUseCaseTest {

    private Object gameId = "__gameId__";
    private Object movingPlayerId = "__movingPlayerId__";

    private GameReference gameReferenceMock;
    private GameReferencePresenterMock gameReferencePresenterMock;
    private Grid.Location movingPlayersMove;
    private PlayGameUseCase.Presenter mockPresenter;
    private UseCase<PlayGameUseCase.Request, PlayGameUseCase.Presenter> playGame;


    @Before
    public void setUp() throws Exception {
        movingPlayersMove = Grid.Location.of(Grid.Row.First, Grid.Column.First);

        gameReferenceMock = mock(GameReference.class);
        gameReferencePresenterMock = new GameReferencePresenterMock();
        doAnswer(gameReferencePresenterMock).when(gameReferenceMock).makeMove(eq(movingPlayerId), any(Grid.Location.class), any(GameReference.Presenter.class));


        GameReferenceGateway gameReferenceGatewayMock = mock(GameReferenceGateway.class);
        when(gameReferenceGatewayMock.fetchById(gameId)).thenReturn(gameReferenceMock);
        mockPresenter = mock(PlayGameUseCase.Presenter.class);

        playGame = new PlayGameUseCase(gameReferenceGatewayMock);
    }

    @Test
    public void givenPlayerMakeMove_NewGridIsPresented() throws Exception {
        PlayGameUseCase.Presenter mockPresenter = mock(PlayGameUseCase.Presenter.class);

        Grid.Mark movingPlayersMark = Grid.Mark.Cross;
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

        PlayGameUseCase.Request request = createMoveRequest();

        playGame.execute(request, mockPresenter);

        verify(mockPresenter).display(expectedGrid);
    }

    @Test
    public void givenPlayerMakeMove_MoveIsForwardedToGame() throws Exception {
        PlayGameUseCase.Request request = createMoveRequest();

        playGame.execute(request, mockPresenter);

        verify(gameReferenceMock).makeMove(eq(movingPlayerId), eq(movingPlayersMove), any(GameReference.Presenter.class));
    }

    @Test
    public void givenGameAnswers_GridIsDisplayed() throws Exception {
        gameReferencePresenterMock.displayGrid(LinearRandomAccessGrid.empty());

        PlayGameUseCase.Request request = createMoveRequest();

        playGame.execute(request, mockPresenter);

        verify(gameReferenceMock).makeMove(eq(movingPlayerId), eq(movingPlayersMove), any(GameReference.Presenter.class));
    }

    private PlayGameUseCase.Request createMoveRequest() {
        PlayGameUseCase.Request request = new PlayGameUseCase.Request();
        request.gameId = gameId;
        request.movingPlayerId = movingPlayerId;
        request.move = movingPlayersMove;
        return request;
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

        private Action<GameReference.Presenter> action = nullAction();

        @SuppressWarnings("unchecked")
        private static Action<GameReference.Presenter> nullAction() {
            return mock(Action.class);
        }

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
