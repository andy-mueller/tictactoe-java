package com.crudetech.gui.widgets;

import java.awt.*;

public interface Widget {
    void paint(GraphicsStream pipe);
    void setLocation(int x, int y);
    Point getLocation();
    void moveBy(int dx, int dy);
}
