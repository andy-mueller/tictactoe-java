package com.crudetech.tictactoe.usecases;

import java.util.UUID;

/**
 *
 */
interface PlayerReferenceGateway {
    UUID create(PlayerReference player);

    PlayerReference fetchById(Object playerId);
}
