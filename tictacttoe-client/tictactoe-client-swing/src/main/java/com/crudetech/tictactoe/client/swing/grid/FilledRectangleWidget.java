package com.crudetech.tictactoe.client.swing.grid;

import java.awt.*;

public class FilledRectangleWidget extends RectangularWidget {

    public FilledRectangleWidget(Rectangle boundary, Paint color) {
        super(boundary, color);
    }


    @Override
    public void paint(Graphics2D g2d) {
        g2d.setPaint(getColor());
        g2d.fill(getBoundary());
    }
}