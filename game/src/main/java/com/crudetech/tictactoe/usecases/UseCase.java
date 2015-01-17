package com.crudetech.tictactoe.usecases;

/**
 *
 */
interface UseCase<TRequest, TPresenter> {
    void execute(TRequest request, TPresenter presenter);
}
