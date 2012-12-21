package com.crudetech.tictactoe.delivery.text.jcurses;

import com.crudetech.tictactoe.ui.UiFeedbackChannel;
import jcurses.util.Message;

class JCursesMessageBoxFeedbackChannel implements UiFeedbackChannel {
    @Override
    public void showMessage(String message) {
        Message msg = new Message("Tic Tac Toe", message, "OK");
        msg.show();
    }
}
