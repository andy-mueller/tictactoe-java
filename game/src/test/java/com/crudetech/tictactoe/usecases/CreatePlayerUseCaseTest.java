package com.crudetech.tictactoe.usecases;

import org.junit.Test;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class CreatePlayerUseCaseTest {

    static class MockGateway implements PlayerGateway {
        private static final UUID firstId = UUID.randomUUID();

        @Override
        public UUID create(Object player) {
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

    @SuppressWarnings("unchecked")
    private <TResponse> UseCase.Presenter<TResponse> presenterMock() {
        return (UseCase.Presenter<TResponse>) mock(UseCase.Presenter.class);
    }
}
