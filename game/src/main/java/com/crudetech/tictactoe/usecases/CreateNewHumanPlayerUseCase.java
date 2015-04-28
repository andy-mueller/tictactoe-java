package com.crudetech.tictactoe.usecases;

import java.util.Objects;

/**
 *
 */
public class CreateNewHumanPlayerUseCase implements UseCase<CreateNewHumanPlayerUseCase.Request, CreateNewHumanPlayerUseCase.Presenter> {
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
            return "CreateNewHumanPlayerUseCase.Response{" +
                    "createdPlayerId=" + createdPlayerId +
                    '}';
        }
    }

    public interface Presenter {
        void display(Response response);
    }

    public CreateNewHumanPlayerUseCase(PlayerReferenceGateway players) {
        this.players = players;
    }

    @Override
    public void execute(Request request, Presenter presenter) {
        Response response = new Response();
        response.createdPlayerId = players.create(new HumanPlayerReference());
        presenter.display(response);
    }
}
