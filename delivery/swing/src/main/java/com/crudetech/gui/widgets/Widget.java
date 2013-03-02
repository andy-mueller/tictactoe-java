package com.crudetech.gui.widgets;

public interface Widget {
    void paint(GraphicsStream pipe);

    CoordinateSystem widgetCoordinates();
}
