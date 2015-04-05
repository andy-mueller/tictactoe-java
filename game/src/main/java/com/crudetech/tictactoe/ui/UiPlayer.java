package com.crudetech.tictactoe.ui;

import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.Player;
import com.crudetech.tictactoe.game.TicTacToeGame;

public class UiPlayer implements Player {
    private final UiView view;
    private final UiFeedbackChannel uiFeedback;
    private TicTacToeGame game;

    public UiPlayer(UiView view, UiFeedbackChannel uiFeedback) {
        this.view = view;
        this.uiFeedback = uiFeedback;
    }

    @Override
    public void yourTurn(Grid actualGrid) {
        view.setModel(actualGrid);
    }

    @Override
    public void moveWasMade(Grid actualGrid) {
        view.setModel(actualGrid);
    }

    @Override
    public void youWin(Grid actualGrid, Grid.ThreeInARow triple) {
        view.highlight(triple);
        uiFeedback.showMessage("You win!");
    }

    @Override
    public void youLoose(Grid actualGrid, Grid.ThreeInARow triple) {
        view.highlight(triple);
        uiFeedback.showMessage("You loose!");
    }

    @Override
    public void tie(Grid actualGrid) {
        uiFeedback.showMessage("Tie!");
    }

    @Override
    public void setGame(TicTacToeGame game) {
        this.game = game;
    }

    protected void makeMove(Grid.Location location) {
        game.makeMove(this, location);
    }
}
