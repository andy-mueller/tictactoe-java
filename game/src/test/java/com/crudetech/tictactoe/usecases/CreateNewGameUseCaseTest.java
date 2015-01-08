package com.crudetech.tictactoe.usecases;

import com.crudetech.tictactoe.game.Grid;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CreateNewGameUseCaseTest {
    private Object startPlayerId;
    private Object secondPlayerId;
    private Object gameId;
    private PlayerGateway mockPlayerGateway;

    @Before
    public void setUp() throws Exception {
        gameId = "a_test_ID";
        mockPlayerGateway = null;
    }

    @Test
    public void givenTwoPlayers_GameCanBeCreated() throws Exception {
        UseCase.Presenter<CreateNewGameUseCase.Response> presenterMock = presenterMock();
        CreateNewGameUseCase createGame = new CreateNewGameUseCase(mockPlayerGateway);
        CreateNewGameUseCase.Request request = new CreateNewGameUseCase.Request();
        request.startPlayerId = startPlayerId;
        request.startPlayerMark = Grid.Mark.Cross;
        request.otherPlayerId = secondPlayerId;

        createGame.execute(request, presenterMock);

        CreateNewGameUseCase.Response expectedResponse = new CreateNewGameUseCase.Response();
        expectedResponse.createdGameId = gameId;
        verify(presenterMock).display(expectedResponse);
    }

    //players are retrieved
    //game is added to gateway
    //game can be used to play? ->MakeMoveUseCase

    static class CreateNewGameUseCase extends TypedUseCase<CreateNewGameUseCase.Request, CreateNewGameUseCase.Response> {
        public CreateNewGameUseCase(PlayerGateway players) {
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
        }

        public static class Request implements UseCase.Request {
            public Object startPlayerId;
            public Grid.Mark startPlayerMark;
            public Object otherPlayerId;
        }

        @Override
        protected void apply(Request request, Presenter<Response> presenter) {
            Response r = new Response();
            r.createdGameId = "a_test_ID";
            presenter.display(r);
        }
    }

    @SuppressWarnings("unchecked")
    private static <TResponse> UseCase.Presenter<TResponse> presenterMock() {
        return (UseCase.Presenter<TResponse>) mock(UseCase.Presenter.class);
    }
}
