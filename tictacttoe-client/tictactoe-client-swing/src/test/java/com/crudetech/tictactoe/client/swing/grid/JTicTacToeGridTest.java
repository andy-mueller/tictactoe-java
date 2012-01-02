package com.crudetech.tictactoe.client.swing.grid;

import com.crudetech.event.EventSupport;
import com.crudetech.tictactoe.game.Grid;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.crudetech.tictactoe.client.swing.grid.Model.ChangedEventObject;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class JTicTacToeGridTest {
    @Test
    public void componentUIIsCreated() {
        JTicTacToeGrid jgrid = new JTicTacToeGrid();
        assertThat(jgrid.getUI(), is(instanceOf(TicTacToeGridUI.class)));
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
    @Test
    public void modelChangedEvenTriggersRepaint() {
        final EventSupport<Model.ChangedEventObject<Model<Grid>>> modelChanged = new EventSupport<>();
        final TicTacToeGridModel model = new TicTacToeGridModel() {
            @Override
            public EventSupport<ChangedEventObject<Model<Grid>>> changed() {
                return modelChanged;
            }
        };

        final AtomicBoolean painted = new AtomicBoolean(false);
        JTicTacToeGrid aGrid = new JTicTacToeGrid(model) {
            @Override
            public void repaint() {
                painted.getAndSet(true);
            }
        };

        modelChanged.fireEvent(new ChangedEventObject<Model<Grid>>(aGrid.getModel()));

        MatcherAssert.assertThat(painted.get(), is(true));
    }
}
