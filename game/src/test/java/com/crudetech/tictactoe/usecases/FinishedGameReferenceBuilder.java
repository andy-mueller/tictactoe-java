package com.crudetech.tictactoe.usecases;

import com.crudetech.tictactoe.game.TicTacToeGameFsm;
import com.crudetech.tictactoe.game.TicTacToeGameMother;

/**
 * Creates a game is started, finished and won by the O player:
 * <pre>
 *    |   | O
 * ---+---+---
 *  X | X | O
 * ---+---+---
 *  X | O | O
 * </pre>
 * <p/>
 * It is the no players turn now
 */
class FinishedGameReferenceBuilder extends GameReference.Builder {
    @Override
    public GameReference build() {
        TicTacToeGameMother gameMother = new TicTacToeGameMother();
        withGrid(gameMother.finishedGridWithNoughtsWinning());
        withState(TicTacToeGameFsm.State.StartingPlayerWins);
        return super.build();
    }
}
