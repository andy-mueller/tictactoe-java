package com.crudetech.tictactoe.usecases;

import org.junit.Test;

import java.util.HashMap;
import java.util.Objects;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InitiateNewGameUseCaseTest {

    UseCaseFactory useCaseFactory = new UseCaseFactory();


    @Test
    public void givenTwoPlayers_newGameIsCreated() throws Exception {
        UseCase.Presenter<InitiateGameUseCase.Response> presenter = presenterMock();
        UseCase<InitiateGameUseCase.Response> uc = useCaseFactory.create("initiate-game");

        UseCase.Request.Builder requestBuilder = uc.requestBuilder();
        requestBuilder.withParameter("player1", 48);
        requestBuilder.withParameter("player2", 84);

        UseCase.Request request = requestBuilder.createRequest();

        uc.execute(request, presenter);

        InitiateGameUseCase.Response expected = new InitiateGameUseCase.Response();
        expected.gameId = 42;
        verify(presenter).display(expected);
    }

    @SuppressWarnings("unchecked")
    private <TResponse> UseCase.Presenter<TResponse> presenterMock() {
        return (UseCase.Presenter<TResponse>) mock(UseCase.Presenter.class);
    }

    static class InitiateGameUseCase extends TypedUseCase<InitiateGameUseCase.Request, InitiateGameUseCase.Response> {
        public static class Request implements UseCase.Request {
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
        protected void apply(Request request, Presenter<Response> presenter) {
            Response r = new Response();
            r.gameId = 42;
            presenter.display(r);
        }

        @Override
        public UseCase.Request.Builder requestBuilder() {
            return new MapRequestBuilder() {
                @Override
                public UseCase.Request createRequest() {
                    Request r = new Request();
                    r.player1 = valueFor("player1");
                    r.player2 = valueFor("player2");
                    return r;
                }
            };
        }
    }

    abstract static class TypedUseCase<TRequest extends UseCase.Request, TResponse> implements UseCase<TResponse> {
        @Override
        public final void execute(Request request, Presenter<TResponse> presenter) {
            @SuppressWarnings("unchecked")
            TRequest typedRequest = (TRequest) request;
            apply(typedRequest, presenter);
        }

        protected abstract void apply(TRequest request, Presenter<TResponse> presenter);
    }

    interface UseCase<TResponse> {
        void execute(Request request, Presenter<TResponse> presenter);

        Request.Builder requestBuilder();

        static interface Request {
            public interface Builder {
                void withParameter(String name, Object value);

                Request createRequest();
            }
        }

        public interface Presenter<TResponse> {
            public void display(TResponse response);
        }
    }

    static abstract class MapRequestBuilder implements UseCase.Request.Builder {
        private final HashMap<String, Object> parameters = new HashMap<>();

        @Override
        public void withParameter(String name, Object value) {
            parameters.put(name, value);
        }

        protected Object valueFor(String name) {
            return Objects.requireNonNull(parameters.get(name));
        }
    }

    public class UseCaseFactory {
        @SuppressWarnings({"unchecked"})
        public <TResponse> UseCase<TResponse> create(String useCaseId) {
            switch (useCaseId) {
                case "initiate-game":
                    return (UseCase<TResponse>) new InitiateGameUseCase();
                default:
                    throw new IllegalArgumentException("Unknown use case");
            }
        }
    }
}
