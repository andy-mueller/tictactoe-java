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

    static interface Presenter {
        void display(Grid grid);

        void highlight(Grid.ThreeInARow threeInARow);

        void finished();
    }

    @Override
    public void execute(Request request, Presenter presenter) {
        GameReference gameReference = games.fetchById(request.gameId);

        gameReference.makeMove(request.movingPlayerId, request.move, adapt(presenter));
    }

    private GameReference.Presenter adapt(final Presenter presenter) {
        return new GameReference.Presenter() {
            @Override
            public void display(Grid grid) {
                presenter.display(grid);
            }

            @Override
            public void highlight(Grid.ThreeInARow threeInARow) {
                presenter.highlight(threeInARow);
            }

            @Override
            public void finished() {
                presenter.finished();
            }
        };
    }
}
