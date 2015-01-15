package com.crudetech.tictactoe.usecases;

import com.crudetech.tictactoe.game.Player;

public interface PlayerFactory {
    Player create(PlayerReference reference);
}
