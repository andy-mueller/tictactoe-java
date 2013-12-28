package com.crudetech.gui.widgets;

public class WidgetJig {
    private final Widget widget;

    public WidgetJig(Widget widget) {
        this.widget = widget;
    }

    public void translate(int dx, int dy) {
        widget.setCoordinateSystem(widget.coordinateSystem().translate(dx, dy));
    }


    public void setLocation(Point location) {
        CoordinateSystem newLocation = new CoordinateSystem(location, widget.coordinateSystem().getScale());
        widget.setCoordinateSystem(newLocation);
    }

    public void scale(double scale) {
        widget.setCoordinateSystem(widget.coordinateSystem().scale(scale));
    }

    public void setScale(double scale) {
        CoordinateSystem newLocation = new CoordinateSystem(widget.coordinateSystem().getLocation(), scale);
        widget.setCoordinateSystem(newLocation);
    }
}
