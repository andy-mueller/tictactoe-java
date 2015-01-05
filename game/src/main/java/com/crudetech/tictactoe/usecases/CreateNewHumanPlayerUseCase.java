package com.crudetech.tictactoe.usecases;

import java.util.Objects;

/**
 *
 */
public class CreateNewHumanPlayerUseCase implements UseCase<CreateNewHumanPlayerUseCase.Response> {
    private final PlayerGateway players;


    public static class Request implements UseCase.Request {
    }

    public static class Response {
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
            return "CreateNewAIPlayerUseCase.Response{" +
                    "createdPlayerId=" + createdPlayerId +
                    '}';
        }
    }

    public CreateNewHumanPlayerUseCase(PlayerGateway players) {
        this.players = players;
    }

    @Override
    public void execute(UseCase.Request request, Presenter<Response> presenter) {
        Response response = new Response();
        response.createdPlayerId = players.create(new HumanPlayerReference());
        presenter.display(response);
    }
}
