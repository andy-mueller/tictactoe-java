package com.crudetech.tictactoe.client.swing.grid;

import com.crudetech.event.EventListener;
import com.crudetech.tictactoe.game.Grid;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;

public class JTicTacToeGrid extends JComponent {
    private final TicTacToeGridModel model;

    static {
        UIManager.getDefaults().put(TicTacToeGridUI.class.getSimpleName(), TicTacToeGridUI.class.getName());
    }

    public JTicTacToeGrid() {
        this(new TicTacToeGridModel());
        updateUI();
    }

    public JTicTacToeGrid(TicTacToeGridUI gridUi) {
        this(new TicTacToeGridModel());
        setUI(gridUi);
    }

    public JTicTacToeGrid(TicTacToeGridModel model) {
        this.model = model;
        updateUI();

        model.changed().addListener(new EventListener<Model.ChangedEventObject<Model<Grid>>>() {
            @Override
            public void onEvent(Model.ChangedEventObject<Model<Grid>> e) {
                repaint();
            }
        });
    }

    public JTicTacToeGrid(TicTacToeGridModel model, TicTacToeGridUI ui) {
        this.model = model;
        setUI(ui);
    }

    @Override
    public String getUIClassID() {
        return TicTacToeGridUI.class.getSimpleName();
    }

    TicTacToeGridUI getUI() {
        return (TicTacToeGridUI) this.ui;
    }

    @Override
    public void setUI(ComponentUI newUI) {
        super.setUI(newUI);
    }

    @Override
    public void updateUI() {
        setUI(UIManager.getUI(this));
    }

    public TicTacToeGridModel getModel() {
        return model;
    }
}
