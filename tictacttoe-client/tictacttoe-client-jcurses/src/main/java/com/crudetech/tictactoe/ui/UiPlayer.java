package com.crudetech.tictactoe.ui;

import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.Player;

public class UiPlayer implements Player {
    private final UiView widget;
    private final UiFeedbackChannel uiFeedback;

    public UiPlayer(UiView widget, UiFeedbackChannel uiFeedback) {
        this.widget = widget;
        this.uiFeedback = uiFeedback;
    }

    @Override
    public void yourTurn(Grid actualGrid) {
        widget.setGrid(actualGrid);
        widget.moveCursorToFirstMarkedCell(actualGrid);
    }

    @Override
    public void youWin(Grid actualGrid, Grid.Triple triple) {
        uiFeedback.showMessage("You win!");
        widget.highlight(triple);
    }

    @Override
    public void youLoose(Grid actualGrid, Grid.Triple triple) {
        uiFeedback.showMessage("You loose!");
        widget.highlight(triple);
    }

    @Override
    public void tie(Grid actualGrid) {
        uiFeedback.showMessage("Tie!");
    }
}
