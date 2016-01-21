package com.crudetech.tictactoe.usecases;


import com.crudetech.tictactoe.game.Grid;

import java.util.Objects;

public class PlayerReference {
    private Object id = "__no_id_set_yet__";

    public boolean hasId(Object id) {
        return Objects.equals(this.id, id);
    }

    public void setId(Object id) {
        this.id = id;
    }
    public Object getId() {
        return this.id;
    }

    public void yourTurn(GameReference game) {

    }

    void makeMove(GameReference game, Grid.Location move) {
        game.makeMove(getId(), move, nullPresenter());
    }

    private GameReference.Presenter nullPresenter() {
        return new GameReference.Presenter() {
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
