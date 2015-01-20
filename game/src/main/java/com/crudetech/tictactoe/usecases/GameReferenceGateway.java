package com.crudetech.tictactoe.usecases;

public interface GameReferenceGateway {
    Object add(GameReference game);

    GameReference fetchById(Object gameId);
}
