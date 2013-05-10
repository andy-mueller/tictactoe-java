package com.crudetech.gui.widgets;

import com.crudetech.lang.Compare;

public class CoordinateSystem {
    public static final double NoScale = 1.0;
    private Point location;
    private double scale;

    public static CoordinateSystem world() {
        return new CoordinateSystem();
    }

    public static CoordinateSystem identity() {
        return world();
    }

    public CoordinateSystem(Point location, double scale) {
        this.location = location;
        this.scale = scale;
    }

    public CoordinateSystem() {
        this(Point.of(0, 0), NoScale);
    }

    public CoordinateSystem(Point location) {
        this(location, NoScale);
    }

    public Point getLocation() {
        return location;
    }


    public double getScale() {
        return scale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CoordinateSystem that = (CoordinateSystem) o;

        return Compare.equals(that.scale, scale, 1e-3)
                && (location.equals(that.location));
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = location.hashCode();
        temp = scale != +0.0d ? Double.doubleToLongBits(scale) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public CoordinateSystem translate(int dx, int dy) {
        location = location.translate(dx, dy);
        return this;
    }

    public CoordinateSystem scale(double scale) {
        this.scale *= scale;
        return this;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "CoordinateSystem{" +
                "location=" + location +
                ", scale=" + scale +
                '}';
    }

    public Rectangle toWorldCoordinates(Rectangle rectangle) {
        return new Rectangle(
                toWorldCoordinates(rectangle.getLocation()),
                toWorldLength(rectangle.width),
                toWorldLength(rectangle.height));
    }

    private int toWorldLength(int length) {
        return (int) (length * scale);
    }

    public Point toWorldCoordinates(Point point) {
        return Point.of(toWorldLength(point.x) + location.x, toWorldLength(point.y) + location.y);
    }

    public Point toWidgetCoordinates(Point world) {
        return Point.of(toWidgetLength(world.x - location.x), toWidgetLength(world.y - location.y));
    }


    public Rectangle toWidgetCoordinates(Rectangle rectangle) {
        return new Rectangle(
                toWidgetCoordinates(rectangle.getLocation()),
                toWidgetLength(rectangle.width),
                toWidgetLength(rectangle.height));
    }

    private int toWidgetLength(int length) {
        return (int) (length / scale);
    }
}
