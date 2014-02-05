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
        final int height = 150;
        final int width = (int) (height * 0.85);

        Graphics2D pipe = (Graphics2D) g;
        Rectangle bounds = new Rectangle(0, 0, width, height);
        pipe.setColor(Color.WHITE);

        pipe.fill(bounds);

        final int horizontalSeparatorHeight = (int) (height * (1.0 / 3));

        pipe.setFont(new Font("Arial", Font.PLAIN, 20));
        pipe.setColor(Color.RED);
        final int textYPos = (horizontalSeparatorHeight - 20) / 2 + 20;
        pipe.drawString("Marianne", 10, textYPos);


        pipe.setFont(new Font("Arial", Font.PLAIN, 50));
        pipe.setColor(Color.RED);
        final int scoreYPos = 130;
        pipe.drawString("12", 20, scoreYPos);


        pipe.drawLine(0, horizontalSeparatorHeight, width, horizontalSeparatorHeight);

        int vertSeparator = (int) (width * (0.66));
        pipe.drawLine(vertSeparator, horizontalSeparatorHeight, vertSeparator, 0);

    }
}
