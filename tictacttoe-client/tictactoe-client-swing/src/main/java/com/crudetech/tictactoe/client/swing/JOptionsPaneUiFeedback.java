package com.crudetech.tictactoe.client.swing;

import com.crudetech.tictactoe.ui.UiFeedbackChannel;

import javax.swing.*;
import java.awt.*;

class JOptionsPaneUiFeedback implements UiFeedbackChannel{
    private final Frame parent;

    JOptionsPaneUiFeedback(Frame parent) {
        this.parent = parent;
    }

    @Override
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(parent, message, parent.getTitle(), JOptionPane.INFORMATION_MESSAGE);
    }
}
