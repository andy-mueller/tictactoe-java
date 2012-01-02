package com.crudetech.tictactoe.client.swing.grid;

import java.awt.*;

public interface Widget {
    void paint(Graphics2D g2d);

    void setLocation(int x, int y);
}
