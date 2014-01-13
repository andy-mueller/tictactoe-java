package com.crudetech.tictactoe.delivery.swing.grid;

import javax.swing.*;

class JScoreboard extends JComponent {

    static {
        UIManager.getDefaults().put(ScoreboardUI.class.getSimpleName(), ScoreboardUI.class.getName());
    }

    JScoreboard() {
        updateUI();
    }

    ScoreboardUI getUI() {
        return (ScoreboardUI) this.ui;
    }

    @Override
    public void updateUI() {
        setUI(UIManager.getUI(this));
    }

    @Override
    public String getUIClassID() {
        return ScoreboardUI.class.getSimpleName();
    }
}
