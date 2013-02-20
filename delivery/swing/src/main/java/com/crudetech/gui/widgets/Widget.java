package com.crudetech.gui.widgets;

public interface Widget {
    void paint(GraphicsStream pipe);
    void setLocation(int x, int y);
    void moveBy(int dx, int dy);
}
