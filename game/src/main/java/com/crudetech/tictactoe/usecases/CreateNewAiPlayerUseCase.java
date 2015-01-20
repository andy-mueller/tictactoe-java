package com.crudetech.tictactoe.usecases;

import java.util.Objects;


public class CreateNewAiPlayerUseCase implements UseCase<CreateNewAiPlayerUseCase.Request, CreateNewAiPlayerUseCase.Presenter> {
    private final PlayerReferenceGateway players;

    public static class Request {
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

    public static interface Presenter {
        void display(Response response);
    }

    public CreateNewAiPlayerUseCase(PlayerReferenceGateway playerReferenceGateway) {
        players = playerReferenceGateway;
    }


    @Override
    public void execute(Request request, Presenter presenter) {
        Response response = new Response();
        response.createdPlayerId = players.create(new AiPlayerReference());
        presenter.display(response);
    }
}
