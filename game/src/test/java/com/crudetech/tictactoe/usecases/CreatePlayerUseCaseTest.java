package com.crudetech.tictactoe.usecases;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.crudetech.matcher.ThrowsException.doesThrow;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class CreatePlayerUseCaseTest {

    static class MockGateway implements PlayerGateway {
        private static final UUID firstId = UUID.randomUUID();
        private final List<Object> players = new ArrayList<>();

        @Override
        public UUID create(Object player) {
            players.add(player);
            return firstId;
        }
    }

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
    public void createNewComputerPlayer() throws Exception {
        UseCase.Presenter<CreateNewPlayerUseCase.Response> newPlayerPresenter = presenterMock();
        PlayerGateway mockGateway = new MockGateway();
        UseCase<CreateNewPlayerUseCase.Response> newPlayerUseCase = new CreateNewPlayerUseCase(mockGateway);

        UseCase.Request.Builder requestBuilder = newPlayerUseCase.requestBuilder();
        requestBuilder.withParameter("playerType", "computer");
        UseCase.Request request = requestBuilder.createRequest();

        newPlayerUseCase.execute(request, newPlayerPresenter);


        CreateNewPlayerUseCase.Response expectedResponse = new CreateNewPlayerUseCase.Response();
        expectedResponse.createdPlayerId = MockGateway.firstId;
        verify(newPlayerPresenter).display(expectedResponse);
    }

    @Test
    public void givenNoPlayerTypeWasSpecified_NoRequestIsBuilt() throws Exception {
        PlayerGateway mockGateway = new MockGateway();
        final UseCase<CreateNewPlayerUseCase.Response> newPlayerUseCase = new CreateNewPlayerUseCase(mockGateway);
        final UseCase.Request.Builder requestBuilder = newPlayerUseCase.requestBuilder();


        requestBuilder.withParameter("playerType", null);
        Runnable executeRequest = new Runnable() {
            @Override
            public void run() {
                requestBuilder.createRequest();
            }
        };

        assertThat(executeRequest, doesThrow(IllegalArgumentException.class));
    }

    @SuppressWarnings("unchecked")
    private <TResponse> UseCase.Presenter<TResponse> presenterMock() {
        return (UseCase.Presenter<TResponse>) mock(UseCase.Presenter.class);
    }
}
