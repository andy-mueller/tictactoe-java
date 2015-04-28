package com.crudetech.tictactoe.usecases;

import com.crudetech.tictactoe.game.*;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public class PlayGameUseCaseTest {

    private Object gameId = "__gameId__";
    private Object movingPlayerId = "__movingPlayerId__";

    private GameReferenceMock gameReferenceMock;
    private Grid.Location movingPlayersMove;
    private PlayGameUseCase.Presenter mockPresenter;
    private UseCase<PlayGameUseCase.Request, PlayGameUseCase.Presenter> playGame;


    @Before
    public void setUp() throws Exception {
        movingPlayersMove = Grid.Location.of(Grid.Row.First, Grid.Column.First);

        gameReferenceMock = new GameReferenceMock();


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


        Player otherPlayer = new SingleMovePlayer(otherPlayersMove);
        Player movingPlayer = new HumanPlayer();

        GameReference gameReference = GameReference.builder()
                .withStartPlayer(movingPlayer)
                .withStartPlayerMark(movingPlayersMark)
                .withOtherPlayer(otherPlayer)
                .build();

        GameReferenceGateway gameReferenceGatewayMock = mock(GameReferenceGateway.class);
        when(gameReferenceGatewayMock.fetchById(gameId)).thenReturn(gameReference);


        UseCase<PlayGameUseCase.Request, PlayGameUseCase.Presenter> playGame = new PlayGameUseCase(gameReferenceGatewayMock);

        PlayGameUseCase.Request request = createMoveRequest();

        playGame.execute(request, mockPresenter);

        verify(mockPresenter).display(expectedGridAfterInitialMove);
        verify(mockPresenter).display(expectedGridAfterMove);
    }

    @Test
    public void givenPlayerMakeMove_MoveIsForwardedToGame() throws Exception {
        PlayGameUseCase.Request request = createMoveRequest();

        playGame.execute(request, mockPresenter);

        gameReferenceMock.verifyMakeMove(movingPlayerId, movingPlayersMove);
    }

    @Test
    public void givenGameAnswers_GridIsDisplayed() throws Exception {
        Grid grid = anyGrid();
        gameReferenceMock.displayGrid(grid);

        PlayGameUseCase.Request request = createMoveRequest();

        playGame.execute(request, mockPresenter);

        verify(mockPresenter).display(grid);
    }

    @Test
    public void givenGameAnswersWithHighlight_HighLightIsDisplayed() throws Exception {
        final Grid.ThreeInARow highlightedRow = diagonal();
        gameReferenceMock.execute(new GameReferenceMock.Action<GameReference.Presenter>() {
            @Override
            public void execute(GameReference.Presenter item) {
                item.highlight(highlightedRow);
            }
        });

        PlayGameUseCase.Request request = createMoveRequest();

        playGame.execute(request, mockPresenter);

        verify(mockPresenter).highlight(highlightedRow);
    }


    @Test
    public void givenGameAnswersFinished_FinishingIsDisplayed() throws Exception {
        gameReferenceMock.execute(new GameReferenceMock.Action<GameReference.Presenter>() {
            @Override
            public void execute(GameReference.Presenter item) {
                item.finished();
            }
        });

        PlayGameUseCase.Request request = createMoveRequest();

        playGame.execute(request, mockPresenter);

        verify(mockPresenter).finished();
    }

    private Grid.ThreeInARow diagonal() {
        return Grid.ThreeInARow.of(
                Grid.Mark.Cross,
                Grid.Location.of(Grid.Row.First, Grid.Column.First),
                Grid.Location.of(Grid.Row.Second, Grid.Column.Second),
                Grid.Location.of(Grid.Row.Third, Grid.Column.Third)
        );
    }

    private static Grid anyGrid() {
        return LinearRandomAccessGrid.of(
                Grid.Mark.Nought, Grid.Mark.None, Grid.Mark.Cross,
                Grid.Mark.None, Grid.Mark.Cross, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Nought);
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

    private static class GameReferenceMock extends GameReference {

        private Object movingPlayerId;
        private Grid.Location move;

        GameReferenceMock() {
            super(null, null, null);
        }

        interface Action<T> {
            void execute(T item);
        }

        private Action<GameReference.Presenter> action = nullAction();

        @SuppressWarnings("unchecked")
        private static Action<GameReference.Presenter> nullAction() {
            return mock(Action.class);
        }

        @Override
        public void makeMove(Object movingPlayerId, Grid.Location move, Presenter presenter) {
            this.movingPlayerId = movingPlayerId;
            this.move = move;
            action.execute(presenter);
        }

        public void displayGrid(final Grid grid) {
            execute(new Action<GameReference.Presenter>() {
                @Override
                public void execute(GameReference.Presenter item) {
                    item.display(grid);
                }
            });
        }

        public void execute(Action<Presenter> action) {
            this.action = action;
        }

        public void verifyMakeMove(Object playerId, Grid.Location playersMove) {
            assertThat(this.movingPlayerId, is(playerId));
            assertThat(this.move, is(playersMove));
        }
    }
}
