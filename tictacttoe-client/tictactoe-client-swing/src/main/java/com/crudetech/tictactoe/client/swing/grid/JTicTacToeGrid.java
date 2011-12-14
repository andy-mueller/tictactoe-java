package com.crudetech.tictactoe.client.swing.grid;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;

public class JTicTacToeGrid extends JComponent {


    static {
        UIManager.getDefaults().put(TicTacToeUI.class.getSimpleName(), TicTacToeUI.class.getName());
    }

    public JTicTacToeGrid() {
        updateUI();
    }

    public JTicTacToeGrid(TicTacToeUI ui) {
        setUI(ui);
    }

    @Override
    public String getUIClassID() {
        return TicTacToeUI.class.getSimpleName();
    }

    TicTacToeUI getUI() {
        return (TicTacToeUI) this.ui;
    }

    @Override
    public void setUI(ComponentUI newUI) {
        super.setUI(newUI);
    }

    @Override
    public void updateUI() {
        setUI(UIManager.getUI(this));
    }
}
