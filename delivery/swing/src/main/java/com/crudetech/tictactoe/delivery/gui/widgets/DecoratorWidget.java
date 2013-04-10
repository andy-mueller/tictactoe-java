package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.gui.widgets.CoordinateSystem;
import com.crudetech.gui.widgets.GraphicsStream;
import com.crudetech.gui.widgets.Widget;

import java.util.Objects;


public class DecoratorWidget implements Widget {
    private final Widget decorated;

    public DecoratorWidget(Widget decorated) {
        this.decorated = decorated;
    }

    public void paint(GraphicsStream pipe) {
        decorated.paint(pipe);
    }

    @Override
    public CoordinateSystem widgetCoordinates() {
        return decorated.widgetCoordinates();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DecoratorWidget that = (DecoratorWidget) o;

        return Objects.equals(decorated, that.decorated);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(decorated);
    }

    Widget getDecorated() {
        return decorated;
    }
}
