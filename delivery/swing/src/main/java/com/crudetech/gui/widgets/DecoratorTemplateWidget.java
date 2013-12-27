package com.crudetech.gui.widgets;

import java.util.Objects;


public abstract class DecoratorTemplateWidget<TDecorated extends Widget> implements Widget {

    public void paint(GraphicsStream pipe) {
        getDecorated().paint(pipe);
    }

    @Override
    public CoordinateSystem coordinateSystem() {
        return getDecorated().coordinateSystem();
    }

    @Override
    public void setCoordinateSystem(CoordinateSystem coordinates) {
        getDecorated().setCoordinateSystem(coordinates);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DecoratorTemplateWidget that = (DecoratorTemplateWidget) o;

        return Objects.equals(getDecorated(), that.getDecorated());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getDecorated());
    }

    protected abstract TDecorated getDecorated();
}
