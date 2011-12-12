package com.crudetech.tictactoe.client.swing;

import javax.swing.*;

public class SwingApp {
    public static void main(String[] args) {
        setSystemLookAndFeel();

        final JFrame frame = new JFrame("Tic Tac Toe (c) 2011 Andreas Mueller");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setVisible(true);
            }
        });
    }

    private static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
    }
}
