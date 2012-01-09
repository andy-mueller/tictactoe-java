package com.crudetech.tictactoe.client.swing.grid;

import com.crudetech.event.EventSupport;
import com.crudetech.junit.feature.Equivalent;
import com.crudetech.junit.feature.Feature;
import com.crudetech.junit.feature.Features;
import com.crudetech.tictactoe.game.Grid;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.crudetech.tictactoe.client.swing.grid.Model.ChangedEventObject;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(Features.class)
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

        assertThat(painted.get(), is(true));
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
    // setModelUnhooksOldModelEvents
    // setModelHooksOldModelEvents
}
