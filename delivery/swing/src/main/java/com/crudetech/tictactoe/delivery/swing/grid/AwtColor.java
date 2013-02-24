package com.crudetech.tictactoe.delivery.swing.grid;

import com.crudetech.gui.widgets.Color;

public class AwtColor implements Color {
    final java.awt.Color color;
    public static final Color ORANGE = new AwtColor(java.awt.Color.ORANGE);
    public static final Color CYAN = new AwtColor(java.awt.Color.CYAN);

    public AwtColor(java.awt.Color color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AwtColor awtColor = (AwtColor) o;

        return color.equals(awtColor.color);

    }

    @Override
    public int hashCode() {
        return color.hashCode();
    }

    @Override
    public String toString() {
        return "AwtColor{" +
                "color=" + color +
                '}';
    }
}
