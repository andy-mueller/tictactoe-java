package com.crudetech.tictactoe.usecases;


import com.crudetech.tictactoe.game.Grid;

import java.util.Objects;

public class PlayerReference {
    private Object id = "__no_id_set_yet__";
    private Presenter presenter = nullPresenter();

    public boolean hasId(Object id) {
        return Objects.equals(this.id, id);
    }

    public void setId(Object id) {
        this.id = id;
    }
    public Object getId() {
        return this.id;
    }


    public void tie(GameReference game, Grid grid) {

    }

    public void youWin(GameReference game, Grid grid, Grid.ThreeInARow threeInARow) {

    }

    public void youLoose(GameReference game, Grid grid, Grid.ThreeInARow threeInARow) {

    }

    public void moveWasMade(GameReference game, Grid grid) {
        presenter.display(grid);
    }

    public void gameAlreadyFinished() {

    }

    public void yourTurn(GameReference game, Grid grid) {
        presenter.display(grid);
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    interface Presenter {
        void display(Grid grid);

        void highlight(Grid.ThreeInARow threeInARow);

        void finished();

        void gameAlreadyFinished();
    }

    void makeMove(GameReference game, Grid.Location move) {
        game.makeMove(this, move);
    }

    private Presenter nullPresenter() {
        return new Presenter() {
            @Override
            public void display(Grid grid) {

            }

            @Override
            public void highlight(Grid.ThreeInARow threeInARow) {

            }

            @Override
            public void finished() {

            }

            @Override
            public void gameAlreadyFinished() {

            }
        };
    }
}
