package com.crudetech.tictactoe.usecases;

import java.util.UUID;

/**
 *
 */
interface PlayerGateway {
    UUID create(PlayerReference player);

    PlayerReference fetchById(Object playerId);
}
