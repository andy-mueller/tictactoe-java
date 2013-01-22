package com.crudetech.tictactoe.ui;

import com.crudetech.tictactoe.game.Grid;


public class CallDrivenHumanVsComputerPlayerInteractorTest extends HumanVsComputerPlayerInteractorTest {

    @Override
    void humanPlayerMakesMove(Grid.Location location) {
        uiPlayer.makeMove(location);
    }

    @Override
    HumanVsComputerPlayerInteractor createInteractor() {
        return HumanVsComputerPlayerInteractor.builder()
                .setComputerPlayer(computerPlayer)
                .setHumanPlayer(uiPlayer)
                .build();
    }
}
