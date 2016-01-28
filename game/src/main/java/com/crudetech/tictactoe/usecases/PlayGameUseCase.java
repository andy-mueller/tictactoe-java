package com.crudetech.tictactoe.usecases;

import com.crudetech.tictactoe.game.Grid;

class PlayGameUseCase implements UseCase<PlayGameUseCase.Request, PlayGameUseCase.Presenter> {
    private final GameReferenceGateway games;
    private final PlayerReferenceGateway players;

    public PlayGameUseCase(GameReferenceGateway games, PlayerReferenceGateway players) {
        this.games = games;
        this.players = players;
    }

    public static class Request {
        public Object gameId;
        public Object movingPlayerId;
        public Grid.Location move;
    }

    interface Presenter {
        void display(Grid grid);

        void highlight(Grid.ThreeInARow threeInARow);

        void finished();
    }

    @Override
    public void execute(Request request, Presenter presenter) {
        GameReference game = games.fetchById(request.gameId);

        PlayerReference movingPlayer = players.fetchById(request.movingPlayerId);
        movingPlayer.setPresenter(adapt(presenter));

        movingPlayer.makeMove(game, request.move);
        //movingPlayer.resetPresenter()
    }

    private PlayerReference.Presenter adapt(final Presenter presenter) {
        return new PlayerReference.Presenter() {
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

            @Override
            public void gameAlreadyFinished() {
            }
        };
    }
}
