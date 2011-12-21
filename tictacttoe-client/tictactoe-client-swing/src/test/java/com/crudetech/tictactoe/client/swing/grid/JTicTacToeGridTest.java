package com.crudetech.tictactoe.client.swing.grid;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class JTicTacToeGridTest {
    @Test
    public void componentUIIsCreated() {
        JTicTacToeGrid jgrid = new JTicTacToeGrid();
        assertThat(jgrid.getUI(), is(instanceOf(TicTacToeUI.class)));
    }
    @Test
    public void setUIOverridesInitialUI() {
        JTicTacToeGrid jgrid = new JTicTacToeGrid();
        TicTacToeUI initialUI = jgrid.getUI();
        TicTacToeUI newUI = new TicTacToeUI();

        jgrid.setUI(newUI);
        assertThat(jgrid.getUI(), is(sameInstance(newUI)));
        assertThat(jgrid.getUI(), is(not(sameInstance(initialUI))));
    }
    @Test
    public void setUIInitializesUIWithComponent() {
        JTicTacToeGrid jgrid = new JTicTacToeGrid();
        TicTacToeUI newUI = mock(TicTacToeUI.class);

        jgrid.setUI(newUI);

        verify(newUI).installUI(jgrid);
    }
    @Test
    public void setUIUninstallsOldUI() {
        TicTacToeUI oldUI = mock(TicTacToeUI.class);
        JTicTacToeGrid jgrid = new JTicTacToeGrid(oldUI);

        jgrid.setUI(new TicTacToeUI());

        verify(oldUI).uninstallUI(jgrid);
    }
}
