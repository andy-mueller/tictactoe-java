package com.crudetech.tictactoe.usecases;

import org.junit.Test;

import java.util.Objects;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InitiateNewGameUseCase2Test {

    UseCaseFactory useCaseFactory = new UseCaseFactory();


    @Test
    public void givenTwoPlayers_newGameIsCreated() throws Exception {
        UseCase.Presenter<InitiateGameUseCase.Response> presenter = presenterMock();
        UseCase<InitiateGameUseCase.Request, InitiateGameUseCase.Response> uc = useCaseFactory.create("initiate-game");

        InitiateGameUseCase.Request request = new InitiateGameUseCase.Request();
        request.player1 = 48;
        request.player2 = 84;


        uc.execute(request, presenter);

        InitiateGameUseCase.Response expected = new InitiateGameUseCase.Response();
        expected.gameId = 42;
        verify(presenter).display(expected);
    }

    @SuppressWarnings("unchecked")
    private <TResponse> UseCase.Presenter<TResponse> presenterMock() {
        return (UseCase.Presenter<TResponse>) mock(UseCase.Presenter.class);
    }

    static class InitiateGameUseCase implements UseCase<InitiateGameUseCase.Request, InitiateGameUseCase.Response> {

        public static class Request {
            public Object player1;
            public Object player2;
        }

        public static class Response {
            public Object gameId;

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                Response response = (Response) o;

                return !(gameId != null ? !gameId.equals(response.gameId) : response.gameId != null);

            }

            @Override
            public int hashCode() {
                return Objects.hashCode(gameId);
            }
        }

        @Override
        public void execute(Request request, Presenter<Response> presenter) {
            Response r = new Response();
            r.gameId = 42;
            presenter.display(r);
        }
    }


    interface UseCase<TRequest, TResponse> {
        void execute(TRequest request, Presenter<TResponse> presenter);

        public interface Presenter<TResponse> {
            public void display(TResponse response);
        }
    }


    public class UseCaseFactory {
        @SuppressWarnings({"unchecked"})
        public <TRequest, TResponse> UseCase<TRequest, TResponse> create(String useCaseId) {
            switch (useCaseId) {
                case "initiate-game":
                    return (UseCase<TRequest, TResponse>) new InitiateGameUseCase();
                default:
                    throw new IllegalArgumentException("Unknown use case");
            }
        }
    }
}
