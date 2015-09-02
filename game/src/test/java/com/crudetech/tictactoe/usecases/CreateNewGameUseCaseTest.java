package com.crudetech.tictactoe.usecases;

import com.crudetech.collections.Pair;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.Player;
import com.crudetech.tictactoe.game.TicTacToeGame;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class CreateNewGameUseCaseTest {
    private final Grid.Mark startPlayersMark = Grid.Mark.Cross;
    private PlayerReference startPlayerReference;
    private PlayerReference secondPlayerReference;
    private final Object startPlayerId = "__startPlayerId__";
    private final Object secondPlayerId = "__secondPlayerId__";
    private final Object gameId = "__gameId__";
    private PlayerReferenceGateway mockPlayerReferenceGateway;
    private CreateNewGameUseCase createGame;
    private GameReferenceGateway mockGameReferenceGateway;
    private PlayerFactory mockPlayerFactory;
    private TicTacToeGame spiedGame;
    private Player startPlayer;
    private Player secondPlayer;


    @Before
    public void setUp() throws Exception {
        setupPlayerGateway();

        setupPlayerFactory();

        setupUseCase();
    }

    private void setupUseCase() {
        mockGameReferenceGateway = mock(GameReferenceGateway.class);
        createGame = new CreateNewGameUseCase(mockPlayerReferenceGateway, mockPlayerFactory, mockGameReferenceGateway);
    }

    private void setupPlayerFactory() {
        startPlayer = mock(Player.class);
        secondPlayer = mock(Player.class);
        mockPlayerFactory = mock(PlayerFactory.class);
        when(mockPlayerFactory.create(startPlayerReference)).thenReturn(startPlayer);
        when(mockPlayerFactory.create(secondPlayerReference)).thenReturn(secondPlayer);
    }

    private void setupPlayerGateway() {
        startPlayerReference = new HumanPlayerReference();
        secondPlayerReference = new HumanPlayerReference();
        mockPlayerReferenceGateway = mock(PlayerReferenceGateway.class);
        when(mockPlayerReferenceGateway.fetchById(startPlayerId)).thenReturn(startPlayerReference);
        when(mockPlayerReferenceGateway.fetchById(secondPlayerId)).thenReturn(secondPlayerReference);
    }

    @Test
    public void givenTwoPlayers_PlayerReferencesAreRetrieved() throws Exception {
        CreateNewGameUseCase.Presenter presenterMock = mock(CreateNewGameUseCase.Presenter.class);
        CreateNewGameUseCase.Request request = createNewGameRequest();

        createGame.execute(request, presenterMock);

        verify(mockPlayerReferenceGateway).fetchById(startPlayerId);
        verify(mockPlayerReferenceGateway).fetchById(secondPlayerId);
    }

    private CreateNewGameUseCase.Request createNewGameRequest() {
        CreateNewGameUseCase.Request request = new CreateNewGameUseCase.Request();
        request.startPlayerId = startPlayerId;
        request.startPlayersMark = startPlayersMark;
        request.otherPlayerId = secondPlayerId;
        return request;
    }

    @Test
    public void givenTwoPlayers_NewGameIsCreated() throws Exception {
        CreateNewGameUseCase.Presenter presenterMock = mock(CreateNewGameUseCase.Presenter.class);
        CreateNewGameUseCase.Request request = createNewGameRequest();

        createGame.execute(request, presenterMock);

        verify(mockGameReferenceGateway).add(any(GameReference.class));
    }

    @Test
    public void givenGameCreation_GameIdIsReturned() throws Exception {
        CreateNewGameUseCase.Presenter presenterMock = mock(CreateNewGameUseCase.Presenter.class);
        CreateNewGameUseCase.Request request = createNewGameRequest();
        when(mockGameReferenceGateway.add(any(GameReference.class))).thenReturn(gameId);

        createGame.execute(request, presenterMock);

        CreateNewGameUseCase.Response expectedResponse = new CreateNewGameUseCase.Response();
        expectedResponse.createdGameId = gameId;
        verify(presenterMock).display(expectedResponse);
    }


    @Ignore("Must be resurrected, after game works with IDs")
    @Test
    public void createdGameIsStartedWithCorrectPlayerAndMark() throws Exception {
        CreateNewGameUseCase.Presenter presenterMock = mock(CreateNewGameUseCase.Presenter.class);
        CreateNewGameUseCase.Request request = createNewGameRequest();
        ArgumentCaptor<GameReference> createdGameRecCaptor = ArgumentCaptor.forClass(GameReference.class);

        createGame.execute(request, presenterMock);

        verify(mockGameReferenceGateway).add(createdGameRecCaptor.capture());
        GameReference gameReference = createdGameRecCaptor.getValue();
        Pair<Player, Grid.Mark> expectedPlayerInfo = new Pair<>(startPlayer, startPlayersMark);
        assertThat(gameReference.game.getStartingPlayer(), is(expectedPlayerInfo));
    }
}
