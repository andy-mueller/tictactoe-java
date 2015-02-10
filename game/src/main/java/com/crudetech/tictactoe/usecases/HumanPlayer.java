package com.crudetech.tictactoe.usecases;

import com.crudetech.tictactoe.game.Grid;
import com.crudetech.tictactoe.game.Player;
import com.crudetech.tictactoe.game.TicTacToeGame;

/**
 *
 */
class HumanPlayer implements Player {
    private GameReference.Presenter presenter = nullPresenter();

    @Override
    public void yourTurn(Grid actualGrid) {
        presenter.display(actualGrid);
    }

    @Override
    public void youWin(Grid actualGrid, Grid.ThreeInARow triple) {
        throw new RuntimeException("Not implemented yet!");
    }

    @Override
    public void youLoose(Grid actualGrid, Grid.ThreeInARow triple) {
        throw new RuntimeException("Not implemented yet!");
    }

    @Override
    public void tie(Grid actualGrid) {
        throw new RuntimeException("Not implemented yet!");
    }

    @Override
    public void setGame(TicTacToeGame game) {
    }

    public void setPresenter(GameReference.Presenter presenter) {
        this.presenter = presenter;
    }

    void resetPresenter() {
        presenter = nullPresenter();
    }

    private GameReference.Presenter nullPresenter() {
        return new GameReference.Presenter() {
            @Override
            public void display(Grid grid) {
            }
        };
    }
}
