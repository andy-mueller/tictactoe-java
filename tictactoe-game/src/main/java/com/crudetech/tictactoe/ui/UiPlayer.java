package com.crudetech.tictactoe.ui;

import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.Player;

public class UiPlayer implements Player {
    private final UiView view;
    private final UiFeedbackChannel uiFeedback;

    public UiPlayer(UiView view, UiFeedbackChannel uiFeedback) {
        this.view = view;
        this.uiFeedback = uiFeedback;
    }

    @Override
    public void yourTurn(Grid actualGrid) {
        view.setGrid(actualGrid);
    }

    @Override
    public void youWin(Grid actualGrid, Grid.Triple triple) {
        uiFeedback.showMessage("You win!");
        view.highlight(triple);
    }

    @Override
    public void youLoose(Grid actualGrid, Grid.Triple triple) {
        uiFeedback.showMessage("You loose!");
        view.highlight(triple);
    }

    @Override
    public void tie(Grid actualGrid) {
        uiFeedback.showMessage("Tie!");
    }
}
