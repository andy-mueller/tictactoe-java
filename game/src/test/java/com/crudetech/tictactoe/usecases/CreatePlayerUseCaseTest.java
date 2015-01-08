package com.crudetech.tictactoe.usecases;

import com.crudetech.matcher.RangeContains;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
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


        assertOnePlayerWasAddedOfType(AiPlayerReference.class);
    }

    private void assertOnePlayerWasAddedOfType(Class<?> type) {
        assertThat(mockGateway.players, contains(type));
        assertThat(mockGateway.players, hasSize(1));
    }

    @Test
    public void createNewHumanPlayerAddsNewHumanPlayerToTheEnvironment() throws Exception {
        UseCase.Presenter<CreateNewHumanPlayerUseCase.Response> newPlayerPresenter = presenterMock();
        UseCase<CreateNewHumanPlayerUseCase.Response> newPlayerUseCase = new CreateNewHumanPlayerUseCase(mockGateway);


        CreateNewHumanPlayerUseCase.Request request = new CreateNewHumanPlayerUseCase.Request();

        newPlayerUseCase.execute(request, newPlayerPresenter);

        assertOnePlayerWasAddedOfType(HumanPlayerReference.class);
    }

    private Matcher<Iterable> contains(Class<?> clazz) {
        return RangeContains.contains(asList(instanceOf(clazz)));
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
