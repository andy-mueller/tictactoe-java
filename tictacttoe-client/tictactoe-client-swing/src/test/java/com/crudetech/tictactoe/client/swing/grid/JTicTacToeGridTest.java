package com.crudetech.tictactoe.client.swing.grid;

import com.crudetech.event.Event;
import com.crudetech.event.EventListener;
import com.crudetech.junit.feature.Equivalent;
import com.crudetech.junit.feature.Feature;
import com.crudetech.junit.feature.Features;
import com.crudetech.tictactoe.game.Grid;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.List;

import static com.crudetech.tictactoe.client.swing.grid.TicTacToeGridModel.ChangedEventObject;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(Features.class)
public class JTicTacToeGridTest {
    @Test
    public void componentUIIsCreatedOnDefaultCtor() {
        JTicTacToeGrid jgrid = new JTicTacToeGrid();
        assertThat(jgrid.getUI(), is(instanceOf(TicTacToeGridUI.class)));
    }

    @Test
    public void modelIsCreatedOnDefaultCtor() {
        JTicTacToeGrid jgrid = new JTicTacToeGrid();
        assertThat(jgrid.getModel(), is(notNullValue()));
    }

    @Test
    public void modelIsSetOnCtor() {
        TicTacToeGridModel model = spy(new TicTacToeGridModel());
        Event<ChangedEventObject> changedEvent = eventMock();
        when(model.changed()).thenReturn(changedEvent);
        JTicTacToeGrid jgrid = new JTicTacToeGrid(model);

        verify(model).changed();
        verify(changedEvent).addListener(anyListener());
        assertThat(jgrid.getModel(), is(model));
    }

    @Test
    public void uiIsSetOnCtor() {
        TicTacToeGridUI ui = spy(new TicTacToeGridUI());
        JTicTacToeGrid jgrid = new JTicTacToeGrid(ui);

        verify(ui).installUI(jgrid);
        assertThat(jgrid.getUI(), is(ui));
    }

    @Test
    public void uiAndModelAreSetOnCtor() {
        TicTacToeGridModel model = spy(new TicTacToeGridModel());
        Event<ChangedEventObject> changedEvent = eventMock();
        when(model.changed()).thenReturn(changedEvent);
        TicTacToeGridUI ui = spy(new TicTacToeGridUI());
        JTicTacToeGrid jgrid = new JTicTacToeGrid(model, ui);

        verify(ui).installUI(jgrid);
        assertThat(jgrid.getUI(), is(ui));
        verify(model).changed();
        verify(changedEvent).addListener(anyListener());
        assertThat(jgrid.getModel(), is(model));
    }

    @SuppressWarnings("unchecked")
    private Event<ChangedEventObject> eventMock() {
        return mock(Event.class);
    }

    @SuppressWarnings("unchecked")
    private EventListener<ChangedEventObject> anyListener() {
        return any(EventListener.class);
    }

    @Test
    public void setUIOverridesInitialUI() {
        JTicTacToeGrid jgrid = new JTicTacToeGrid();
        TicTacToeGridUI initialGridUI = jgrid.getUI();
        TicTacToeGridUI newGridUI = new TicTacToeGridUI();

        jgrid.setUI(newGridUI);
        assertThat(jgrid.getUI(), is(sameInstance(newGridUI)));
        assertThat(jgrid.getUI(), is(not(sameInstance(initialGridUI))));
    }

    @Test
    public void setUIInitializesUIWithComponent() {
        JTicTacToeGrid jgrid = new JTicTacToeGrid();
        TicTacToeGridUI newGridUI = mock(TicTacToeGridUI.class);

        jgrid.setUI(newGridUI);

        verify(newGridUI).installUI(jgrid);
    }

    @Test
    public void setUIUninstallsOldUI() {
        TicTacToeGridUI oldGridUI = mock(TicTacToeGridUI.class);
        JTicTacToeGrid jgrid = new JTicTacToeGrid(oldGridUI);

        jgrid.setUI(new TicTacToeGridUI());

        verify(oldGridUI).uninstallUI(jgrid);
    }

    @Feature(Equivalent.class)
    public static Equivalent.Factory<JTicTacToeGrid.CellClickedEventObject> cellClickedEventObjectEquivalent() {
        return new Equivalent.Factory<JTicTacToeGrid.CellClickedEventObject>() {
            Grid.Location cellLocation = Grid.Location.of(Grid.Row.First, Grid.Column.First);

            @Override
            public JTicTacToeGrid.CellClickedEventObject createItem() {
                return new JTicTacToeGrid.CellClickedEventObject(grid, cellLocation);
            }

            JTicTacToeGrid grid = new JTicTacToeGrid();

            @Override
            public List<JTicTacToeGrid.CellClickedEventObject> createOtherItems() {
                return asList(
                        new JTicTacToeGrid.CellClickedEventObject(grid, Grid.Location.of(Grid.Row.First, Grid.Column.Second)),
                        new JTicTacToeGrid.CellClickedEventObject(new JTicTacToeGrid(), cellLocation)
                );
            }
        };
    }


    private static class TicTacToeGridModelEventRegistrationTrackingStub extends TicTacToeGridModel {
        Event<ChangedEventObject> changedEventSpy;
        EventListener<ChangedEventObject> addedHandler;
        EventListener<ChangedEventObject> removedHandler;

        @Override
        public Event<ChangedEventObject> changed() {
            if (changedEventSpy == null) {
                changedEventSpy = createAndPrepareEventSpy();
            }
            return changedEventSpy;
        }

        @SuppressWarnings("unchecked")
        private Event<ChangedEventObject> createAndPrepareEventSpy() {
            Event<ChangedEventObject> spy = spy(super.changed());
            doAnswer(new Answer<Object>() {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    addedHandler = (EventListener<ChangedEventObject>) invocation.getArguments()[0];
                    return invocation.callRealMethod();
                }
            }).when(spy).addListener(any(EventListener.class));
            doAnswer(new Answer<Object>() {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    removedHandler = (EventListener<ChangedEventObject>) invocation.getArguments()[0];
                    return invocation.callRealMethod();
                }
            }).when(spy).removeListener(any(EventListener.class));
            return spy;
        }
    }

    @Test
    public void setModelUnhooksOldModelEvents() {
        JTicTacToeGrid jgrid = new JTicTacToeGrid();

        TicTacToeGridModelEventRegistrationTrackingStub firstModel = new TicTacToeGridModelEventRegistrationTrackingStub();
        jgrid.setModel(firstModel);
        assertThat(firstModel.addedHandler, is(notNullValue()));

        TicTacToeGridModelEventRegistrationTrackingStub secondModel = new TicTacToeGridModelEventRegistrationTrackingStub();
        jgrid.setModel(secondModel);
        assertThat(firstModel.removedHandler, is(firstModel.addedHandler));
        assertThat(secondModel.addedHandler, is(firstModel.addedHandler));
    }
}
