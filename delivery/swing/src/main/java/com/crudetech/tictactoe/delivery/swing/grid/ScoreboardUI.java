package com.crudetech.tictactoe.delivery.swing.grid;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

public class ScoreboardUI extends ComponentUI {
    @SuppressWarnings("unused")
    public static ScoreboardUI createUI(JComponent component) {
        return new ScoreboardUI();
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        g.setColor(Color.CYAN);
        g.fillRect(10, 10, 200, 200);
    }
}
