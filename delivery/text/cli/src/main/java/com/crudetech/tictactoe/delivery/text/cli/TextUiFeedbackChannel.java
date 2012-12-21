package com.crudetech.tictactoe.delivery.text.cli;

import com.crudetech.tictactoe.ui.UiFeedbackChannel;

import java.io.PrintWriter;

public class TextUiFeedbackChannel implements UiFeedbackChannel {
    private final PrintWriter out;

    public TextUiFeedbackChannel(PrintWriter out) {
        this.out = out;
    }

    @Override
    public void showMessage(String message) {
        out.println(message);
    }
}
