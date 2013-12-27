package com.crudetech.gui.widgets;

public class WidgetJig {
    private final Widget widget;

    public WidgetJig(Widget widget) {

        this.widget = widget;
    }

    public void translate(int dx, int dy) {
        widget.setCoordinateSystem(widget.coordinateSystem().translate(dx, dy));
    }


    public void setLocation(int x, int y) {
        CoordinateSystem newLocation = new CoordinateSystem(Point.of(x, y), widget.coordinateSystem().getScale());
        widget.setCoordinateSystem(newLocation);
    }

    public void scale(double scale) {
        widget.setCoordinateSystem(widget.coordinateSystem().scale(scale));
    }
}
