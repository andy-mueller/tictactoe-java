package com.crudetech.tictactoe.usecases;

/**
 *
 */
abstract class TypedUseCase<TRequest extends UseCase.Request, TResponse> implements UseCase<TResponse> {
    @Override
    public final void execute(Request request, Presenter<TResponse> presenter) {
        @SuppressWarnings("unchecked")
        TRequest typedRequest = (TRequest) request;
        apply(typedRequest, presenter);
    }

    protected abstract void apply(TRequest request, Presenter<TResponse> presenter);
}
