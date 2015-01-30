package com.crudetech.tictactoe.usecases;

import com.crudetech.tictactoe.game.Grid;

class PlayGameUseCase implements UseCase<PlayGameUseCase.Request, PlayGameUseCase.Presenter> {
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
        GameReference gameReference = games.fetchById(request.gameId);

        HumanPlayer movingPlayer = (HumanPlayer) gameReference.getPlayerById(request.movingPlayerId);
        movingPlayer.setPresenter(presenter);
        gameReference.makeMove(movingPlayer, request.move);
//        movingPlayer.resetPresenter();
    }
}
