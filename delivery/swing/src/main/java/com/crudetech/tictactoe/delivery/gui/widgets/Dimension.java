package com.crudetech.tictactoe.delivery.gui.widgets;

public class Dimension {
    public final int width;
    public final int height;

    public Dimension(int width, int height) {

        this.width = width;
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dimension dimension = (Dimension) o;

        return height == dimension.height && width == dimension.width;

    }

    @Override
    public int hashCode() {
        int result = width;
        result = 31 * result + height;
        return result;
    }
}
