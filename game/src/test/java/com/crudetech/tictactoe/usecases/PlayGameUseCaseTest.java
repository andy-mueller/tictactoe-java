package com.crudetech.tictactoe.usecases;

import org.junit.Ignore;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class PlayGameUseCaseTest {

    @Ignore
    @Test
    public void givenPlayerMakeMove_NewGridIsPresented() throws Exception {
//        UseCase<PlayGameUseCase.Response> playGame = new PlayGameUseCase();
//
//        UseCase.Presenter<PlayGameUseCase.Response> mockPresenter = newMockPresenter();
//
//        playGame.execute(request, mockPresenter);
//
//        PlayGameUseCase.Response expectedResponse = new PlayGameUseCase.Response();
//        expectedResponse.grid = expectedGrid;
//        verify(mockPresenter).display(expectedGrid);
    }

    private UseCase.Presenter<PlayGameUseCase.Response> newMockPresenter() {
        return mock(UseCase.Presenter.class);
    }

    static class PlayGameUseCase extends TypedUseCase<PlayGameUseCase.Request, PlayGameUseCase.Response> {
        public static class Request implements UseCase.Request {
        }

        public static class Response {
        }
//        public static interface Presenter{
//            void display(Grid grid);
//            void highlight(Grid.ThreeInARow threeInARow);
//            void gameAlreadyFinished();
//        }

        @Override
        protected void apply(Request request, Presenter<Response> presenter) {
        }
    }
}
