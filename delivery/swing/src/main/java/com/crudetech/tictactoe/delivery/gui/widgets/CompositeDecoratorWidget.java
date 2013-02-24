package com.crudetech.tictactoe.delivery.gui.widgets;

import com.crudetech.gui.widgets.AlphaValue;
import com.crudetech.gui.widgets.GraphicsStream;
import com.crudetech.gui.widgets.Widget;

import java.util.Objects;

public class CompositeDecoratorWidget extends DecoratorWidget {
    private final AlphaValue alpha;

    public CompositeDecoratorWidget(Widget decorated, AlphaValue alpha) {
        super(decorated);
        this.alpha = alpha;
    }

    public void paint(GraphicsStream pipe) {
        pipe.pushAlpha(alpha);
        try {
            super.paint(pipe);
        } finally {
            pipe.popAlpha();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CompositeDecoratorWidget that = (CompositeDecoratorWidget) o;

        return Objects.equals(alpha, that.alpha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), alpha);
    }
}