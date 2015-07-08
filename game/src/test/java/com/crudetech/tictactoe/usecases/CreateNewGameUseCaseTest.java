package com.crudetech.tictactoe.usecases;

import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.Player;
import com.crudetech.tictactoe.game.TicTacToeGame;
import org.junit.Before;
import org.junit.Test;

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
        createGame = new CreateNewGameUseCase(mockPlayerReferenceGateway, mockPlayerFactory, mockGameReferenceGateway) {
            @Override
            GameReference.Builder gameReferenceBuilder() {
                return new GameReference.Builder() {
                    @Override
                    TicTacToeGame newGame(Player startingPlayer, Player otherPlayer) {
                        spiedGame = super.newGame(startingPlayer, otherPlayer);
                        return spiedGame;
                    }
                };
            }
        };
    }

    private void setupPlayerFactory() {
        startPlayer = mock(Player.class);
        secondPlayer = mock(Player.class);
        mockPlayerFactory = mock(PlayerFactory.class);
        when(mockPlayerFactory.create(startPlayerReference)).thenReturn(startPlayer);
        when(mockPlayerFactory.create(secondPlayerReference)).thenReturn(secondPlayer);
    }

    private void setupPlayerGateway() {
        startPlayerReference = new PlayerReference();
        secondPlayerReference = new PlayerReference();
        mockPlayerReferenceGateway = mock(PlayerReferenceGateway.class);
        when(mockPlayerReferenceGateway.fetchById(startPlayerId)).thenReturn(startPlayerReference);
        when(mockPlayerReferenceGateway.fetchById(secondPlayerId)).thenReturn(secondPlayerReference);
    }

    @Test
    public void givenTwoPlayers_PlayerReferencesAreRetrieved() throws Exception {
        CreateNewGameUseCase.Presenter presenterMock = mock(CreateNewGameUseCase.Presenter.class);
        CreateNewGameUseCase.Request request = createRequest();

        createGame.execute(request, presenterMock);

        verify(mockPlayerReferenceGateway).fetchById(startPlayerId);
        verify(mockPlayerReferenceGateway).fetchById(secondPlayerId);
    }

    private CreateNewGameUseCase.Request createRequest() {
        CreateNewGameUseCase.Request request = new CreateNewGameUseCase.Request();
        request.startPlayerId = startPlayerId;
        request.startPlayersMark = startPlayersMark;
        request.otherPlayerId = secondPlayerId;
        return request;
    }

    @Test
    public void givenTwoPlayers_NewGameIsCreated() throws Exception {
        CreateNewGameUseCase.Presenter presenterMock = mock(CreateNewGameUseCase.Presenter.class);
        CreateNewGameUseCase.Request request = createRequest();

        createGame.execute(request, presenterMock);

        verify(mockGameReferenceGateway).add(any(GameReference.class));
    }

    @Test
    public void givenGameCreation_GameIdIsReturned() throws Exception {
        CreateNewGameUseCase.Presenter presenterMock = mock(CreateNewGameUseCase.Presenter.class);
        CreateNewGameUseCase.Request request = createRequest();
        when(mockGameReferenceGateway.add(any(GameReference.class))).thenReturn(gameId);

        createGame.execute(request, presenterMock);

        CreateNewGameUseCase.Response expectedResponse = new CreateNewGameUseCase.Response();
        expectedResponse.createdGameId = gameId;
        verify(presenterMock).display(expectedResponse);
    }


    @Test
    public void givenTwoPlayerIds_GamePlayersAreCreated() throws Exception {
        CreateNewGameUseCase.Presenter presenterMock = mock(CreateNewGameUseCase.Presenter.class);
        CreateNewGameUseCase.Request request = createRequest();

        createGame.execute(request, presenterMock);

        verify(mockPlayerFactory).create(startPlayerReference);
        verify(mockPlayerFactory).create(secondPlayerReference);
    }


    @Test
    public void createdGameIsStartedWithCorrectPlayerAndMark() throws Exception {
        CreateNewGameUseCase.Presenter presenterMock = mock(CreateNewGameUseCase.Presenter.class);
        CreateNewGameUseCase.Request request = createRequest();

        createGame.execute(request, presenterMock);

        assertThat(spiedGame.getStartingPlayer(), is(startPlayer));
        assertThat(spiedGame.getStartingPlayersMark(), is(startPlayersMark));
    }
}
