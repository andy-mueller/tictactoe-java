package com.crudetech.tictactoe.usecases;

/**
 *
 */
interface UseCase<TResponse> {
    void execute(Request request, Presenter<TResponse> presenter);

    static interface Request {
    }

    public interface Presenter<TResponse> {
        public void display(TResponse response);
    }
}
