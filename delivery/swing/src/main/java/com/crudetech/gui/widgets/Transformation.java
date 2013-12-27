package com.crudetech.gui.widgets;

public class Transformation {
    private final double dx;
    private final double dy;
    private final double s;

    public Transformation(double dx, double dy, double scale) {
        this.dx = dx;
        this.dy = dy;
        this.s = scale;
    }

    public Point transform(Point point) {
        final int x = (int) (point.x * s + dx);
        final int y = (int) (point.y * s + dy);
        return Point.of(x, y);
    }

    public Transformation inverse() {
        final double det = s * s;

        final double s1 = s / det;
        final double dx1 = -dx * s / det;
        final double dy1 = -s * dy / det;

        return new Transformation(dx1, dy1, s1);
    }
}
