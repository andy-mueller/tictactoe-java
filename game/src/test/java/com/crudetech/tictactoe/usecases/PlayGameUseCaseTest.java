package com.crudetech.tictactoe.usecases;

import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.Player;
import com.crudetech.tictactoe.game.TicTacToeGame;

public class PlayGameUseCaseTest {

    private Object gameId = "__gameId__";
    private Object movingPlayerId = "__movingPlayerId__";


//    @Test
//    public void givenPlayerMakeMove_NewGridIsPresented() throws Exception {
//        PlayGameUseCase.Presenter mockPresenter = mock(PlayGameUseCase.Presenter.class);
//
//        HumanPlayerReference movingPlayer = new HumanPlayerReference();
//
//        PlayerReferenceGateway playerReferenceGatewayMock = mock(PlayerReferenceGateway.class);
//        when(playerReferenceGatewayMock.fetchById(movingPlayerId)).thenReturn(movingPlayer);
//
//        GameReference gameReference = new GameReference(null, movingPlayer, otherPlayer);
//
//        GameReferenceGateway gameReferenceGatewayMock = mock(GameReferenceGateway.class);
//        when(gameReferenceGatewayMock.fetchById(gameId)).thenReturn(preparedGame);
//        UseCase<PlayGameUseCase.Request, PlayGameUseCase.Presenter> playGame = new PlayGameUseCase(gameReferenceGatewayMock);
//
//        PlayGameUseCase.Request request = new PlayGameUseCase.Request();
//        request.gameId = gameId;
//        request.movingPlayerId = movingPlayerId;
//        request.move = Grid.Location.of(Grid.Row.Second, Grid.Column.Second);
//
//        playGame.execute(request, mockPresenter);
//
//        Grid expectedGrid = LinearRandomAccessGrid.of();
//        verify(mockPresenter).display(expectedGrid);
//    }


    static class PlayGameUseCase implements UseCase<PlayGameUseCase.Request, PlayGameUseCase.Presenter> {
        private final GameReferenceGateway games;

        public PlayGameUseCase(GameReferenceGateway games) {
            this.games = games;
        }

        public static class Request {
            public Object gameId;
            public Object movingPlayerId;
            public Grid.Location move;
        }

        public static class Response {
        }

        static interface Presenter {
            void display(Grid grid);
        }

        @Override
        public void execute(Request request, Presenter presenter) {
        }
    }

    static class HumanPlayer implements Player {
        @Override
        public void yourTurn(Grid actualGrid) {
            throw new RuntimeException("Not implemented yet!");
        }

        @Override
        public void youWin(Grid actualGrid, Grid.ThreeInARow triple) {
            throw new RuntimeException("Not implemented yet!");
        }

        @Override
        public void youLoose(Grid actualGrid, Grid.ThreeInARow triple) {
            throw new RuntimeException("Not implemented yet!");
        }

        @Override
        public void tie(Grid actualGrid) {
            throw new RuntimeException("Not implemented yet!");
        }

        @Override
        public void setGame(TicTacToeGame game) {
            throw new RuntimeException("Not implemented yet!");
        }
    }
}
