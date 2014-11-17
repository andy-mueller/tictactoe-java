package com.crudetech.tictactoe.usecases;

/**
 *
 */
public class UseCaseFactory {
    @SuppressWarnings({"unchecked"})
    public <TResponse> UseCase<TResponse> create(String useCaseId) {
        switch (useCaseId) {
            case "initiate-game":
                throw new RuntimeException("initiate-game");
            case "new-player":
                throw new RuntimeException("new-player");
                //return (UseCase<TResponse>) new InitiateNewGameUseCaseTest.CreateNewPlayerUseCase(null);
            default:
                throw new IllegalArgumentException("Unknown use case");
        }
    }
}
