package com.crudetech.tictactoe.delivery.swing.grid;

import java.awt.Composite;
import java.awt.Graphics2D;
import java.util.Objects;

public class CompositeDecoratorWidget extends DecoratorWidget {
    private final Composite composite;

    public CompositeDecoratorWidget(Widget decorated, Composite composite) {
        super(decorated);
        this.composite = composite;
    }

    public void paintEcs(Graphics2D g2d) {
        Composite oldComposite = g2d.getComposite();
        g2d.setComposite(composite);
        try {
            super.paintEcs(g2d);
        } finally {
            g2d.setComposite(oldComposite);
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
