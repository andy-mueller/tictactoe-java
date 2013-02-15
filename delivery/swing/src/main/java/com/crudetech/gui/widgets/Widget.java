package com.crudetech.gui.widgets;

import java.awt.*;

public interface Widget {
    void paintEcs(Graphics2D g2d);
    void setLocation(int x, int y);
    Point getLocation();
    void moveBy(int dx, int dy);
}
