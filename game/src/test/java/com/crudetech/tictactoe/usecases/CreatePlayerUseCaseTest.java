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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class CreatePlayerUseCaseTest {

    private MockGateway mockGateway;

    static class MockGateway implements PlayerGateway {
        private static final UUID firstId = UUID.randomUUID();
        private final List<Object> players = new ArrayList<>();

        @Override
        public UUID create(PlayerReference player) {
            players.add(player);
            return firstId;
        }

        @Override
        public PlayerReference fetchById(Object playerId) {
            throw new RuntimeException("Not implemented yet!");
        }
    }

    @Before
    public void setUp() throws Exception {
        mockGateway = new MockGateway();
    }

    @Test
    public void createNewAiPlayer() throws Exception {
        CreateNewAiPlayerUseCase.Presenter newPlayerPresenter = mock(CreateNewAiPlayerUseCase.Presenter.class);

        UseCase<CreateNewAiPlayerUseCase.Request, CreateNewAiPlayerUseCase.Presenter> newPlayerUseCase = new CreateNewAiPlayerUseCase(mockGateway);


        CreateNewAiPlayerUseCase.Request request = new CreateNewAiPlayerUseCase.Request();

        newPlayerUseCase.execute(request, newPlayerPresenter);


        CreateNewAiPlayerUseCase.Response expectedResponse = new CreateNewAiPlayerUseCase.Response();
        expectedResponse.createdPlayerId = MockGateway.firstId;
        verify(newPlayerPresenter).display(expectedResponse);
    }


    @Test
    public void createNewAiPlayerAddsNewAiPlayerToTheEnvironment() throws Exception {
        CreateNewAiPlayerUseCase.Presenter newPlayerPresenter = mock(CreateNewAiPlayerUseCase.Presenter.class);
        UseCase<CreateNewAiPlayerUseCase.Request, CreateNewAiPlayerUseCase.Presenter> newPlayerUseCase = new CreateNewAiPlayerUseCase(mockGateway);


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
        CreateNewHumanPlayerUseCase.Presenter newPlayerPresenter = mock(CreateNewHumanPlayerUseCase.Presenter.class);
        UseCase<CreateNewHumanPlayerUseCase.Request, CreateNewHumanPlayerUseCase.Presenter> newPlayerUseCase = new CreateNewHumanPlayerUseCase(mockGateway);


        CreateNewHumanPlayerUseCase.Request request = new CreateNewHumanPlayerUseCase.Request();

        newPlayerUseCase.execute(request, newPlayerPresenter);

        assertOnePlayerWasAddedOfType(HumanPlayerReference.class);
    }

    private Matcher<Iterable> contains(Class<?> clazz) {
        return RangeContains.contains(asList(instanceOf(clazz)));
    }
}
