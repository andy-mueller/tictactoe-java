package com.crudetech.tictactoe.usecases;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.crudetech.matcher.RangeContains.contains;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class CreatePlayerUseCaseTest {

    private MockGateway mockGateway;

    static class MockGateway implements PlayerGateway {
        private static final UUID firstId = UUID.randomUUID();
        private final List<Object> players = new ArrayList<>();

        @Override
        public UUID create(Object player) {
            players.add(player);
            return firstId;
        }
    }

    @Before
    public void setUp() throws Exception {
        mockGateway = new MockGateway();
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


    @Test
    public void createNewAiPlayerAddsNewAiPlayerToTheEnvironment() throws Exception {
        UseCase.Presenter<CreateNewAiPlayerUseCase.Response> newPlayerPresenter = presenterMock();
        UseCase<CreateNewAiPlayerUseCase.Response> newPlayerUseCase = new CreateNewAiPlayerUseCase(mockGateway);


        CreateNewAiPlayerUseCase.Request request = new CreateNewAiPlayerUseCase.Request();

        newPlayerUseCase.execute(request, newPlayerPresenter);


        assertThat(mockGateway.players, contains(new Matcher[]{instanceOf(PlayerReference.class)}));
    }

    @SuppressWarnings("unchecked")
    private static <TResponse> UseCase.Presenter<TResponse> presenterMock() {
        return (UseCase.Presenter<TResponse>) mock(UseCase.Presenter.class);
    }

    static class MockPresenter<T> implements UseCase.Presenter<T> {
        private T response;

        @Override
        public void display(T response) {
            this.response = response;
        }

        void verifyLastResponseWas(T expectedResponse) {
            assertThat(this.response, is(expectedResponse));
        }
    }
}
