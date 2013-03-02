package com.crudetech.gui.widgets;

import java.util.Objects;

public abstract class EcsWidget implements Widget {
    private final CoordinateSystem ecs = CoordinateSystem.world();


    public EcsWidget(int x, int y) {
        ecs.translate(x, y);
    }

    public EcsWidget() {
    }

    @Override
    public void paint(GraphicsStream pipe) {
        pipe.pushTranslation(ecs.getLocation().x, ecs.getLocation().y);
        try {
            paintEcs(pipe);
        } finally {
            pipe.popTransformation();
        }
    }

    @Override
    public CoordinateSystem widgetCoordinates() {
        return ecs;
    }

    protected abstract void paintEcs(GraphicsStream pipe);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EcsWidget ecsWidget = (EcsWidget) o;

        return Objects.equals(ecs, ecsWidget.ecs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ecs);
    }

    protected String getLocationAsString() {
        return ecs.toString();
    }
}
