package com.crudetech.tictactoe.usecases;

import com.crudetech.tictactoe.game.*;

import java.util.Objects;


class CreateNewGameUseCase implements UseCase<CreateNewGameUseCase.Request, CreateNewGameUseCase.Presenter> {
    private final PlayerReferenceGateway playerReferences;
    private final PlayerFactory playerFactory;
    private final GameReferenceGateway games;

    public CreateNewGameUseCase(PlayerReferenceGateway players, PlayerFactory playerFactory, GameReferenceGateway games) {
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

    @Override
    public void execute(Request request, Presenter presenter) {
        PlayerReference startingPlayer = playerReferences.fetchById(request.startPlayerId);
        PlayerReference otherPlayer = playerReferences.fetchById(request.otherPlayerId);

        GameReference gameReference = gameReferenceBuilder()
                .withStartPlayer(startingPlayer)
                .withStartPlayerMark(request.startPlayersMark)
                .withOtherPlayer(otherPlayer).build();

        Response response = new Response();
        response.createdGameId = games.add(gameReference);
        presenter.display(response);
    }

    private Player createPlayer(Object playerId) {
        return playerFactory.create(playerReferences.fetchById(playerId));
    }

    GameReference.Builder gameReferenceBuilder() {
        return new GameReference.Builder();
    }
}
