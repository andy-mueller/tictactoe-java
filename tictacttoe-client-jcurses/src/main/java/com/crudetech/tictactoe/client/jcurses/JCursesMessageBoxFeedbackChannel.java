package com.crudetech.tictactoe.client.jcurses;

import jcurses.util.Message;

class JCursesMessageBoxFeedbackChannel implements UserFeedbackChannel {
    @Override
    public void showMessage(String message) {
        Message msg = new Message("Tic Tac Toe", message, "OK");
        msg.show();
    }
}
