package com.crudetech.tictactoe.usecases;

import java.util.Objects;

/**
 *
 */
public class CreateNewAiPlayerUseCase extends TypedUseCase<CreateNewAiPlayerUseCase.Request, CreateNewAiPlayerUseCase.Response> {
    private final PlayerGateway players;

    public static class Request implements UseCase.Request {
    }

    //test
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

    public CreateNewAiPlayerUseCase(PlayerGateway playerGateway) {
        players = playerGateway;
    }


    @Override
    protected void apply(Request request, Presenter<Response> presenter) {
        Response response = new Response();
        response.createdPlayerId = players.create(new AiPlayerReference());
        presenter.display(response);
    }
}
