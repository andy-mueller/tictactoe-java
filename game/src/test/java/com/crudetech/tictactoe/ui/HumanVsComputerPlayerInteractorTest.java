package com.crudetech.tictactoe.ui;

import com.crudetech.tictactoe.game.ComputerPlayer;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;
import org.junit.Before;
import org.junit.Test;

import static com.crudetech.matcher.ThrowsException.doesThrow;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public abstract class HumanVsComputerPlayerInteractorTest {
    UiPlayer uiPlayer;
    ComputerPlayer computerPlayer;
    HumanVsComputerPlayerInteractor interactor;

    @Before
    public void setUp() throws Exception {
        uiPlayer = spy(new UiPlayer(mock(UiView.class), mock(UiFeedbackChannel.class)));
        computerPlayer = mock(ComputerPlayer.class);
        interactor = createInteractor();
    }

    abstract void humanPlayerMakesMove(Grid.Location location);

    abstract HumanVsComputerPlayerInteractor createInteractor();

    @Test
    public void givenStartedWithHumanPlayer_HumanPlayerCanMakeMove() {
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

    @Test
    public void givenStartedWithHumanPlayer_HumanPlayerCanMakeMoveOnInteractor() {
        interactor.startWithHumanPlayer(Grid.Mark.Cross);

        interactor.makeHumanPlayerMove(Grid.Location.of(Grid.Row.First, Grid.Column.Third));

        Grid expectedGrid = LinearRandomAccessGrid.of(
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Cross,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None);
        verify(computerPlayer).yourTurn(expectedGrid);
    }

    @Test
    public void givenStartedWithHumanPlayer_ComputerPlayerCannotMakeMoveOnInteractor() {
        interactor.startWithHumanPlayer(Grid.Mark.Cross);

        Runnable makeComputerPlayerMoveWhenNotItsTurn = new Runnable() {
            @Override
            public void run() {
                interactor.makeComputerPlayerMove(Grid.Location.of(Grid.Row.First, Grid.Column.Third));
            }
        };

        assertThat(makeComputerPlayerMoveWhenNotItsTurn, doesThrow(IllegalStateException.class));
    }


    @Test
    public void givenStartedWithComputerPlayer_ComputerPlayerCanMakeMoveOnInteractor() {
        interactor.startWithComputerPlayer(Grid.Mark.Cross);

        interactor.makeComputerPlayerMove(Grid.Location.of(Grid.Row.First, Grid.Column.Third));

        Grid expectedGrid = LinearRandomAccessGrid.of(
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None);
        verify(computerPlayer).yourTurn(expectedGrid);
    }
}
