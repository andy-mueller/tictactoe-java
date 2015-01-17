package com.crudetech.tictactoe.usecases;

import org.junit.Ignore;
import org.junit.Test;

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


    static class PlayGameUseCase implements UseCase<PlayGameUseCase.Request, PlayGameUseCase.Presenter> {
        public static class Request {
        }

        public static class Response {
        }
//        public static interface Presenter{
//            void display(Grid grid);
//            void highlight(Grid.ThreeInARow threeInARow);
//            void gameAlreadyFinished();
//        }

        static class Presenter {
        }

        @Override
        public void execute(Request request, Presenter presenter) {
        }
    }
}
