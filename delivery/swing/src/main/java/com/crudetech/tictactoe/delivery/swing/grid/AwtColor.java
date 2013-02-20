package com.crudetech.tictactoe.delivery.swing.grid;

import com.crudetech.gui.widgets.Color;

class AwtColor implements Color {
    final java.awt.Color color;
    public static final Color ORANGE = new AwtColor(java.awt.Color.ORANGE);
    public static final Color CYAN = new AwtColor(java.awt.Color.CYAN);

    AwtColor(java.awt.Color color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AwtColor awtColor = (AwtColor) o;

        if (!color.equals(awtColor.color)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return color.hashCode();
    }
}
