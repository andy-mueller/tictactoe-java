package com.crudetech.tictactoe.usecases;

import com.crudetech.tictactoe.game.Player;

import java.util.Objects;

/**
 *
 */
class CreateNewPlayerUseCase extends TypedUseCase<CreateNewPlayerUseCase.Request, CreateNewPlayerUseCase.Response> {
    private interface PlayerFactory {
        Player create();
    }

    public enum PlayerType implements PlayerFactory {
        Computer;
        @Override
        public Player create() {
            throw new RuntimeException("Not implemented yet!");
        }
    }

    static class Request implements UseCase.Request {
        public Object playerType;
    }

    static class Response {
        public Object createdPlayerId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || this.getClass() != o.getClass()) return false;

            Response response = (Response) o;

            return Objects.equals(createdPlayerId, response.createdPlayerId);

        }


        @Override
        public int hashCode() {
            return Objects.hashCode(createdPlayerId);
        }

        @Override
        public String toString() {
            return "Response{" +
                    "createdPlayerId=" + createdPlayerId +
                    '}';
        }
    }

    private final PlayerGateway players;

    CreateNewPlayerUseCase(PlayerGateway players) {
        this.players = players;
    }

    @Override
    protected void apply(Request request, Presenter<Response> presenter) {
        Response response = new Response();
        response.createdPlayerId = players.create(null);
        presenter.display(response);
    }

    @Override
    public CreateNewPlayerUseCase.Request.Builder requestBuilder() {
        return new MapRequestBuilder() {
            @Override
            public UseCase.Request createRequest() {
                Request r = new Request();
                r.playerType = valueFor("playerType");
                return r;
            }
        };
    }
}
