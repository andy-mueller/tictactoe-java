package com.crudetech.tictactoe.delivery.swing;

import com.crudetech.tictactoe.ui.UiFeedbackChannel;

import javax.swing.JOptionPane;
import java.awt.EventQueue;
import java.awt.Frame;

class JOptionsPaneUiFeedback implements UiFeedbackChannel{
    private final Frame parent;

    JOptionsPaneUiFeedback(Frame parent) {
        this.parent = parent;
    }

    @Override
    public void showMessage(final String message) {
        Runnable shoMessage = new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(parent, message, parent.getTitle(), JOptionPane.INFORMATION_MESSAGE);
            }
        };

        EventQueue.invokeLater(shoMessage);
    }
}
