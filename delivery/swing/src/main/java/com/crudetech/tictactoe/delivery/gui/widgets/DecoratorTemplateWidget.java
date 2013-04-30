package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.gui.widgets.CoordinateSystem;
import com.crudetech.gui.widgets.GraphicsStream;
import com.crudetech.gui.widgets.Widget;

import java.util.Objects;


public abstract class DecoratorTemplateWidget<TDecorated extends Widget> implements Widget {

    public void paint(GraphicsStream pipe) {
        getDecorated().paint(pipe);
    }

    @Override
    public CoordinateSystem widgetCoordinates() {
        return getDecorated().widgetCoordinates();
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

    abstract TDecorated getDecorated();
}
