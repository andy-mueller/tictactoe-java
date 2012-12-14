package com.crudetech.tictactoe.ui;


import com.crudetech.event.EventSupport;
import com.crudetech.tictactoe.game.ComputerPlayer;
import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.LinearRandomAccessGrid;
import org.junit.Before;
import org.junit.Test;

import static com.crudetech.matcher.RangeIsEmpty.isEmpty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class HumanVsComputerPlayerInteractorTest {

    private EventSupport<CellEventObject<Object>> event;
    private UiView uiView;
    private ComputerPlayer computerPlayer;
    private HumanVsComputerPlayerInteractor interactor;

    @Before
    public void setUp() throws Exception {
        event = new EventSupport<CellEventObject<Object>>();
        uiView = mock(UiView.class);
        computerPlayer = mock(ComputerPlayer.class);
        interactor = new HumanVsComputerPlayerInteractor(
                computerPlayer,
                new HumanPlayer(uiView, mock(UiFeedbackChannel.class), event));
    }

    @Test
    public void givenHumanPlayerRaisesEvent_moveIsMadeOnTheGame() {
        interactor.startWithHumanPlayer(Grid.Mark.Cross);

        event.fireEvent(new CellEventObject<Object>(this, Grid.Location.of(Grid.Row.First, Grid.Column.Third)));

        Grid expectedGrid = LinearRandomAccessGrid.of(
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.Cross,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None,
                Grid.Mark.None, Grid.Mark.None, Grid.Mark.None);
        verify(computerPlayer).yourTurn(expectedGrid);
    }

    @Test
    public void givenInteractorIsStartedForHumanPlayer_gameStartsWithHumanPlayer() {
        interactor.startWithHumanPlayer(Grid.Mark.Cross);

        verify(uiView).setModel(LinearRandomAccessGrid.empty());
        verify(computerPlayer, never()).yourTurn(any(Grid.class));
    }

    @Test
    public void givenInteractorIsDestroyed_EventsAreUnhooked() {
        interactor.startWithHumanPlayer(Grid.Mark.Cross);

        interactor.destroy();

        assertThat(event.getListeners(), isEmpty());
    }
}
