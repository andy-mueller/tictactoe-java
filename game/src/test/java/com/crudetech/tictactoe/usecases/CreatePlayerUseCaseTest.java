package com.crudetech.tictactoe.usecases;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    public void createNewAiPlayer() throws Exception {
        UseCase.Presenter<CreateNewAiPlayerUseCase.Response> newPlayerPresenter = presenterMock();
        PlayerGateway mockGateway = new MockGateway();
        UseCase<CreateNewAiPlayerUseCase.Response> newPlayerUseCase = new CreateNewAiPlayerUseCase(mockGateway);


        CreateNewAiPlayerUseCase.Request request = new CreateNewAiPlayerUseCase.Request();

        newPlayerUseCase.execute(request, newPlayerPresenter);


        CreateNewAiPlayerUseCase.Response expectedResponse = new CreateNewAiPlayerUseCase.Response();
        expectedResponse.createdPlayerId = MockGateway.firstId;
        verify(newPlayerPresenter).display(expectedResponse);
    }


    @SuppressWarnings("unchecked")
    private <TResponse> UseCase.Presenter<TResponse> presenterMock() {
        return (UseCase.Presenter<TResponse>) mock(UseCase.Presenter.class);
    }
}
