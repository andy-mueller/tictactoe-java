package com.crudetech.gui.widgets;

import java.awt.*;
import java.util.Objects;

public abstract class EcsWidget implements Widget {
    private int locationX;
    private int locationY;


    public EcsWidget(int x, int y) {
        locationX = x;
        locationY = y;
    }

    public EcsWidget() {
        this(0, 0);
    }

    @Override
    public void setLocation(int x, int y) {
        locationX = x;
        locationY = y;
    }

    @Override
    public void moveBy(int dx, int dy) {
        locationX += dx;
        locationY += dy;
    }

    public int getLocationX() {
        return locationX;
    }

    public int getLocationY() {
        return locationY;
    }

    @Override
    public void paint(GraphicsStream pipe) {
        pipe.pushTranslation(locationX, locationY);
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

        return Objects.equals(locationX, ecsWidget.locationX)
            && Objects.equals(locationY, ecsWidget.locationY);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationX, locationY);
    }

    protected String getLocationAsString() {
        return String.format("Location[%s,%s]", Integer.toString(locationX), Integer.toString(locationY));
    }
}
