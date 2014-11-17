package com.crudetech.tictactoe.usecases;

/**
 *
 */
interface UseCase<TResponse> {
    void execute(Request request, Presenter<TResponse> presenter);

    Request.Builder requestBuilder();

    static interface Request {
        public interface Builder {
            void withParameter(String name, Object value);

            Request createRequest();
        }
    }

    public interface Presenter<TResponse> {
        public void display(TResponse response);
    }
}
