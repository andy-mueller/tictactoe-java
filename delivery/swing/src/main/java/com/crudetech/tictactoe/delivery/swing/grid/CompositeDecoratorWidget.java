package com.crudetech.tictactoe.delivery.swing.grid;

import com.crudetech.gui.widgets.GraphicsStream;
import com.crudetech.gui.widgets.Widget;

import java.awt.*;
import java.util.Objects;

public class CompositeDecoratorWidget extends DecoratorWidget {
    private final Composite composite;

    public CompositeDecoratorWidget(Widget decorated, Composite composite) {
        super(decorated);
        this.composite = composite;
    }

    public void paint(GraphicsStream pipe) {
        pipe.pushComposite(composite);
        try {
            super.paint(pipe);
        } finally {
            pipe.popComposite();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CompositeDecoratorWidget that = (CompositeDecoratorWidget) o;

        return Objects.equals(composite, that.composite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), composite);
    }
}
