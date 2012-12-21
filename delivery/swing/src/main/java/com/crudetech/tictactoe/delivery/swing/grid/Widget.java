package com.crudetech.tictactoe.delivery.swing.grid;

import java.awt.Graphics2D;
import java.awt.Point;

public interface Widget {
    void paintEcs(Graphics2D g2d);
    void setLocation(int x, int y);
    Point getLocation();
    void moveBy(int dx, int dy);
}
