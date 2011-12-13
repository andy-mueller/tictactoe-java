package com.crudetech.tictactoe.client.swing;


import javax.swing.*;
import javax.swing.plaf.ComponentUI;

public class TicTacToeUI extends ComponentUI {

    public static TicTacToeUI createUI(JComponent component) {
        return new TicTacToeUI();
    }
}
