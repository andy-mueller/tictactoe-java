package com.crudetech.tictactoe.usecases;

import com.crudetech.tictactoe.game.TicTacToeGameMother;

/**
 * Creates a game that is still open for both players to win or to produce a tie:
 * <pre>
 *    |   | O
 * ---+---+---
 *  X | X | O
 * ---+---+---
 *  X | O |
 * </pre>
 * <p/>
 * <p/>
 * <p/>
 * It will overwrite any grid state you provide
 */
class AlmostFinishedGameReferenceBuilder extends GameReference.Builder {
    public GameReference build() {
        TicTacToeGameMother gameMother = new TicTacToeGameMother();
        withGrid(gameMother.almostFinishedGrid());
        return super.build();
    }
}
