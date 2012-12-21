package com.crudetech.tictactoe.delivery.swing.grid;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Objects;


public class DecoratorWidget implements Widget {
    private final Widget decorated;

    public DecoratorWidget(Widget decorated) {
        this.decorated = decorated;
    }

    public void setLocation(int x, int y) {
        decorated.setLocation(x, y);
    }

    public Point getLocation() {
        return decorated.getLocation();
    }

    public void moveBy(int dx, int dy) {
        decorated.moveBy(dx, dy);
    }

    public void paintEcs(Graphics2D g2d) {
        decorated.paintEcs(g2d);
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
