package com.crudetech.tictactoe.usecases;

import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.Player;
import com.crudetech.tictactoe.game.TicTacToeGame;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class CreateNewGameUseCaseTest {
    private final Grid.Mark startPlayersMark = Grid.Mark.Cross;
    private PlayerReference startPlayerReference;
    private PlayerReference secondPlayerReference;
    private final Object startPlayerId = "__startPlayerId__";
    private final Object secondPlayerId = "__secondPlayerId__";
    private final Object gameId = "__gameId__";
    private PlayerGateway mockPlayerGateway;
    private CreateNewGameUseCase createGame;
    private GameGateway mockGameGateway;
    private PlayerFactory mockPlayerFactory;
    private TicTacToeGame spiedGame;
    private Player startPlayer;


    @Before
    public void setUp() throws Exception {
        setupPlayerGateway();

        setupPlayerFactory();

        setupUseCase();
    }

    private void setupUseCase() {
        mockGameGateway = mock(GameGateway.class);
        createGame = new CreateNewGameUseCase(mockPlayerGateway, mockPlayerFactory, mockGameGateway) {
            @Override
            GameBuilder gameBuilder() {
                return new GameBuilder() {
                    @Override
                    TicTacToeGame newGame(Player startingPlayer, Player otherPlayer) {
                        spiedGame = spy(super.newGame(startingPlayer, otherPlayer));
                        return spiedGame;
                    }
                };
            }
        };
    }

    private void setupPlayerFactory() {
        startPlayer = mock(Player.class);
        Player secondPlayer = mock(Player.class);
        mockPlayerFactory = mock(PlayerFactory.class);
        when(mockPlayerFactory.create(startPlayerReference)).thenReturn(startPlayer);
        when(mockPlayerFactory.create(secondPlayerReference)).thenReturn(secondPlayer);
    }

    private void setupPlayerGateway() {
        startPlayerReference = new PlayerReference();
        secondPlayerReference = new PlayerReference();
        mockPlayerGateway = mock(PlayerGateway.class);
        when(mockPlayerGateway.fetchById(startPlayerId)).thenReturn(startPlayerReference);
        when(mockPlayerGateway.fetchById(secondPlayerId)).thenReturn(secondPlayerReference);
    }

    @Test
    public void givenTwoPlayers_PlayerReferencesAreRetrieved() throws Exception {
        UseCase.Presenter<CreateNewGameUseCase.Response> presenterMock = presenterMock();
        CreateNewGameUseCase.Request request = createRequest();

        createGame.execute(request, presenterMock);

        verify(mockPlayerGateway).fetchById(startPlayerId);
        verify(mockPlayerGateway).fetchById(secondPlayerId);
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
        UseCase.Presenter<CreateNewGameUseCase.Response> presenterMock = presenterMock();
        CreateNewGameUseCase.Request request = createRequest();

        createGame.execute(request, presenterMock);

        verify(mockGameGateway).add(spiedGame);
    }

    @Test
    public void givenGameCreation_GameIdIsReturned() throws Exception {
        UseCase.Presenter<CreateNewGameUseCase.Response> presenterMock = presenterMock();
        CreateNewGameUseCase.Request request = createRequest();
        when(mockGameGateway.add(any(TicTacToeGame.class))).thenReturn(gameId);

        createGame.execute(request, presenterMock);

        CreateNewGameUseCase.Response expectedResponse = new CreateNewGameUseCase.Response();
        expectedResponse.createdGameId = gameId;
        verify(presenterMock).display(expectedResponse);
    }


    @Test
    public void givenTwoPlayerIds_GamePlayersAreCreated() throws Exception {
        UseCase.Presenter<CreateNewGameUseCase.Response> presenterMock = presenterMock();
        CreateNewGameUseCase.Request request = createRequest();

        createGame.execute(request, presenterMock);

        verify(mockPlayerFactory).create(startPlayerReference);
        verify(mockPlayerFactory).create(secondPlayerReference);
    }


    @Test
    public void createdGameIsStartedWIthCorrectPlayerAndMark() throws Exception {
        UseCase.Presenter<CreateNewGameUseCase.Response> presenterMock = presenterMock();
        CreateNewGameUseCase.Request request = createRequest();

        createGame.execute(request, presenterMock);

        verify(spiedGame).startWithPlayer(startPlayer, startPlayersMark);
    }

    @SuppressWarnings("unchecked")
    private static <TResponse> UseCase.Presenter<TResponse> presenterMock() {
        return (UseCase.Presenter<TResponse>) mock(UseCase.Presenter.class);
    }
}
