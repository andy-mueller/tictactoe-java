package com.crudetech.tictactoe.usecases;

import com.crudetech.tictactoe.game.*;

import java.util.Objects;


class CreateNewGameUseCase implements UseCase<CreateNewGameUseCase.Request, CreateNewGameUseCase.Presenter> {
    private final PlayerGateway playerReferences;
    private final PlayerFactory playerFactory;
    private final GameGateway games;

    public CreateNewGameUseCase(PlayerGateway players, PlayerFactory playerFactory, GameGateway games) {
        this.playerReferences = players;
        this.playerFactory = playerFactory;
        this.games = games;
    }

    public static class Response {
        public Object createdGameId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Response response = (Response) o;

            if (!Objects.equals(createdGameId, response.createdGameId)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(createdGameId);
        }

        @Override
        public String toString() {
            return "Response{" +
                    "createdGameId=" + createdGameId +
                    '}';
        }
    }

    public static interface Presenter {
        void display(Response response);
    }

    public static class Request {
        public Object startPlayerId;
        public Grid.Mark startPlayersMark;
        public Object otherPlayerId;
    }

    static class GameBuilder {

        private Player otherPlayer;
        private Grid.Mark startPlayerMark;
        private Player startingPlayer;

        public GameBuilder withStartPlayer(Player startingPlayer) {
            this.startingPlayer = startingPlayer;
            return this;
        }

        public GameBuilder withStartPlayerMark(Grid.Mark startPlayerMark) {
            this.startPlayerMark = startPlayerMark;
            return this;
        }

        public GameBuilder withOtherPlayer(Player otherPlayer) {
            this.otherPlayer = otherPlayer;
            return this;
        }

        public TicTacToeGame build() {
            TicTacToeGame g = newGame(startingPlayer, otherPlayer);
            g.startWithPlayer(startingPlayer, startPlayerMark);
            return g;
        }

        TicTacToeGame newGame(Player startingPlayer, Player otherPlayer) {
            return new TicTacToeGame(startingPlayer, otherPlayer);
        }
    }

    @Override
    public void execute(Request request, Presenter presenter) {
        Player startPlayer = createPlayer(request.startPlayerId);
        Player secondPlayer = createPlayer(request.otherPlayerId);

        TicTacToeGame game = gameBuilder()
                .withStartPlayer(startPlayer)
                .withStartPlayerMark(request.startPlayersMark)
                .withOtherPlayer(secondPlayer).build();

        Response response = new Response();
        response.createdGameId = games.add(game);
        presenter.display(response);
    }

    private Player createPlayer(Object playerId) {
        return playerFactory.create(playerReferences.fetchById(playerId));
    }

    GameBuilder gameBuilder() {
        return new GameBuilder();
    }
}
