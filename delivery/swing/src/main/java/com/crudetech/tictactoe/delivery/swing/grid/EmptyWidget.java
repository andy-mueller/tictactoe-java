package com.crudetech.tictactoe.delivery.swing.grid;


import com.crudetech.gui.widgets.EcsWidget;

import java.awt.*;

public class EmptyWidget extends EcsWidget {
    @Override
    public void paintEcs(Graphics2D g2d) {}

    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass());
    }

    @Override
    public int hashCode() { return -42; }
}
