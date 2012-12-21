package com.crudetech.tictactoe.client.swing.grid;

import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Objects;


public abstract class RectangularWidget extends EcsWidget {
    private final Rectangle boundary;
    private final Paint color;

    public RectangularWidget(Rectangle boundary, Paint color) {
        super(new Point(boundary.x, boundary.y));
        this.color = color;
        this.boundary = (Rectangle) boundary.clone();
        this.boundary.setLocation(0, 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        RectangularWidget that = (RectangularWidget) o;

        return Objects.equals(boundary, that.boundary)
                && Objects.equals(color, that.color);

    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), boundary, color);
    }

    Paint getColor() {
        return color;
    }

    Rectangle getBoundary() {
        return boundary;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "boundary=" + getBoundary() +
                ", color=" + getColor() +
                '}';
    }
}
