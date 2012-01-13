package com.crudetech.tictactoe.client.swing.grid;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class RectangleWidget extends RectangularWidget{
    public RectangleWidget(Rectangle boundary, Color color) {
        super(boundary, color);
    }

    @Override
    public void paintEcs(Graphics2D g2d) {
        g2d.setPaint(getColor());
        g2d.draw(getBoundary());
    }
}
