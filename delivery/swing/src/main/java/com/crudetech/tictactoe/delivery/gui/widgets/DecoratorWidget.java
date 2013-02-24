package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.gui.widgets.GraphicsStream;
import com.crudetech.gui.widgets.Point;
import com.crudetech.gui.widgets.Widget;

import java.util.Objects;


public class DecoratorWidget implements Widget {
    private final Widget decorated;

    public DecoratorWidget(Widget decorated) {
        this.decorated = decorated;
    }

    public void setLocation(int x, int y) {
        decorated.setLocation(x, y);
    }

    public void moveBy(int dx, int dy) {
        decorated.moveBy(dx, dy);
    }

    @Override
    public Point getLocation() {
        return decorated.getLocation();
    }

    public void paint(GraphicsStream pipe) {
        decorated.paint(pipe);
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
}
