package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.gui.widgets.Color;
import com.crudetech.gui.widgets.EcsWidget;
import com.crudetech.gui.widgets.Rectangle;

import java.util.Objects;


public abstract class RectangularWidget extends EcsWidget {
    private final Rectangle boundary;
    private final Color color;

    public RectangularWidget(Rectangle boundary, Color color) {
        super(boundary.x, boundary.y);
        this.color = color;
        this.boundary = boundary.setLocation(0, 0);
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

    Color getColor() {
        return color;
    }

    Rectangle getBoundary() {
        return boundary;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "location=" + getLocation() +
                "boundary=" + getBoundary() +
                ", color=" + getColor() +
                '}';
    }
}