package com.crudetech.gui.widgets;

public interface Widget {
    void paint(GraphicsStream pipe);

    CoordinateSystem coordinateSystem();
    void setCoordinateSystem(CoordinateSystem coordinates);
}
