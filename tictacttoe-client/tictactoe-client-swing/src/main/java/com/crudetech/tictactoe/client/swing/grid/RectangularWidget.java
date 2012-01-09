package com.crudetech.tictactoe.client.swing.grid;

import java.awt.*;
import java.util.Objects;


public abstract class RectangularWidget implements Widget {
    private final Rectangle boundary;
    private final Paint color;

    public RectangularWidget(Rectangle boundary, Paint color) {
        this.color = color;
        this.boundary = (Rectangle) boundary.clone();
    }

    @Override
    public abstract void paint(Graphics2D g2d);

    @Override
    public void setLocation(int x, int y) {
        boundary.x = x;
        boundary.y = y;
    }

    @Override
    public void moveBy(int dx, int dy) {
        boundary.x += dx;
        boundary.y += dy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RectangularWidget that = (RectangularWidget) o;

        return Objects.equals(boundary, that.boundary)
            && Objects.equals(color, that.color);

    }

    @Override
    public int hashCode() {
        return Objects.hash(boundary, color);
    }
    Paint getColor() {
        return color;
    }

    Rectangle getBoundary() {
        return boundary;
    }
    @Override
    public String toString() {
        return getClass().getSimpleName() +  "{" +
                "boundary=" + getBoundary() +
                ", color=" + getColor() +
                '}';
    }
}
