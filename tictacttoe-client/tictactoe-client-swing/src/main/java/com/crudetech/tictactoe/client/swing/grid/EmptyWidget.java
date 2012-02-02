package com.crudetech.tictactoe.client.swing.grid;


import java.awt.Graphics2D;

public class EmptyWidget extends EcsWidget{
    @Override
    public void paintEcs(Graphics2D g2d) {}

    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass());
    }

    @Override
    public int hashCode() { return -42; }
}
