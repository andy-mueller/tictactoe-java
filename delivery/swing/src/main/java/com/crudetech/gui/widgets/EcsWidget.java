package com.crudetech.gui.widgets;

import java.awt.*;
import java.util.Objects;

public abstract class EcsWidget implements Widget {
    private final Point location;

    public EcsWidget(Point location) {
        this(location.x, location.y);
    }

    public EcsWidget(int x, int y) {
        location = new Point(x, y);
    }

    public EcsWidget() {
        this(0, 0);
    }

    @Override
    public void setLocation(int x, int y) {
        this.location.setLocation(x, y);
    }

    @Override
    public Point getLocation() {
        return (Point) location.clone();
    }

    @Override
    public void moveBy(int dx, int dy) {
        location.translate(dx, dy);
    }

    @Override
    public void paint(GraphicsStream pipe) {
        Point loc = getLocation();
        pipe.pushTranslation(loc.x, loc.y);
        try {
            paintEcs(pipe);
        } finally {
            pipe.popTransformation();
        }
    }

    protected abstract void paintEcs(GraphicsStream pipe);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EcsWidget ecsWidget = (EcsWidget) o;

        return Objects.equals(location, ecsWidget.location);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(location);
    }
}
