package com.crudetech.tictactoe.client.swing.grid;

import java.awt.*;

public class RectangleWidget extends RectangularWidget{
    public RectangleWidget(Rectangle boundary, Color color) {
        super(boundary, color);
    }

    @Override
    public void paint(Graphics2D g2d) {
        g2d.setPaint(getColor());
        g2d.draw(getBoundary());
    }
}
