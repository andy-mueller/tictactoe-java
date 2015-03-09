package com.crudetech.tictactoe.usecases;

import com.crudetech.tictactoe.game.Player;
import com.crudetech.tictactoe.game.TicTacToeGame;
import org.junit.Test;

public class GameReferenceTest {
    @Test
    public void bootstrap() throws Exception {
        Player startPlayer = null;
        Player otherPlayer = null;
        TicTacToeGame game = new TicTacToeGame(startPlayer, otherPlayer);
        GameReference gameRef = new GameReference(game, startPlayer, otherPlayer);

    }
}