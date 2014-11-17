package com.crudetech.tictactoe.usecases;

import org.junit.Test;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InitiateNewGameUseCaseTest {

    UseCaseFactory useCaseFactory = new UseCaseFactory();

    @Test
    public void createNewPlayer() throws Exception {
        UseCase.Presenter<CreateNewPlayerUseCase.Response> newPlayerPresenter = presenterMock();
        PlayerGateway mockGateway = new MockGateway();
        UseCase<CreateNewPlayerUseCase.Response> newPlayerUseCase = new CreateNewPlayerUseCase(mockGateway);
        UseCase.Request.Builder requestBuilder = newPlayerUseCase.requestBuilder();

        UseCase.Request request = requestBuilder.createRequest();

        newPlayerUseCase.execute(request, newPlayerPresenter);


        CreateNewPlayerUseCase.Response expectedResponse = new CreateNewPlayerUseCase.Response();
        expectedResponse.createdPlayerId = MockGateway.firstId;
        verify(newPlayerPresenter).display(expectedResponse);

    }

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

    interface PlayerGateway {
        UUID create(Object player);
    }

    static class MockGateway implements PlayerGateway {
        private static final UUID firstId = UUID.randomUUID();

        @Override
        public UUID create(Object player) {
            return firstId;
        }
    }


    static class CreateNewPlayerUseCase extends TypedUseCase<CreateNewPlayerUseCase.Request, CreateNewPlayerUseCase.Response> {
        static class Request implements UseCase.Request {
        }

        static class Response {
            public Object createdPlayerId;

            @Override
            public boolean equals(Object o) {
                if (isEqualInInstanceOrType(this, o)) return true;

                Response response = (Response) o;

                return Objects.equals(createdPlayerId, response.createdPlayerId);

            }

            private boolean isEqualInInstanceOrType(Response response, Object that) {
                if (response == that) return true;
                if (that == null || response.getClass() != that.getClass()) return false;
                return false;
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
        protected void apply(Request request, Presenter<CreateNewPlayerUseCase.Response> presenter) {
            Response response = new Response();
            response.createdPlayerId = players.create(null);
            presenter.display(response);
        }

        @Override
        public Request.Builder requestBuilder() {
            return new MapRequestBuilder() {
                @Override
                public UseCase.Request createRequest() {
                    return new Request();
                }
            };
        }
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
            Object player1 = request.player1;
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
                case "new-player":
                    return (UseCase<TResponse>) new CreateNewPlayerUseCase(null);
                default:
                    throw new IllegalArgumentException("Unknown use case");
            }
        }
    }
}
