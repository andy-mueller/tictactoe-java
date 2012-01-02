package com.crudetech.tictactoe.client.swing.grid;

import java.awt.*;
import java.util.Objects;

public class FilledRectangleWidget implements Widget {
    private final Rectangle boundary;
    private final Paint color;

    public FilledRectangleWidget(Rectangle boundary, Paint color) {
        this.boundary = (Rectangle) boundary.clone();
        this.color = color;
    }


    @Override
    public void paint(Graphics2D g2d) {
        g2d.setPaint(color);
        g2d.fill(boundary);
    }

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

        FilledRectangleWidget that = (FilledRectangleWidget) o;

        return Objects.equals(boundary, that.boundary)
            && Objects.equals(color, that.color);

    }

    @Override
    public int hashCode() {
        return Objects.hash(boundary, color);
    }

    @Override
    public String toString() {
        return "FilledRectangleWidget{" +
                "boundary=" + boundary +
                ", color=" + color +
                '}';
    }
}
