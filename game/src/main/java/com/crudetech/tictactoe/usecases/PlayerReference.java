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
        presenter.tie(this, grid);
    }

    public void youWin(GameReference game, Grid grid, Grid.ThreeInARow threeInARow) {
        presenter.won(this, grid, threeInARow);
    }

    public void youLoose(GameReference game, Grid grid, Grid.ThreeInARow threeInARow) {

    }

    public void moveWasMade(GameReference game, Grid grid) {
        presenter.moveWasMade(this, grid);
    }

    public void gameAlreadyFinished() {

    }

    public void yourTurn(GameReference game, Grid grid) {
        presenter.yourTurn(this, grid);
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    interface Presenter {
        void tie(PlayerReference player, Grid grid);

        void won(PlayerReference player, Grid grid, Grid.ThreeInARow threeInARow);

        void lost(PlayerReference player, Grid grid, Grid.ThreeInARow threeInARow);

        void moveWasMade(PlayerReference player, Grid grid);

        void yourTurn(PlayerReference player, Grid grid);

        void gameAlreadyFinished();
    }

    void makeMove(GameReference game, Grid.Location move) {
        game.makeMove(this, move);
    }

    private Presenter nullPresenter() {
        return new Presenter() {
            @Override
            public void tie(PlayerReference player, Grid grid) {

            }

            @Override
            public void won(PlayerReference player, Grid grid, Grid.ThreeInARow threeInARow) {

            }

            @Override
            public void lost(PlayerReference player, Grid grid, Grid.ThreeInARow threeInARow) {

            }

            @Override
            public void moveWasMade(PlayerReference player, Grid grid) {

            }

            @Override
            public void yourTurn(PlayerReference player, Grid grid) {

            }

            @Override
            public void gameAlreadyFinished() {

            }
        };
    }
}
