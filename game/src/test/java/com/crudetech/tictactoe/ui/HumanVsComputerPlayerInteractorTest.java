package com.crudetech.tictactoe.ui;

import com.crudetech.tictactoe.game.ComputerPlayer;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public abstract class HumanVsComputerPlayerInteractorTest {
    UiPlayer uiPlayer;
    ComputerPlayer computerPlayer;
    HumanVsComputerPlayerInteractor interactor;

    @Before
    public void setUp() throws Exception {
        uiPlayer = spy(createUiPlayer());
        computerPlayer = mock(ComputerPlayer.class);
        interactor = createInteractor();
    }

    UiPlayer createUiPlayer() {
        return new UiPlayer(mock(UiView.class), mock(UiFeedbackChannel.class));
    }

    abstract void humanPlayerMakesMove(Grid.Location location);

    abstract HumanVsComputerPlayerInteractor createInteractor();

    @Test
    public void givenHumanPlayerRaisesEvent_moveIsMadeOnTheGame() {
        interactor.startWithHumanPlayer(Grid.Mark.Cross);

        humanPlayerMakesMove(Grid.Location.of(Grid.Row.First, Grid.Column.Third));

        Grid expectedGrid = LinearRandomAccessGrid.of(
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Cross,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None);
        verify(computerPlayer).yourTurn(expectedGrid);
    }

    @Test
    public void givenStartedWithHumanPlayer_gameStartsWithHumanPlayer() {
        interactor.startWithHumanPlayer(Grid.Mark.Cross);

        verify(uiPlayer).yourTurn(LinearRandomAccessGrid.empty());
        verify(computerPlayer, never()).yourTurn(any(Grid.class));
    }

}
