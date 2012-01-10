package com.crudetech.tictactoe.client.swing.grid;


import java.awt.*;

public class EmptyWidget implements Widget{
    @Override
    public void paint(Graphics2D g2d) {
    }

    @Override
    public void setLocation(int x, int y) {
    }

    @Override
    public void moveBy(int dx, int dy) {
    }

    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass());
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
