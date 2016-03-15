package com.crudetech.tictactoe.usecases;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RegisterGameListenerUseCaseTest {
    @Test
    public void givenGameExists_whenListenerIsRegistered_successIsIndicated() throws Exception {
        RegisterGameListenerUseCase uc = new RegisterGameListenerUseCase();

        RegisterGameListenerUseCase.Request request = new RegisterGameListenerUseCase.Request();
        request.gameId = "__gameId__";
        request.listener = new RegisterGameListenerUseCase.GameEventListener() {
        };
        RegisterGameListenerUseCase.Presenter presenter = mock(RegisterGameListenerUseCase.Presenter.class);
        uc.excute(request, presenter);

        verify(presenter).registered("__listenerId__");
    }

    static class RegisterGameListenerUseCase {
        public void excute(Request request, Presenter presenter) {
            presenter.registered("__listenerId__");
        }

        public static class Request {
            public Object gameId;
            public GameEventListener listener;
        }

        public interface GameEventListener {
        }

        public interface Presenter {
            void registered(Object listenerId);
        }
    }

}
