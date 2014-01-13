package com.crudetech.tictactoe.delivery.swing.grid;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;

public class ScoreboardUI extends ComponentUI {
    @SuppressWarnings("unused")
    public static ScoreboardUI createUI(JComponent component) {
        return new ScoreboardUI();
    }
}
