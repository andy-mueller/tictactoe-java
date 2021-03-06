package com.crudetech.gui.widgets;

public class Point implements Transformable<Point>{
    public final int x, y;
    public static final Point Origin = new Point(0, 0);

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        return x == point.x && y == point.y;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public static Point of(int x, int y) {
        return new Point(x, y);
    }

    public Point translate(int dx, int dy) {
        return of(x + dx, y + dy);
    }

    @Override
    public Point transformBy(Transformation xform) {
        return xform.transform(this);
    }
}
