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
            public void tie(PlayerReference player, Grid grid) {

            }

            @Override
            public void won(PlayerReference player, Grid grid, Grid.ThreeInARow threeInARow) {
                presenter.display(grid);
                presenter.highlight(threeInARow);
                presenter.finished();
            }

            @Override
            public void lost(PlayerReference player, Grid grid, Grid.ThreeInARow threeInARow) {

            }

            @Override
            public void moveWasMade(PlayerReference player, Grid grid) {
                presenter.display(grid);
            }

            @Override
            public void yourTurn(PlayerReference player, Grid grid) {
                presenter.display(grid);
            }

            @Override
            public void gameAlreadyFinished() {

            }
        };
    }
}
