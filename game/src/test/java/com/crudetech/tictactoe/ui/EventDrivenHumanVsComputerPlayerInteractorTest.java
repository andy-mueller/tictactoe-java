package com.crudetech.tictactoe.ui;


import com.crudetech.event.EventSupport;
import com.crudetech.tictactoe.game.Grid;
import org.junit.Before;
import org.junit.Test;

import static com.crudetech.matcher.RangeIsEmpty.isEmpty;
import static org.hamcrest.MatcherAssert.assertThat;

public class EventDrivenHumanVsComputerPlayerInteractorTest extends HumanVsComputerPlayerInteractorTest {

    private EventSupport<CellEventObject<Object>> event;

    @Before
    public void setUp() throws Exception {
        event = new EventSupport<CellEventObject<Object>>();
        super.setUp();
    }


    @Override
    HumanVsComputerPlayerInteractor createInteractor() {
        return HumanVsComputerPlayerInteractor.builder()
                .setComputerPlayer(computerPlayer)
                .setHumanPlayer(uiPlayer)
                .setHumanPlayerMadeMove(event)
                .build();
    }

    @Override
    void humanPlayerMakesMove(Grid.Location location) {
        event.fireEvent(new CellEventObject<Object>(this, location));
    }

    @Test
    public void givenEventDrivenInteractorIsDestroyed_EventsAreUnhooked() {
        interactor.startWithHumanPlayer(Grid.Mark.Cross);

        interactor.destroy();

        assertThat(event.getListeners(), isEmpty());
    }
}
