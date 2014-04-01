package com.crudetech.gui.widgets;

import java.util.Objects;

public class CompositeDecoratorWidget<TDecorated extends Widget> extends DecoratorWidget<TDecorated> {
    private final AlphaValue alpha;

    public CompositeDecoratorWidget(TDecorated decorated, AlphaValue alpha) {
        super(decorated);
        this.alpha = alpha;
    }

    public void paint(GraphicsStream stream) {
        try (GraphicsStream.Context pipe = stream.newContext()) {
            pipe.pushAlpha(alpha);
            super.paint(pipe);
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

    @Override
    public String toString() {
        return "CompositeDecoratorWidget{" +
                "decorated=" + getDecorated() +
                ",alpha=" + alpha +
                '}';
    }
}
