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

    public CoordinateSystem setScale(double scale) {
        this.scale = scale;
        return this;
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

    public CoordinateSystem setLocation(Point location) {
        this.location = location;
        return this;
    }

    @Override
    public String toString() {
        return "CoordinateSystem{" +
                "location=" + location +
                ", scale=" + scale +
                '}';
    }

    public <TTransformable extends Transformable<TTransformable>>
    TTransformable toWorldCoordinates(TTransformable transformable) {
        return transformable.transformBy(toWorldTransformation());
    }

    private Transformation toWorldTransformation() {
        return new Transformation(location.x, location.y, scale);
    }

    public <TTransformable extends Transformable<TTransformable>>
    TTransformable toWidgetCoordinates(TTransformable transformable) {
        return transformable.transformBy(toWidgetTransformation());
    }

    private Transformation toWidgetTransformation() {
        return toWorldTransformation().inverse();
    }
}

