package com.crudetech.tictactoe.usecases;

import com.crudetech.tictactoe.game.ComputerPlayer;
import com.crudetech.tictactoe.game.Grid;


class SingleMovePlayer extends ComputerPlayer {
    private final Grid.Location move;

    public SingleMovePlayer(Grid.Location otherPlayersMove) {
        move = otherPlayersMove;
    }

    @Override
    protected Grid.Location computeNextMove(Grid actualGrid) {
        return move;
    }
}
