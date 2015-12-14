package com.crudetech.tictactoe.usecases;

/**
 *
 */
public class UseCaseFactory {
    @SuppressWarnings({"unchecked"})
    public <TRequest, TPresenter> UseCase<TRequest, TPresenter> create(String useCaseId) {
        switch (useCaseId) {
            case "initiate-game":
                throw new RuntimeException("initiate-game");
            case "new-player":
                return (UseCase<TRequest, TPresenter>) new CreateNewGameUseCase(null, null);
//                throw new RuntimeException("new-player");
            //return (UseCase<TResponse>) new InitiateNewGameUseCaseTest.CreateNewPlayerUseCase(null);
            default:
                throw new IllegalArgumentException("Unknown use case");
        }
    }

    public <TUseCase extends UseCase<?, ?>> TUseCase create2(String useCaseId) {
        return (TUseCase) new CreateNewGameUseCase(null, null);
    }

    void test() {
        UseCase<CreateNewGameUseCase.Request, CreateNewGameUseCase.Presenter> uc = create("new-game");
        CreateNewGameUseCase uc2 = create2("new-game");
    }
}
